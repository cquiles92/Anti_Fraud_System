package antifraud.exception.invalidrequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidFeedbackRequestException extends RuntimeException {
    public InvalidFeedbackRequestException() {
        super("Error: Invalid feedback insertion request");
    }
}


