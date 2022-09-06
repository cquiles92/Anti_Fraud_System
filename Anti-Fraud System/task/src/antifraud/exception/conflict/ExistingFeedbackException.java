package antifraud.exception.conflict;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ExistingFeedbackException extends RuntimeException {
    public ExistingFeedbackException(long feedbackID) {
        super(String.format("Error: Transaction ID: %d already contains feedback.", feedbackID));
    }
}


