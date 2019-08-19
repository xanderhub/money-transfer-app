package repository;

import entities.Account;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class InMemoryAccountRepository implements AccountRepository {

    private final Map<Integer, Account> accountMap;

    public InMemoryAccountRepository(){
        accountMap = new HashMap<>();
    }

    public void addAccount(final int id, final BigDecimal amount) {
        accountMap.put(id, new Account(id, amount));
    }

    public Account getAccount(int id) {
        return accountMap.get(id);
    }
}
