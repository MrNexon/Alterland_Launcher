package ru.alterland.java.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.codec.digest.DigestUtils;
import ru.alterland.java.UserData;
import ru.alterland.java.api.exceptions.ApiExceptions;
import ru.alterland.java.api.exceptions.AuthException;
import ru.alterland.java.launcher.Connection;

import java.io.IOException;

public class Auth {

    public static UserData loginUser(String login, String pass) throws ApiExceptions {
        String query = "login=" + login + "&password=" + Auth.MD5(pass);
        String response;
        try {
            response = Connection.getResponse("auth.loginUser", query);
        } catch (IOException e) {
            throw new ApiExceptions(e.getMessage(), ApiExceptions.Type.ConnectionError);
        }
        JsonObject obj = new Gson().fromJson(response, JsonObject.class);
        if (obj.get("status").getAsInt() != 1){
            JsonObject exJson = obj.getAsJsonObject("exception");
            throw new AuthException(exJson.get("exception_msg").getAsString(), AuthException.Type.values()[exJson.get("exception_code").getAsInt() - 1]);
        }
        JsonObject responseJson = obj.getAsJsonObject("response");
        //System.out.println(responseJson);
        UserData userData = new UserData(responseJson.get("login").getAsString(), responseJson.get("access_token").getAsString(), UserData.Role.values()[responseJson.get("access_level").getAsInt()], responseJson.get("last_login_timestamp").getAsString(), responseJson.get("user_uuid").getAsString(), Boolean.valueOf(responseJson.get("early_access").getAsString()));
        return userData;
    }

    /*public void loginUser() throws AuthException {
        String query = "login=" + login + "&password=" + MD5(pass);
        String response = Connection.getResponse("auth.loginUser", query);
        JsonObject obj = new Gson().fromJson(response, JsonObject.class);
        if (obj.get("status").getAsInt() != 1){
            JsonObject exJson = obj.getAsJsonObject("exception");
            throw new AuthException(exJson.get("exception_msg").getAsString(), AuthException.Type.values()[exJson.get("exception_code").getAsInt() - 1]);
        }
        JsonObject responseJson = obj.getAsJsonObject("response");
        UserData userData = new UserData(responseJson.get("login").getAsString(), responseJson.get("access_token").getAsString(), UserData.Role.valueOf(responseJson.get("role").getAsString()), responseJson.get("last_login_timestamp").getAsString(), responseJson.get("user_uuid").getAsString(), Boolean.valueOf(responseJson.get("early_access").getAsString()));
        System.out.println(userData.getRole());
    }*/

    private static String MD5(String text) {
        return DigestUtils.md5Hex(text);
    }
}