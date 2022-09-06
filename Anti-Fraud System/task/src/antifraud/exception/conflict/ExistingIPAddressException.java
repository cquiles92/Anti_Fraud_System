package antifraud.exception.conflict;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ExistingIPAddressException extends RuntimeException {
    public ExistingIPAddressException(String ipAddress) {
        super(String.format("Error: IP Address save conflict. IP Address %s already exists.", ipAddress));
    }
}
