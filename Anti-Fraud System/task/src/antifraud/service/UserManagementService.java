package antifraud.service;

import antifraud.dto.DeletedUserDto;
import antifraud.dto.RegisteredUserDto;
import antifraud.entity.RegisteredUser;
import antifraud.exception.badrequest.AdministratorLockException;
import antifraud.exception.badrequest.AdministratorRoleRequestException;
import antifraud.exception.badrequest.InvalidLockRequestException;
import antifraud.exception.badrequest.InvalidRoleRequestException;
import antifraud.exception.conflict.ExistingUserConflictException;
import antifraud.exception.conflict.ExistingUserRoleException;
import antifraud.exception.notfound.UserNotFoundException;
import antifraud.model.requestBody.UserLockRequest;
import antifraud.model.enumerator.Role;
import antifraud.model.requestBody.RoleModificationRequest;
import antifraud.model.responseBody.UserStatus;
import antifraud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserManagementService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return getRegisteredUser(username);
    }

    public List<RegisteredUserDto> loadUserList() {
        return repository.findAll(Sort.by("id")).stream().map(RegisteredUserDto::new).collect(Collectors.toList());
    }

    public RegisteredUserDto registerUser(@Valid RegisteredUser user) {
        if (repository.findAll().size() > 0) {
            Optional<RegisteredUser> existingUser = repository.findByUsernameIgnoreCase(user.getUsername());
            if (existingUser.isPresent()) {
                throw new ExistingUserConflictException(user.getUsername());
            }
            user.setRole(Role.MERCHANT);
        } else {
            user.setRole(Role.ADMINISTRATOR);
            user.setDisableLock(true);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);

        return new RegisteredUserDto(user);
    }

    public RegisteredUserDto modifyUserRole(RoleModificationRequest modificationRequest) {
        RegisteredUser existingUser = getRegisteredUser(modificationRequest.getUsername());
        Role requestedRole = isValueInRoleEnumMap(modificationRequest.getRole());

        if (requestedRole == Role.ADMINISTRATOR) {
            throw new AdministratorRoleRequestException();
        } else {
            if (existingUser.getRole() == requestedRole) {
                throw new ExistingUserRoleException(existingUser.getName(), existingUser.getRole().name());
            } else {
                existingUser.setRole(requestedRole);
                repository.save(existingUser);
                return new RegisteredUserDto(existingUser);
            }
        }
    }

    public UserStatus modifyUserLockStatus(UserLockRequest userLockRequest) {
        RegisteredUser user = getRegisteredUser(userLockRequest.getUsername());
        boolean lockStatus = getLockRequest(userLockRequest.getOperation());
        if (user.getRole() == Role.ADMINISTRATOR) {
            throw new AdministratorLockException();
        }
        user.setDisableLock(lockStatus);
        repository.save(user);

        return new UserStatus(user);
    }

    public DeletedUserDto deleteUser(String username) {
        Optional<RegisteredUser> existingUser = repository.findByUsernameIgnoreCase(username);
        if (existingUser.isPresent()) {
            DeletedUserDto deletedUserDto = new DeletedUserDto(username);
            repository.delete(existingUser.get());
            return deletedUserDto;
        }
        throw new UserNotFoundException(username);
    }

    //helper methods
    private RegisteredUser getRegisteredUser(String username) {
        Optional<RegisteredUser> user = repository.findByUsernameIgnoreCase(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException(username);
        }

        return user.get();
    }

    private Role isValueInRoleEnumMap(String request) {
        Map<String, Role> roleMap = new HashMap<>();
        for (Role role : Role.values()) {
            roleMap.put(role.name(), role);
        }
        if (roleMap.containsKey(request)) {
            return roleMap.get(request);
        } else {
            throw new InvalidRoleRequestException(request);
        }
    }

    private boolean getLockRequest(String operation) {
        if (operation.equals("LOCK")) {
            return false;
        } else if (operation.equals("UNLOCK")) {
            return true;
        } else {
            throw new InvalidLockRequestException();
        }
    }
}
