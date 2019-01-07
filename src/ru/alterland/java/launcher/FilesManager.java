package ru.alterland.java.launcher;

import ru.alterland.java.ServerData;
import ru.alterland.java.api.exceptions.ApiExceptions;

import java.io.File;

public class FilesManager {
    public static final String fileSeparator = System.getProperty("file.separator");
    public static final String userFolderPath = System.getenv("APPDATA") + "/.alterland/";
    public static final String clientsFolder = "clients/";
    public static final String jreFolder = "jre/";
    public static final String jreBinFolder = "jre/bin/";
    public static final String assetsFolder = "assets/";
    public static final String versionFolder = "versions/";
    public static final String librariesFolder = "libraries/";


    public static void checkLauncherFolder() throws ApiExceptions {
        if (!new File(userFolderPath).exists()) if (!new File(userFolderPath).mkdirs()) throw new ApiExceptions("Folder not create", ApiExceptions.Type.InternalError);
    }

    public static void checkClientFolder(ServerData serverData) throws ApiExceptions {
        if (!new File(userFolderPath + clientsFolder + serverData.getName()).exists()) if (!new File(userFolderPath + clientsFolder + serverData.getName()).mkdirs()) throw new ApiExceptions("Folder not create", ApiExceptions.Type.InternalError);
    }
}
