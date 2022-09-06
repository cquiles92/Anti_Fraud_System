package antifraud.service;

import antifraud.dto.StolenCardDto;
import antifraud.entity.StolenCard;
import antifraud.exception.conflict.ExistingStolenCardException;
import antifraud.exception.badrequest.InvalidCardException;
import antifraud.exception.notfound.StolenCardNotFound;
import antifraud.model.enumerator.PaymentCardType;
import antifraud.repository.StolenCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StolenCardService {
    @Autowired
    StolenCardRepository stolenCardRepository;

    public void saveStolenCard(StolenCard stolenCard) {
        Optional<StolenCard> savedCard = stolenCardRepository.findByNumber(stolenCard.getNumber());
        if (savedCard.isPresent()) {
            throw new ExistingStolenCardException(stolenCard.getNumber());
        } else {
            stolenCardRepository.save(stolenCard);
        }
    }

    public StolenCardDto deleteStolenCard(String stolenCardNumber) {
        if (!isValidCardNumber(stolenCardNumber)) {
            throw new InvalidCardException(stolenCardNumber);
        }

        Optional<StolenCard> savedCard = stolenCardRepository.findByNumber(stolenCardNumber);
        if (savedCard.isEmpty()) {
            throw new StolenCardNotFound(stolenCardNumber);
        }
        StolenCardDto dto = new StolenCardDto(savedCard.get());
        stolenCardRepository.delete(savedCard.get());
        return dto;
    }

    public List<StolenCard> getStolenCardList() {
        return stolenCardRepository.findAll(Sort.by("id"));
    }

    protected Optional<StolenCard> getStolenCard(String cardNumber) {
        if (!isValidCardNumber(cardNumber)) {
            throw new InvalidCardException(cardNumber);
        }

        return stolenCardRepository.findByNumber(cardNumber);
    }

    protected boolean isValidCardNumber(String cardNumber) {
        if (isValidCardNumberLength(cardNumber)) {
            return isValidChecksum(cardNumber);
        }
        return false;
    }

    private boolean isValidCardNumberLength(String cardNumber) {
        /*
        Go through a large list of possible card type list ex Visa, MasterCard, Discover
        Check if the length of the given card number is the same as for example MasterCard (length = 16)
        If there is a match check if the prefixes match the bank number
        Return false if the card does not match the criteria
         */

        List<PaymentCardType> cardTypeList = Arrays.stream(PaymentCardType.values())
                .filter(paymentCardType -> paymentCardType.getCardLength() == cardNumber.length())
                .collect(Collectors.toList());

        for (PaymentCardType cardType : cardTypeList) {
            String[] prefixes = cardType.getPrefix();
            for (String prefix : prefixes) {
                String currentCardPrefix = cardNumber.substring(0, prefix.length());
                if (currentCardPrefix.equals(prefix)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isValidChecksum(String cardNumber) {
        /*
        Use Luhn's algorithm to check the checksum digit (last digit)
         */
        int[] cardDigits = new int[cardNumber.length()];
        int total = 0;
        for (int i = 0; i < cardDigits.length; i++) {
            cardDigits[i] = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (i % 2 == 0) {
                cardDigits[i] *= 2;
            }
            total += cardDigits[i] > 9 ? cardDigits[i] - 9 : cardDigits[i];
        }
        total %= 10;
        return total == 0;
    }
}
