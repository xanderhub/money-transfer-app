package entities;

import java.math.BigDecimal;

public final class Account {
    private final int id;
    private BigDecimal balance;

    public Account(int id, BigDecimal initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void withdraw(final BigDecimal amount){
        balance = balance.subtract(amount);
    }

    public void deposit(final BigDecimal amount){
        balance = balance.add(amount);
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }


}
