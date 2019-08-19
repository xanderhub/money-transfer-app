package service;

import entities.Account;
import repository.AccountRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;

@Singleton
public class AccountService {

    private final AccountRepository repository;

    @Inject
    public AccountService(final AccountRepository repository) {
        this.repository = repository;
    }

    public void createAccount(final int id, final BigDecimal amount){
        repository.addAccount(id, amount);
    }

    public Account getAccount(final int id){
        return repository.getAccount(id);
    }
}
