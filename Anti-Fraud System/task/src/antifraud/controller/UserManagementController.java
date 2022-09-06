package antifraud.controller;

import antifraud.dto.DeletedUserDto;
import antifraud.dto.RegisteredUserDto;
import antifraud.entity.RegisteredUser;
import antifraud.model.requestBody.RoleModificationRequest;
import antifraud.model.requestBody.UserLockRequest;
import antifraud.model.responseBody.UserStatus;
import antifraud.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@PreAuthorize("isAuthenticated()")
public class UserManagementController {
    @Autowired
    private UserManagementService userManagementService;

    @PreAuthorize("permitAll()")
    @PostMapping("/user")
    public ResponseEntity<RegisteredUserDto> userRegistration(@Valid @RequestBody RegisteredUser user) {
        RegisteredUserDto dto = userManagementService.registerUser(user);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<DeletedUserDto> userDeletion(@AuthenticationPrincipal RegisteredUser user,
                                                       @PathVariable(required = false) String username) {
        DeletedUserDto dto = userManagementService.deleteUser(username);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<RegisteredUserDto>> retrieveUserList(@AuthenticationPrincipal RegisteredUser user) {
        List<RegisteredUserDto> userDtoList = userManagementService.loadUserList();
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    @PutMapping("/role")
    public ResponseEntity<RegisteredUserDto> changeUserRole(@AuthenticationPrincipal RegisteredUser user,
                                                            @Valid @RequestBody RoleModificationRequest modificationRequest) {
        RegisteredUserDto dto = userManagementService.modifyUserRole(modificationRequest);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/access")
    public ResponseEntity<UserStatus> modifyUserLock(@AuthenticationPrincipal RegisteredUser user,
                                                     @Valid @RequestBody UserLockRequest lockRequest) {
        UserStatus status = userManagementService.modifyUserLockStatus(lockRequest);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
