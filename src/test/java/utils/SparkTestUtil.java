package utils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SparkTestUtil {

    private int port;

    private HttpClient httpClient;

    public SparkTestUtil(int port) {
        this.port = port;
        this.httpClient = httpClientBuilder().build();
    }

    private HttpClientBuilder httpClientBuilder() {
        Registry<ConnectionSocketFactory> socketRegistry = RegistryBuilder
                .<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .build();
        BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(socketRegistry);
        return HttpClientBuilder.create().setConnectionManager(connManager);
    }


    public UrlResponse doMethod(String requestMethod, String path, String body) throws IOException {
        HttpUriRequest httpRequest = getHttpRequest(requestMethod, path, body, "application/json");
        HttpResponse httpResponse = httpClient.execute(httpRequest);

        UrlResponse urlResponse = new UrlResponse();
        urlResponse.status = httpResponse.getStatusLine().getStatusCode();
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            urlResponse.body = EntityUtils.toString(entity);
        } else {
            urlResponse.body = "";
        }
        Map<String, String> headers = new HashMap<>();
        Header[] allHeaders = httpResponse.getAllHeaders();
        for (Header header : allHeaders) {
            headers.put(header.getName(), header.getValue());
        }
        urlResponse.headers = headers;
        return urlResponse;
    }

    private HttpUriRequest getHttpRequest(String requestMethod, String path, String body,
                                          String acceptType) {
        try {
            String protocol = "http";
            String uri = protocol + "://localhost:" + port + path;

            if (requestMethod.equals("GET")) {
                HttpGet httpGet = new HttpGet(uri);
                httpGet.setHeader("Accept", acceptType);
                return httpGet;
            }

            if (requestMethod.equals("POST")) {
                HttpPost httpPost = new HttpPost(uri);
                httpPost.setHeader("Accept", acceptType);
                httpPost.setEntity(new StringEntity(body));
                return httpPost;
            }

            throw new IllegalArgumentException("Unknown method " + requestMethod);

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static class UrlResponse {

        public Map<String, String> headers;
        public String body;
        public int status;
    }

}
