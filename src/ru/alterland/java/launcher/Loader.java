package ru.alterland.java.launcher;

import org.apache.commons.codec.digest.DigestUtils;
import ru.alterland.java.api.pojo.DownloadFile;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

public class Loader {
    private static Logger log = Logger.getLogger(Loader.class.getName());
    public Integer id;
    public List<DownloadFile> downloadFiles;

    public Loader(Integer id, List<DownloadFile> downloadFiles) {
        this.id = id;
        this.downloadFiles = downloadFiles;
    }

    public Integer getId() {
        return id;
    }

    public List<DownloadFile> getDownloadFiles(){
        return downloadFiles;
    }

    public void loadFile(DownloadFile downloadFile, String userFolderPath) throws Exception {
        if (checkFile(userFolderPath + downloadFile.getLocalPath(), downloadFile.getHash())) return;
        log.info("[LOADER " + id + "] File download start");
        URL link = new URL(Connection.server + downloadFile.getServerPath());
        InputStream in = new BufferedInputStream(link.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n;
        while (-1 != (n = in.read(buf)))
        {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
        byte[] response = out.toByteArray();
        if (downloadFile.getLocalPath().lastIndexOf(FilesManager.fileSeparator) > -1) {
            String dirPath = userFolderPath + downloadFile.getLocalPath().substring(0, downloadFile.getLocalPath().lastIndexOf(FilesManager.fileSeparator));
            File dir = new File(dirPath);
            if (!dir.exists()){
                if (!dir.mkdirs()) throw new Exception("Create dir failed");
            }
        }
        FileOutputStream fos = new FileOutputStream(userFolderPath + downloadFile.getLocalPath());
        fos.write(response);
        fos.close();
    }

    private Boolean checkFile(String path, String hash){
        Boolean check = new File(path).exists() && MD5File(path).equals(hash);
        log.info("[LOADER " + id + "] File equals \"" + new File(path).getName() + "\" : " + check);
        return check;
    }

    private String MD5File(String path) {
        try {
            InputStream inputStream = Files.newInputStream(Paths.get(path));
            return DigestUtils.md5Hex(inputStream);
        } catch (IOException e) {
            return null;
        }
    }
}
