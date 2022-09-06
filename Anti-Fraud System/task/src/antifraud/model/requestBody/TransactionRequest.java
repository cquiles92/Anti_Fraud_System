package antifraud.model.requestBody;

import antifraud.model.enumerator.Code;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;


public class TransactionRequest {
    @NotNull(message = "Value cannot be less than or equal to 0")
    private long amount;

    @NotNull
    @Pattern(regexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$",
            message = "must be a valid IP address")
    private String ip;

    @NotNull
    @CreditCardNumber
    private String number;

    @Enumerated(EnumType.STRING)
    private Code region;

    @Column(name = "local_date_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    public TransactionRequest() {
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Code getRegion() {
        return region;
    }

    public void setRegion(Code region) {
        this.region = region;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}


