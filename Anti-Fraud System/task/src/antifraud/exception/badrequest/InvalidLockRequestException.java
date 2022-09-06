package antifraud.exception.badrequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidLockRequestException extends RuntimeException {
    public InvalidLockRequestException() {
        super("Error: Invalid Lock request. Must be \"LOCK\" or \"UNLOCK\".");
    }
}
