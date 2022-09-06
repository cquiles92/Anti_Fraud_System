package antifraud.entity;

import org.hibernate.validator.constraints.CreditCardNumber;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class CardDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @CreditCardNumber
    @Column(name = "cardNumber")
    private String number;

    @Column(columnDefinition = "integer default 200")
    private long allowedLimit;

    @Column(columnDefinition = "integer default 1500")
    private long manualLimit;

    public CardDetails() {
    }

    public CardDetails(String number) {
        this.number = number;
        this.allowedLimit = 200;
        this.manualLimit = 1500;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getAllowedLimit() {
        return allowedLimit;
    }

    public void setAllowedLimit(long allowedLimit) {
        this.allowedLimit = allowedLimit;
    }

    public long getManualLimit() {
        return manualLimit;
    }

    public void setManualLimit(long manualLimit) {
        this.manualLimit = manualLimit;
    }
}


