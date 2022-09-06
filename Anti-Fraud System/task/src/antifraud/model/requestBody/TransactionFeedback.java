package antifraud.model.requestBody;

import javax.validation.constraints.NotNull;

public class TransactionFeedback {
    @NotNull
    private long transactionId;

    @NotNull
    private String feedback;


    public TransactionFeedback() {
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}


