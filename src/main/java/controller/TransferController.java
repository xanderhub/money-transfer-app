package controller;

import com.google.gson.Gson;
import entities.Account;
import entities.StandardResponse;
import entities.StatusResponse;
import entities.TranferRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AccountService;
import service.TransferService;

import javax.inject.Inject;

import static spark.Spark.get;
import static spark.Spark.post;

public class TransferController {
    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);

    @Inject
    public TransferController(TransferService transferService, AccountService accountService){

        get("/accounts/:id", (request, response) -> {
            response.type("application/json");
            Account account;
            logger.info("processing request: {}{}",request.host(), request.pathInfo());

            try {
                account = accountService.getAccount(Integer.parseInt(request.params(":id")));
                if(account == null){
                    response.status(404);
                    return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "Account " + request.params(":id") + " doesn't exist"));
                }
            }
            catch (NumberFormatException e) {
                response.status(400);
                logger.error("Invalid request: {} - account id should be numeric value", request.pathInfo());
                return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "Invalid request - account id should be numeric value"));
            }
            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(account)));
        });

        post("/transfer", (request, response) -> {
            response.type("application/json");
            logger.info("processing request: {}{}",request.host(), request.pathInfo());

            TranferRequest req = new Gson().fromJson(request.body(), TranferRequest.class);
            StandardResponse res = transferService.transfer(req.getSourceAccountId(), req.getTargetAccountId(), req.getAmount());
            if(res.getStatus() == StatusResponse.ERROR)
                response.status(400);
            return new Gson().toJson(new Gson().toJsonTree(res));
        });
    }
}
