package antifraud.exception.conflict;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ExistingStolenCardException extends RuntimeException {
    public ExistingStolenCardException(String cardNumber) {
        super(String.format("Error: Card number %s is already saved and reported as stolen", cardNumber));
    }
}
