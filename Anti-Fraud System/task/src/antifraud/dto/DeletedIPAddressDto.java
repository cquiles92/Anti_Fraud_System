package antifraud.dto;

import antifraud.entity.IPAddress;

public class DeletedIPAddressDto {
    private String status;

    public DeletedIPAddressDto(IPAddress ipAddress) {
        status = String.format("IP %s successfully removed!", ipAddress.getIp());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
