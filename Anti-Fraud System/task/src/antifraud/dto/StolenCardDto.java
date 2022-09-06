package antifraud.dto;


import antifraud.entity.StolenCard;

public class StolenCardDto {
    private String status;

    public StolenCardDto(StolenCard stolenCard) {
        status = String.format("Card %s successfully removed!", stolenCard.getNumber());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
