# Money transfer application
Test assigment for Revolut - money tranfer simulator

## Description
The service simulates money tranfers between accounts. It's built as RESTful web service based on [Spark](http://sparkjava.com/) framework <br />Spark is a micro web framework for Java and it's aimed for simplicity and provides only a minimal set of features <br /> and has an embedded web server. The service allows concurrent transfers/transactions between multiple accounts. For the sake of simplicity tranfer service <br />designed with blocking API i.e each transfer thread is being blocked until transfer is complete. 

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
