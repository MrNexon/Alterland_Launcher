package ru.alterland.java.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.alterland.java.api.exceptions.ApiExceptions;
import ru.alterland.java.api.pojo.DownloadFile;
import ru.alterland.java.launcher.Connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Natives {
    private static Logger log = Logger.getLogger(Natives.class.getName());

    public enum Lib {
        jre, minecraft
    }
    public static List<DownloadFile> getLibFiles(Lib lib) throws ApiExceptions {
        String os;
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) return null;
        else os = "windows";
        String query = "libId=" + lib.toString() + "&os=" + os;
        String response;
        try {
            response = Connection.getResponse("natives.getLibFiles", query);
        } catch (IOException e) {
            throw new ApiExceptions(e.getMessage(), ApiExceptions.Type.ConnectionError);
        }
        JsonObject obj = new Gson().fromJson(response, JsonObject.class);
        if (obj.get("status").getAsInt() != 1){
            JsonObject exJson = obj.getAsJsonObject("exception");
            throw new ApiExceptions(exJson.get("exception_msg").getAsString(), ApiExceptions.Type.values()[exJson.get("exception_code").getAsInt() - 1]);
        }
        JsonArray arr = obj.get("response").getAsJsonArray();
        List<DownloadFile> downloadFiles = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++){
            DownloadFile downloadFile = new Gson().fromJson(arr.get(i), DownloadFile.class);
            downloadFiles.add(downloadFile);
        }
        log.info("Files list downloaded");
        return downloadFiles;
    }
}
