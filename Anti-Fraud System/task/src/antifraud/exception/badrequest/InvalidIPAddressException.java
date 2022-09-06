package antifraud.exception.badrequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidIPAddressException extends RuntimeException {
    public InvalidIPAddressException(String ipAddress) {
        super(String.format("Error: IP Address %s is not valid.", ipAddress));
    }
}
