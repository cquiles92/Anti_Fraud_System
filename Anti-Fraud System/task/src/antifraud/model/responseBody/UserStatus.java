package antifraud.model.responseBody;

import antifraud.entity.RegisteredUser;
public class UserStatus {
    private String status;

    public UserStatus(RegisteredUser user) {
        String lock = user.isAccountNonLocked() ? "unlocked" : "locked";
        this.status = String.format("User %s %s!", user.getUsername(), lock);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
