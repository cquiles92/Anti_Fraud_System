package antifraud.controller;

import antifraud.dto.StolenCardDto;
import antifraud.entity.RegisteredUser;
import antifraud.entity.StolenCard;
import antifraud.service.StolenCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/antifraud/stolencard")
public class StolenCardController {
    @Autowired
    StolenCardService stolenCardService;

    @PostMapping
    public ResponseEntity<StolenCard> addStolenCard(@Valid @RequestBody StolenCard card,
                                                    @AuthenticationPrincipal RegisteredUser user) {
        stolenCardService.saveStolenCard(card);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<StolenCardDto> deleteStolenCard(@Valid @PathVariable String number,
                                                          @AuthenticationPrincipal RegisteredUser user) {
        StolenCardDto dto = stolenCardService.deleteStolenCard(number);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<StolenCard>> getStolenCardList(@AuthenticationPrincipal RegisteredUser user) {
        List<StolenCard> stolenCards = stolenCardService.getStolenCardList();
        return new ResponseEntity<>(stolenCards, HttpStatus.OK);
    }
}
