package antifraud.model.requestBody;

import javax.validation.constraints.NotEmpty;

public class UserLockRequest {
    @NotEmpty
    private String username;

    private String operation;

    public UserLockRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
