package antifraud.exception.badrequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidRoleRequestException extends RuntimeException {
    public InvalidRoleRequestException(String role) {
        super(String.format("Error: %s is not a valid role.", role));
    }
}
