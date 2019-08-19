import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;
import utils.MappedResponse;
import utils.RequestBuilder;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestMoneyTranferService {

    @BeforeClass
    public static void beforeClass() {
        MoneyTransferApplication.initialize();
    }

    @AfterClass
    public static void afterClass() {
        Spark.stop();
    }

    @Test
    public void getExistingAccountTest() {
        MappedResponse res = RequestBuilder.build("GET", "/accounts/2");
        Map<String, String> json = res.json();
        assertEquals(200, res.getStatus());
        assertEquals("SUCCESS", json.get("status"));
    }


//    @Test
//    public void getNonExistingAccountTest() {
//        MappedResponse res = RequestBuilder.build("GET", "/accounts/-1");
//        Map<String, String> json = res.json();
//        assertEquals(404, res.getStatus());
//        assertEquals("ERROR", json.get("status"));
//    }

}
