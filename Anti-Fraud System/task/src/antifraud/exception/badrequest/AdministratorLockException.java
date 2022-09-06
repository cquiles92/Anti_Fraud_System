package antifraud.exception.badrequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AdministratorLockException extends RuntimeException {
    public AdministratorLockException() {
        super("Error: Cannot Lock an Administrator account");
    }
}
