package antifraud.exception.badrequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AdministratorRoleRequestException extends RuntimeException {
    public AdministratorRoleRequestException() {
        super("Error: Illegal Request : Cannot have more than one administrator in the system.");
    }
}
