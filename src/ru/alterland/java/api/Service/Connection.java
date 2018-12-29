package ru.alterland.java.api.Service;

import ru.alterland.java.UserData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connection {
    public static String apiServer = "http://api.alterland.ru/methods/";
    public static String server = "http://api.alterland.ru/";
    private static String ver = "0.1";

    public static String getResponse(String method, String query){
        return sendResponce(method, query, null);
    }

    public static String getResponse(String method, String query, UserData userData){
        return sendResponce(method, query, userData.getAccessToken());
    }

    private static String sendResponce(String method, String query, String accessToken){
        try {
            URL obj = new URL(apiServer + method + "?" + query + "&accessToken=" + accessToken + "&v=" + ver);
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
