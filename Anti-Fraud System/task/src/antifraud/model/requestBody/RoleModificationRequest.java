package antifraud.model.requestBody;

import javax.validation.constraints.NotEmpty;

public class RoleModificationRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    private String role;

    RoleModificationRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
