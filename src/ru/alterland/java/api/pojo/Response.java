package ru.alterland.java.api.pojo;

import com.google.gson.JsonObject;

public class Response {
    private JsonObject response;
    private int status;

    public JsonObject getResponse() {
        return response;
    }

    public int getStatus() {
        return status;
    }
}
