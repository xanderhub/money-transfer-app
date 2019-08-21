# Money transfer application
Test assigment for Revolut - money tranfer simulator

## Description
The service simulates bank transactions between accounts. It's built as RESTful web service based on [Spark](http://sparkjava.com/) framework <br />Spark is a micro web framework for Java and it's aimed for simplicity and provides only a minimal set of features <br /> and has an embedded web server. The service allows concurrent transfers/transactions between multiple accounts.<br />For the sake of simplicity tranfer service designed with blocking API i.e each transfer thread is being blocked until transfer is complete. Authentication logic was also omitted to ease on demo.

<br /> 

### Technologies / libraries  used in this project:
* Java 8
* [Spark](http://sparkjava.com/) framework - as mentioned
* [CDI](https://www.baeldung.com/java-ee-cdi) (Contexts and Dependency Injection) - is a standard dependency injection framework
* Junit - for tests.
* Gson - used for converting Java objects into JSON representations and vice versa.
* [Logback](https://logback.qos.ch/) - logging provider.


<br /> 

### API:

Here is an example of HTTP GET requests that you can run:<br /> 

`localhost:4567/accounts/2`     -     retrieves account information by its id<br />

Example of response:
```
{
    "status": "SUCCESS",
    "data": {
        "id": 2,
        "balance": 200.00
    }
}
```

Example of HTTP POST request - money transfer request:<br />

Request url: `localhost:4567/transfer`  <br />
Request body:
```
{
	"sourceAccountId": "1",
	"targetAccountId": "2",
	"amount": "25.12"
}
```

Example of response:
```
{
    "status": "SUCCESS",
    "message": "Amount of 25.12 has been transfered. Your balance is: 74.88"
}
```

Logs example:
```
12:55:29.153 [qtp516495823-24] INFO  controller.TransferController - processing request: localhost:4567/transfer
12:55:29.164 [pool-1-thread-1] INFO  entities.Transaction - 1566208529163 - WITHDRAW from account 1 amount of 25.12 => balance: 74.88
12:55:29.666 [pool-1-thread-1] INFO  entities.Transaction - 1566208529163 - DEPOSIT to account 2 amount of 25.12 => balance: 225.12
```
<br /> 

### Transaction process: <br /> 
The heart of the tranfer service logic is `Transaction` class <br /> 
Its simplified version is as following: <br /> 


```
public final class Transaction implements Callable<StandardResponse> {
    /*
    instance variables and constructors
    */
    
    @Override
    public StandardResponse call() {
        // Acquire the lock of acount 'from'
        synchronized (from) {
            try {
                if (from.getBalance().compareTo(amount) < 0) 
                    return new StandardResponse(StatusResponse.ERROR,"Insufficient funds on account " + from + ". Transaction aborted");
                from.withdraw(amount);
                Thread.sleep(500); //simulate IO delay
            } catch (InterruptedException e) {
                return new StandardResponse(StatusResponse.ERROR, "Server error - transaction failed");
            }
        }
        // Release the lock of 'from'
        // Acquire the lock of account 'to'
        synchronized (to) {
            try {
                to.deposit(amount);
                Thread.sleep(500);  //simulate IO delay
            } catch (InterruptedException e) {
                return new StandardResponse(StatusResponse.ERROR, "Server error - transaction failed");
            }
        }
        // Release the lock of 'to'
        return new StandardResponse(StatusResponse.SUCCESS, 
	"Amount of " + amount + " has been transfered. Your balance is: " + from.getBalance());
    }
}
```

As can be seen above `call()` method (i.e execute transaction) locks source account object first, withdraws funds, <br />unlocks source account, locks destination account as a second step, deposits funds and finally unlocks destination account <br /> 
In this case a thread (or transaction) holds only one lock at a time. This approach prevents from deadlocks that might occur in cases then both accounts transfer funds to each other at the same time. <br />
In order to get transfer status `Transaction` class implements `Callable<>` interface which allows to return the status (as `StandardResponse` object) wrapped in `Future<>` container.<br />
Each transaction is being handled by `MultiThreadedTranferService` which manages a thread pool (`ExecutorService`) of transactions. Here is an example of simplified code of transaction subbmition:

```
executorService.submit(
                    new Transaction(
                             fromAccount
                            ,toAccount
                            ,amount))
                    .get();
```

The `get()` method mentioned in the code above returns `Future<>` object which contains our `StandardResponse`. 
`get()` method is blocking and each thread waits here until transaction comletes.
This explains the blocking API of tranfer service mentioned in the description.
