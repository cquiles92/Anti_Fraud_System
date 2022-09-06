package antifraud.entity;

import antifraud.model.enumerator.Code;
import antifraud.model.requestBody.TransactionRequest;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
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

    private String result;

    private String feedback;

    public Transaction() {
    }

    public Transaction(TransactionRequest request) {
        this.amount = request.getAmount();
        this.ip = request.getIp();
        this.number = request.getNumber();
        this.region = request.getRegion();
        this.date = request.getDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getResult() {
        return result == null ? "" : result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getFeedback() {
        return feedback == null ? "" : feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}


