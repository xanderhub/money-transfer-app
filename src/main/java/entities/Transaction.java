package entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

public final class Transaction implements Callable<StandardResponse> {

    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);

    private final long id;
    private final Account from;
    private final Account to;
    private final BigDecimal amount;

    public Transaction(long id, final Account from, final Account to, BigDecimal amount) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public StandardResponse call() {
        // Acquire the lock of entities.Account 'from'
        synchronized (from) {
            try {
                if (from.getBalance().compareTo(amount) < 0) {
                    logger.info("{} - Insufficient funds on account {}",id, from);
                    return new StandardResponse(
                            StatusResponse.ERROR
                            ,"Insufficient funds on account " + from + ". Transaction aborted");
                }
                from.withdraw(amount);
                logger.info("{} - WITHDRAW from account {} amount of {} => balance: {}", id, from, amount, from.getBalance());
                Thread.sleep(500); //simulate IO delay
            } catch (InterruptedException e) {
                logger.error("{} FAILED - transaction failure - {}", id, e.getMessage());
                return new StandardResponse(StatusResponse.ERROR, "Server error - transaction failed");
            }
        }
        // Release the lock of entities.Account 'from'
        // Acquire the lock of entities.Account 'to'
        synchronized (to) {
            try {
                to.deposit(amount);
                logger.info("{} - DEPOSIT to account {} amount of {} => balance: {}", id, to, amount, to.getBalance());
                Thread.sleep(500);  //simulate IO delay
            } catch (InterruptedException e) {
                logger.error("{} FAILED - transaction failure - {}", id, e.getMessage());
                return new StandardResponse(StatusResponse.ERROR, "Server error - transaction failed");
            }
        }
        // Release the lock of entities.Account 'to'
        return new StandardResponse(StatusResponse.SUCCESS, "Amount of " + amount + " has been transfered. Your balance is: " + from.getBalance());
    }
}