package service;

import entities.Account;
import entities.StandardResponse;
import entities.StatusResponse;
import entities.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
public class MultiThreadedTranferService implements TransferService{
    private static final Logger logger = LoggerFactory.getLogger(MultiThreadedTranferService.class);
    private final ExecutorService executorService;
    private final AccountService accountService;

    @Override
    public StandardResponse transfer(final int fromId, final int toId, final BigDecimal amount) {
        final Account fromAccount = accountService.getAccount(fromId);
        final Account toAccount = accountService.getAccount(toId);

        /*---- validation ---*/
        if(fromAccount == null)
            return new StandardResponse(StatusResponse.ERROR, "Source account doesn't exist");
        if(toAccount == null)
            return new StandardResponse(StatusResponse.ERROR, "Target account doesn't exist");
        if(toAccount.equals(fromAccount))
            return new StandardResponse(StatusResponse.ERROR, "Source & target accounts are equal");
        if(amount.scale() > 2 || amount.compareTo(BigDecimal.ZERO) <= 0)
            return new StandardResponse(StatusResponse.ERROR, "Transfer amount is invalid");

        /*---- begin tranfer ---*/
        try {
            return executorService.submit(
                    new Transaction(System.currentTimeMillis()
                            ,fromAccount
                            ,toAccount
                            ,amount))
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Server error - failed to retrieve transaction status - {}", e.getMessage());
            return new StandardResponse(StatusResponse.ERROR,
                    "Server error - failed to retrieve transaction status");
        }
    }

    @Inject
    public MultiThreadedTranferService(AccountService accountService){
        this.executorService = Executors.newCachedThreadPool();
        this.accountService = accountService;
    }
}
