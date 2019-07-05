package ru.alterland.java.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.alterland.java.ServerData;
import ru.alterland.java.UserData;
import ru.alterland.java.api.exceptions.ApiExceptions;
import ru.alterland.java.launcher.Connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Servers {
    private static Logger log = Logger.getLogger(Servers.class.getName());

    public static List<ServerData> getServerList(UserData userData) throws ApiExceptions {
        String response;
        try {
            response = Connection.getResponse("servers.getServerList", null, userData);
        } catch (IOException e) {
            throw new ApiExceptions(e.getMessage(), ApiExceptions.Type.ConnectionError);
        }
        JsonObject obj = new Gson().fromJson(response, JsonObject.class);
        if (obj.get("status").getAsInt() != 1){
            JsonObject exJson = obj.getAsJsonObject("exception");
            throw new ApiExceptions(exJson.get("exception_msg").getAsString(), ApiExceptions.Type.values()[exJson.get("exception_code").getAsInt() - 1]);
        }
        //JsonObject obh = obj.get("response").getAsJsonObject();
        //System.out.println("TEST: " + obh);
        JsonArray arr = obj.get("response").getAsJsonArray();
        List<ServerData> serversData = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++){
            JsonObject server = new Gson().fromJson(arr.get(i), JsonObject.class);
            ServerData serverData;
            serverData = new ServerData(Connection.server + server.get("cover_src").getAsString(), server.get("brand_color").getAsString(), server.get("server_name").getAsString(), server.get("title").getAsString(), server.get("description").getAsString(), ServerData.ArticleStatus.values()[server.get("article_status").getAsInt()], ServerData.ServerStatus.values()[server.get("status").getAsInt()], server.get("attributes").toString());
            serversData.add(serverData);
            log.info("Load server complete: " + server.get("server_name").getAsString());
        }
        return serversData;
    }

    public static Boolean getNewServersList(UserData userData, List<String> servers) throws ApiExceptions {
        String response;
        try {
            response = Connection.getResponse("servers.getNewServers", "servers=" + new Gson().toJson(servers), userData);
        } catch (IOException e) {
            throw new ApiExceptions(e.getMessage(), ApiExceptions.Type.ConnectionError);
        }
        JsonObject obj = new Gson().fromJson(response, JsonObject.class);
        if (obj.get("status").getAsInt() != 1){
            JsonObject exJson = obj.getAsJsonObject("exception");
            throw new ApiExceptions(exJson.get("exception_msg").getAsString(), ApiExceptions.Type.values()[exJson.get("exception_code").getAsInt() - 1]);
        }
        JsonObject responseObj = obj.get("response").getAsJsonObject();

        return responseObj.get("diff").getAsBoolean();
    }
}
