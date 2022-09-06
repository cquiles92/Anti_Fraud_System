package antifraud.controller;

import antifraud.dto.DeletedIPAddressDto;
import antifraud.entity.IPAddress;
import antifraud.entity.RegisteredUser;
import antifraud.service.SuspiciousIPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/antifraud/suspicious-ip")
public class SuspiciousIPController {
    @Autowired
    private SuspiciousIPService suspiciousIPService;

    @PostMapping
    public ResponseEntity<IPAddress> addSuspiciousIP(@Valid @RequestBody IPAddress address,
                                                     @AuthenticationPrincipal RegisteredUser user) {
        suspiciousIPService.saveIPAddress(address);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @DeleteMapping("/{ip}")
    public ResponseEntity<DeletedIPAddressDto> deleteSuspiciousIP(@Valid @PathVariable String ip,
                                                                  @AuthenticationPrincipal RegisteredUser user) {
        DeletedIPAddressDto dto = suspiciousIPService.deleteIPAddress(ip);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<IPAddress>> getSuspiciousIPList(@AuthenticationPrincipal RegisteredUser user) {
        List<IPAddress> addressList = suspiciousIPService.getIPAddressList();
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }
}
