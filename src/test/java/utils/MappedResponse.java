package utils;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class MappedResponse {

    private final String body;
    private final int status;

    public MappedResponse(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public Map<String,String> json() {
        return new Gson().fromJson(body, HashMap.class);
    }

    public String getBody() {
        return body;
    }

    public int getStatus() {
        return status;
    }
}