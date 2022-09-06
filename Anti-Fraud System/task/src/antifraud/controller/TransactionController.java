package antifraud.controller;

import antifraud.dto.TransactionDto;
import antifraud.model.requestBody.TransactionFeedback;
import antifraud.entity.RegisteredUser;
import antifraud.entity.Transaction;
import antifraud.model.requestBody.TransactionRequest;
import antifraud.model.responseBody.TransactionResult;
import antifraud.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/antifraud/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResult> verifyTransaction(@Valid @RequestBody TransactionRequest request,
                                                               @AuthenticationPrincipal RegisteredUser user) {
        TransactionResult result = transactionService.processTransaction(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<TransactionDto> addFeedbackToTransaction(@Valid @RequestBody TransactionFeedback feedback,
                                                                   @AuthenticationPrincipal RegisteredUser user) {
        TransactionDto dto = transactionService.addFeedback(feedback);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}


