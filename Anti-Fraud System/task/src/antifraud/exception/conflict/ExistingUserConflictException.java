package antifraud.exception.conflict;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ExistingUserConflictException extends RuntimeException {
    public ExistingUserConflictException(String username) {
        super(String.format("Error: Username %s is already registered", username));
    }
}
