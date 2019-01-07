package ru.alterland.java.launcher;

import ru.alterland.java.UserData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class Connection {
    public static String apiServer = "http://launcher.alterland.ru/methods/";
    public static String server = "http://launcher.alterland.ru/";
    private static String ver = "0.1";

    private static Logger log = Logger.getLogger(Connection.class.getName());

    public static String getResponse(String method, String query) throws IOException {
        return sendResponse(method, query, null);
    }

    public static String getResponse(String method, String query, UserData userData) throws IOException {
        return sendResponse(method, query, userData.getAccessToken());
    }

    private static String sendResponse(String method, String query, String accessToken) throws IOException {
        URL obj = new URL(apiServer + method + "?" + query + "&accessToken=" + accessToken + "&v=" + ver);
        log.info("Send response: " + obj.toString());
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String out;
        StringBuffer response = new StringBuffer();
        while ((out = in.readLine()) != null){
            response.append(out);
        }
        in.close();
        return response.toString();
    }
}
