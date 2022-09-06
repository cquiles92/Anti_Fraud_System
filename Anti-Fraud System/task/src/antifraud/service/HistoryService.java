package antifraud.service;

import antifraud.dto.TransactionDto;
import antifraud.entity.Transaction;
import antifraud.exception.badrequest.InvalidCardException;
import antifraud.exception.notfound.CardNotFoundException;
import antifraud.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private StolenCardService stolenCardService;

    public List<TransactionDto> getAllTransactionHistory() {
        List<Transaction> transactionList = transactionRepository.findAll(Sort.by("id"));
        return transactionList.stream().map(TransactionDto::new).collect(Collectors.toList());
    }

    public List<TransactionDto> getUserTransactionHistory(String cardNumber) {
        if (!stolenCardService.isValidCardNumber(cardNumber)) {
            throw new InvalidCardException(cardNumber);
        }

        List<Transaction> transactionList = transactionRepository.findByNumber(cardNumber);

        if (transactionList.isEmpty()) {
            throw new CardNotFoundException(cardNumber);
        }

        return transactionList.stream().map(TransactionDto::new).collect(Collectors.toList());
    }
}

