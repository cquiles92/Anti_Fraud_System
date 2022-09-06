package antifraud.service;

import antifraud.dto.TransactionDto;
import antifraud.entity.*;
import antifraud.exception.badrequest.InvalidTransactionException;
import antifraud.exception.badrequest.InvalidTransactionFeedbackException;
import antifraud.exception.conflict.ExistingFeedbackException;
import antifraud.exception.invalidrequest.InvalidFeedbackRequestException;
import antifraud.exception.notfound.TransactionNotFoundException;
import antifraud.model.enumerator.TransactionStatus;
import antifraud.model.requestBody.TransactionFeedback;
import antifraud.model.requestBody.TransactionRequest;
import antifraud.model.responseBody.TransactionResult;
import antifraud.repository.CardDetailsRepository;
import antifraud.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CardDetailsRepository cardDetailsRepository;
    @Autowired
    private StolenCardService stolenCardService;
    @Autowired
    private SuspiciousIPService suspiciousIPService;

    public TransactionResult processTransaction(TransactionRequest request) {

        Transaction transaction = new Transaction(request);
        TransactionResult result = new TransactionResult();
        List<String> infoDetails = new ArrayList<>();

        processAmount(transaction, result, infoDetails);
        processTransactionsLastHour(transaction, result, infoDetails);
        processCard(request.getNumber(), result, infoDetails);
        processIPAddress(request.getIp(), result, infoDetails);
        prepareTransactionResultInfo(result, infoDetails);

        transaction.setResult(result.getResult());
        transactionRepository.save(transaction);

        return result;
    }

    public TransactionDto addFeedback(TransactionFeedback feedback) {
        Set<String> values = Arrays.stream(TransactionStatus.values()).map(Enum::name).collect(Collectors.toSet());
        if (!values.contains(feedback.getFeedback())) {
            throw new InvalidTransactionFeedbackException();
        }

        Optional<Transaction> savedTransaction = transactionRepository.findById(feedback.getTransactionId());
        if (savedTransaction.isEmpty()) {
            throw new TransactionNotFoundException(feedback.getTransactionId());
        }


        Transaction transaction = savedTransaction.get();
        if (!transaction.getFeedback().equals("")) {
            throw new ExistingFeedbackException(transaction.getId());
        }

        if (feedback.getFeedback().equals(transaction.getResult())) {
            throw new InvalidFeedbackRequestException();
        }

        processFeedback(feedback, transaction);
        return new TransactionDto(transaction);
    }


    //Helper methods
    private void processAmount(Transaction request, TransactionResult result, List<String> details) {
        CardDetails cardDetails;
        long amount = request.getAmount();

        Optional<CardDetails> repositoryData = cardDetailsRepository.findByNumber(request.getNumber());
        if (repositoryData.isEmpty()) {
            cardDetails = new CardDetails(request.getNumber());
            cardDetailsRepository.save(cardDetails);
        } else {
            cardDetails = repositoryData.get();
        }

        if (amount <= 0) {
            throw new InvalidTransactionException();
        } else if (amount <= cardDetails.getAllowedLimit()) {
            result.setResult(TransactionStatus.ALLOWED.name());
            details.add("none");
        } else if (amount <= cardDetails.getManualLimit()) {
            result.setResult(TransactionStatus.MANUAL_PROCESSING.name());
            details.add("amount");
        } else {
            result.setResult(TransactionStatus.PROHIBITED.name());
            details.add("amount");
        }
    }

    private void processCard(String requestCard, TransactionResult result, List<String> details) {
        Optional<StolenCard> suspiciousCard = stolenCardService.getStolenCard(requestCard);
        if (suspiciousCard.isPresent()) {
            ifNotAlreadyProhibited(result, details);
            details.add("card-number");
        }
    }

    private void processIPAddress(String requestIPAddress, TransactionResult result, List<String> details) {
        Optional<IPAddress> suspiciousIPAddress = suspiciousIPService.getSuspiciousIP(requestIPAddress);
        if (suspiciousIPAddress.isPresent()) {
            ifNotAlreadyProhibited(result, details);
            details.add("ip");
        }
    }

    private void processTransactionsLastHour(Transaction request, TransactionResult result, List<String> details) {
        Set<String> ipAddresses = new HashSet<>(Collections.singletonList(request.getIp()));
        Set<String> geoLocations = new HashSet<>(List.of(request.getRegion().name()));
        LocalDateTime currentTime = request.getDate();
        LocalDateTime lastHour = request.getDate().minus(1, ChronoUnit.HOURS);
        List<Transaction> transactionList = transactionRepository.findTransactionsInLastHour(request.getNumber(), lastHour, currentTime);

        for (Transaction transaction : transactionList) {
            ipAddresses.add(transaction.getIp());
            geoLocations.add(transaction.getRegion().name());
        }

        if (ipAddresses.size() > 3 || geoLocations.size() > 3) {
            ifNotAlreadyProhibited(result, details);
            if (ipAddresses.size() > 3) {
                details.add("ip-correlation");
            }
            if (geoLocations.size() > 3) {
                details.add("region-correlation");
            }
        } else if (ipAddresses.size() == 3 || geoLocations.size() == 3) {
            if (!result.getResult().equals(TransactionStatus.PROHIBITED.name())) {
                if (result.getResult().equals(TransactionStatus.ALLOWED.name())) {
                    details.clear();
                }
                result.setResult(TransactionStatus.MANUAL_PROCESSING.name());
                if (ipAddresses.size() > 2) {
                    details.add("ip-correlation");
                }
                if (geoLocations.size() > 2) {
                    details.add("region-correlation");
                }
            }
        }
    }

    private void prepareTransactionResultInfo(TransactionResult result, List<String> details) {
        if (details.size() == 1) {
            result.setInfo(details.get(0));
        } else {
            Collections.sort(details);
            StringBuilder stringBuilder = new StringBuilder(details.get(0));
            for (int i = 1; i < details.size(); i++) {
                stringBuilder.append(", ").append(details.get(i));
            }
            result.setInfo(stringBuilder.toString());
        }
    }

    private void ifNotAlreadyProhibited(TransactionResult result, List<String> details) {
        if (!result.getResult().equals(TransactionStatus.PROHIBITED.name())) {
            result.setResult(TransactionStatus.PROHIBITED.name());
            details.clear();
        }
    }

    private void processFeedback(TransactionFeedback feedback, Transaction transaction) {
        CardDetails cardDetails = cardDetailsRepository.findByNumber(transaction.getNumber()).orElseThrow();

        if (feedback.getFeedback().equals(TransactionStatus.ALLOWED.name())) {
            if (transaction.getResult().equals(TransactionStatus.PROHIBITED.name())) {
                //increase manual
                cardDetails.setManualLimit(getIncreasedLimit(cardDetails.getManualLimit(), transaction.getAmount()));
            }
            cardDetails.setAllowedLimit(getIncreasedLimit(cardDetails.getAllowedLimit(), transaction.getAmount()));
            //increase allowed

        } else if (feedback.getFeedback().equals(TransactionStatus.MANUAL_PROCESSING.name())) {
            if (transaction.getResult().equals(TransactionStatus.ALLOWED.name())) {
                //decrease allowed
                cardDetails.setAllowedLimit(getDecreasedLimit(cardDetails.getAllowedLimit(), transaction.getAmount()));
            } else {
                //increase manual
                cardDetails.setManualLimit(getIncreasedLimit(cardDetails.getManualLimit(), transaction.getAmount()));
            }
        } else {
            if (transaction.getResult().equals(TransactionStatus.ALLOWED.name())) {
                //decrease allowed
                cardDetails.setAllowedLimit(getDecreasedLimit(cardDetails.getAllowedLimit(), transaction.getAmount()));
            }
            //decrease manual
            cardDetails.setManualLimit(getDecreasedLimit(cardDetails.getManualLimit(), transaction.getAmount()));
        }
        cardDetailsRepository.save(cardDetails);
        transaction.setFeedback(feedback.getFeedback());
        transactionRepository.save(transaction);
    }

    private long getIncreasedLimit(long limit, long transactionValue) {
        return (long) Math.ceil(0.8 * limit + 0.2 * transactionValue);
    }

    private long getDecreasedLimit(long limit, long transactionValue) {
        return (long) Math.ceil(0.8 * limit - 0.2 * transactionValue);
    }
}


