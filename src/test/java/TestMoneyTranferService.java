import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;
import utils.SparkTestUtil;

public class TestMoneyTranferService {
    private static final Logger logger = LoggerFactory.getLogger(TestMoneyTranferService.class);
    private static final String VALID_TRANSFER = "{\"sourceAccountId\": \"1\",\"targetAccountId\": \"2\",\"amount\": \"25.12\"}";

    private static SparkTestUtil testUtil;

    @BeforeClass
    public static void beforeClass() {
        logger.info("Starting Spark server for tests");
        testUtil = new SparkTestUtil(4567);
        MoneyTransferApplication.initialize();
    }

    @AfterClass
    public static void afterClass() {
        logger.info("Shutting down Spark...");
        Spark.stop();
    }

    @Test
    public void getExistingAccountTest() throws Exception {
        SparkTestUtil.UrlResponse response = testUtil.doMethod("GET", "/accounts/2", null);
        Assert.assertEquals(200, response.status);
        Assert.assertTrue(response.body.contains("SUCCESS"));
    }

    @Test
    public void transferTest() throws Exception {
        SparkTestUtil.UrlResponse response = testUtil.doMethod("POST", "/transfer", VALID_TRANSFER);
        Assert.assertEquals(200, response.status);
        Assert.assertTrue(response.body.contains("SUCCESS"));
    }


//    @Test
//    public void getNonExistingAccountTest() {
//        MappedResponse res = RequestBuilder.build("GET", "/accounts/-1");
//        Map<String, String> json = res.json();
//        assertEquals(404, res.getStatus());
//        assertEquals("ERROR", json.get("status"));
//    }

}
