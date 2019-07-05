package ru.alterland.java.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ru.alterland.java.api.exceptions.ApiExceptions;
import ru.alterland.java.api.exceptions.AuthException;
import ru.alterland.java.launcher.Connection;

import java.io.IOException;

public class Versions {
    public static Boolean checkVersion() throws ApiExceptions {
        String query = "repos_id=launcher" + "&prog_ver=0.0.0";
        String response;
        try {
            response = Connection.getResponse("versions.checkVersion", query);
        } catch (IOException e) {
            throw new ApiExceptions(e.getMessage(), ApiExceptions.Type.ConnectionError);
        }
        JsonObject obj = new Gson().fromJson(response, JsonObject.class);
        if (obj.get("status").getAsInt() != 1){
            JsonObject exJson = obj.getAsJsonObject("exception");
            throw new AuthException(exJson.get("exception_msg").getAsString(), AuthException.Type.values()[exJson.get("exception_code").getAsInt() - 1]);
        }
        JsonObject responseJson = obj.getAsJsonObject("response");
        return responseJson.get("ver_status").getAsString().equals("true");
    }
}
