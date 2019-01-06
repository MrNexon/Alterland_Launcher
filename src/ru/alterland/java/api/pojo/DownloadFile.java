package ru.alterland.java.api.pojo;

import ru.alterland.java.launcher.FilesManager;

public class DownloadFile {
    private String serverPath;
    private String localPath;
    private String hash;

    public String getServerPath() {
        return serverPath;
    }

    public String getLocalPath() {
        return localPath.replace('/', FilesManager.fileSeparator.toCharArray()[0]).replace('\\', FilesManager.fileSeparator.toCharArray()[0]);
    }

    public String getHash() {
        return hash;
    }
}
