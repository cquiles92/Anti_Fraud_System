package antifraud.exception.conflict;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ExistingUserRoleException extends RuntimeException {
    public ExistingUserRoleException(String username, String role) {
        super(String.format("Error: %s cannot reassign existing role: %s", username, role));
    }
}
