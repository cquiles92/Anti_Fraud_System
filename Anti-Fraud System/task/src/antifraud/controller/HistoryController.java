package antifraud.controller;

import antifraud.dto.TransactionDto;
import antifraud.entity.RegisteredUser;
import antifraud.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getTransactionHistoryList(@AuthenticationPrincipal RegisteredUser user) {
        List<TransactionDto> transactionList = historyService.getAllTransactionHistory();
        return new ResponseEntity<>(transactionList, HttpStatus.OK);
    }

    @GetMapping("/{number}")
    public ResponseEntity<List<TransactionDto>> getTransactionHistoryByCardNumber(@AuthenticationPrincipal RegisteredUser user,
                                                                                  @PathVariable String number) {
        List<TransactionDto> transactionList = historyService.getUserTransactionHistory(number);
        return new ResponseEntity<>(transactionList, HttpStatus.OK);
    }
}


