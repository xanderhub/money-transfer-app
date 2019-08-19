import controller.TransferController;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AccountService;

import java.math.BigDecimal;

public class MoneyTransferApplication {

    private static final Logger logger = LoggerFactory.getLogger(MoneyTransferApplication.class);

    protected static void initialize(){
        Weld weld = new Weld();
        WeldContainer container = weld.initialize();

        logger.info("Initializing TransferController...");
        container.select(TransferController.class).get();

        logger.info("Populating accounts data...");
        initializeAccounts(container);
    }

    private static void initializeAccounts(WeldContainer container){
        AccountService accountService = container.select(AccountService.class).get();

        accountService.createAccount(1, new BigDecimal("100.00"));
        accountService.createAccount(2, new BigDecimal("200.00"));
        accountService.createAccount(3, new BigDecimal("120.00"));
        accountService.createAccount(4, new BigDecimal("230.00"));
        accountService.createAccount(5, new BigDecimal("425.00"));
    }

    public static void main(String[] args) {
        MoneyTransferApplication.initialize();
    }
}
