package entities;

import java.math.BigDecimal;

public final class TranferRequest {

    private int sourceAccountId;
    private int targetAccountId;
    private BigDecimal amount;

    public TranferRequest(int sourceAccountId, int targetAccountId, BigDecimal amount) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
    }

    public int getSourceAccountId() {
        return sourceAccountId;
    }

    public int getTargetAccountId() {
        return targetAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
