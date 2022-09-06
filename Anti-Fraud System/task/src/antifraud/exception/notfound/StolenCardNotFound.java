package antifraud.exception.notfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class StolenCardNotFound extends RuntimeException {
    public StolenCardNotFound(String cardNumber) {
        super(String.format("Error: Card Number %s not found.", cardNumber));
    }
}
