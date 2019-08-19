package repository;

import entities.Account;

import java.math.BigDecimal;

public interface AccountRepository {
    void addAccount(final int id, final BigDecimal amount);
    Account getAccount(final int id);
}
