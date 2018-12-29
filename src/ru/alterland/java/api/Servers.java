package ru.alterland.java.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.alterland.java.Card;
import ru.alterland.java.UserData;
import ru.alterland.java.api.Exceptions.ApiExceptions;
import ru.alterland.java.api.Service.Connection;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Servers {
    public static List<Card> getServerList(UserData userData) throws ApiExceptions {
        String response = Connection.getResponse("servers.getServerList", null, userData);
        JsonObject obj = new Gson().fromJson(response, JsonObject.class);
        if (obj.get("status").getAsInt() != 1){
            JsonObject exJson = obj.getAsJsonObject("exception");
            throw new ApiExceptions(exJson.get("exception_msg").getAsString(), ApiExceptions.Type.values()[exJson.get("exception_code").getAsInt() - 1]);
        }
        JsonArray arr = obj.get("response").getAsJsonArray();
        List<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < arr.size(); i++){
            JsonObject server = new Gson().fromJson(arr.get(i), JsonObject.class);
            Card card;
            try {
                card = new Card(Connection.server + server.get("cover_src").getAsString(), server.get("server_name").getAsString(), server.get("title").getAsString(), server.get("description").getAsString(), Card.ArticleStatus.Installed, Card.ServerStatus.values()[server.get("status").getAsInt()], InetAddress.getByName(server.get("status").getAsString()));
                cards.add(card);
                System.out.println("Load apiServer complete: " + server.get("server_name").getAsString());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return cards;
    }
}
