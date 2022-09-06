package antifraud.exception;

import antifraud.exception.badrequest.*;
import antifraud.exception.conflict.*;
import antifraud.exception.invalidrequest.InvalidFeedbackRequestException;
import antifraud.exception.notfound.IPAddressNotFoundException;
import antifraud.exception.notfound.CardNotFoundException;
import antifraud.exception.notfound.TransactionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionControlAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {AdministratorRoleRequestException.class, AdministratorLockException.class,
            InvalidRoleRequestException.class, InvalidLockRequestException.class, InvalidIPAddressException.class,
            InvalidCardException.class, InvalidTransactionException.class, InvalidTransactionFeedbackException.class})
    protected ResponseEntity<String> handleInvalidTransactionOrRequest(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class, IPAddressNotFoundException.class,
            CardNotFoundException.class, TransactionNotFoundException.class})
    protected ResponseEntity<String> handleUserOrRoleNotFound(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ExistingUserConflictException.class, ExistingUserRoleException.class,
            ExistingIPAddressException.class, ExistingStolenCardException.class, ExistingFeedbackException.class})
    protected ResponseEntity<String> handleExistingUserOrRoleConflict(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = InvalidFeedbackRequestException.class)
    protected ResponseEntity<String> handleUnprocessableRequest(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);

    }
}


