package antifraud.dto;

import antifraud.entity.RegisteredUser;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;

public class RegisteredUserDto implements Serializable {
    @NotEmpty
    private final long id;
    @NotEmpty
    private final String name;
    @NotEmpty
    private final String username;

    @NotEmpty
    private final String role;

    public RegisteredUserDto(RegisteredUser user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.role = user.getRole().name();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

}
