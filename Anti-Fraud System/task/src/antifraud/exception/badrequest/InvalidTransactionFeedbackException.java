package antifraud.exception.badrequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidTransactionFeedbackException extends RuntimeException {
    public InvalidTransactionFeedbackException() {
        super("Error: Invalid Transaction Feedback.");
    }
}


