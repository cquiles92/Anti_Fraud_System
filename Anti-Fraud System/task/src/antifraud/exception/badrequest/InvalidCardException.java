package antifraud.exception.badrequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCardException extends RuntimeException {
    public InvalidCardException(String cardNumber) {
        super(String.format("Error: %s is an invalid card number.", cardNumber));
    }
}
