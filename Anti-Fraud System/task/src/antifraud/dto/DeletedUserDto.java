package antifraud.dto;

import java.util.Objects;

public class DeletedUserDto {
    private String username;
    private String status;

    public DeletedUserDto(String username) {
        this.username = username;
        this.status = "Deleted successfully!";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeletedUserDto that = (DeletedUserDto) o;
        return Objects.equals(username, that.username) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, status);
    }

    @Override
    public String toString() {
        return "DeletedUserDto{" +
               "username='" + username + '\'' +
               ", status='" + status + '\'' +
               '}';
    }
}
