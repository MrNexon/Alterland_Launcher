package ru.alterland.java.launcher;

import javafx.application.Platform;
import org.apache.commons.codec.digest.DigestUtils;
import ru.alterland.Main;
import ru.alterland.controllers.MainWrapper;
import ru.alterland.controllers.fragments.ServerCard;
import ru.alterland.java.ServerData;
import ru.alterland.java.api.Client;
import ru.alterland.java.api.exceptions.ApiExceptions;
import ru.alterland.java.api.pojo.DownloadFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class MinecraftManager {

    private static Integer loadersCount = 5;
    private Integer averageFileCount = 0;
    private Integer filesDownloaded = 0;
    private Integer filesCount = 0;
    private Integer errorIterations = 0;
    private Boolean[] downloadersState = new Boolean[loadersCount];
    private List<DownloadFile> errorFiles = new ArrayList<>();
    private List<Loader> loaders = new ArrayList<>();
    private List<ExecutorService> executorServices = new ArrayList<>();
    private MainWrapper mainWrapper;
    private ServerData serverData;
    private List<ServerCard> serverCards;
    private String clientPath;

    public MinecraftManager(ServerData serverData, List<ServerCard> serverCards, MainWrapper mainWrapper){
        this.serverData = serverData;
        this.serverCards = serverCards;
        this.mainWrapper = mainWrapper;
        clientPath = FilesManager.userFolderPath + FilesManager.clientsFolder + serverData.getName() + FilesManager.fileSeparator;
        for (int i = 0; i < loadersCount; i++){
            downloadersState[i] = false;
        }
    }

    public void startClient(){
        //connectLibs();
        mainWrapper.showProgressBar();
        mainWrapper.setProgressLabelText("Инициализация...");
        mainWrapper.setStatus(true);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                FilesManager.checkClientFolder(serverData);
                checkClientFiles(Client.getClientFiles(serverData, Main.getUserData()));
            } catch (ApiExceptions apiExceptions) {
                System.out.println(apiExceptions.getType());
                switch (apiExceptions.getType()){
                    case ConnectionError:
                        Main.fatalError("Не удается подключиться, проверьте соединение и перезапустите лаунчер", mainWrapper, apiExceptions);
                        break;
                    case InvalidAccessToken:
                        Main.fatalError("Кто то зашел через ваш аккаунт. Попросите его выйти и перезапустите лаунчер", mainWrapper, apiExceptions);
                        break;
                    default:
                        Main.fatalError(mainWrapper, apiExceptions);
                }
            }
        });
    }

    private void fileDownloadComplete(DownloadFile downloadFile){
        filesDownloaded++;
        mainWrapper.setProgressLabelText("Загрузка файлов: " + filesDownloaded + " из " + filesCount);
        Double progress = (filesDownloaded * 0.5) / averageFileCount;
        mainWrapper.setProgressBarValue(progress);
    }

    private void checkLoadersStatus(Integer id) {
        downloadersState[id] = true;
        Integer stopCounts = 0;
        for (Boolean aBoolean : downloadersState) {
            if (aBoolean) stopCounts += 1;
        }

        if (stopCounts.equals(loadersCount)) {
            System.out.println(filesDownloaded + " : " + filesCount + " : " + errorFiles.size());
            if (filesDownloaded.equals(filesCount)) {
                checkFilesComplete();
            } else {
                errorFiles.forEach(System.out::println);
                /*checkClientFiles(errorFiles);
                if (errorIterations > 2) {
                    Main.fatalError("Не удалось загрузить файлы, перезапустите лаунчер или повторите попытку позже", mainWrapper, new ApiExceptions("Files download error", ApiExceptions.Type.UnknownError));
                }
                errorIterations++;*/
            }
        }
    }

    private void fileDownloadError(DownloadFile downloadFile){
        errorFiles.add(downloadFile);
    }

    private void checkFilesComplete() {
        System.out.println("COMPLETE");
        errorFiles.forEach(System.out::println);
    }

    private void checkClientFiles(List<DownloadFile> downloadFiles){
        filesCount = downloadFiles.size();
        averageFileCount = downloadFiles.size() / 2;
        List<List<DownloadFile>> parts = chopped(downloadFiles, loadersCount);
        for (int i = 0; i < loadersCount; i++){
            loaders.add(new Loader(i, parts.get(i)));
        }
        loaders.forEach(loader -> {
            Runnable runnable = () -> {
                loader.getDownloadFiles().forEach(downloadFile -> {
                    try {
                        loader.loadFile(downloadFile, clientPath);
                        Platform.runLater(() -> fileDownloadComplete(downloadFile));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Platform.runLater(() -> fileDownloadError(downloadFile));
                    }
                });
                checkLoadersStatus(loader.getId());
            };
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(runnable);
            executorServices.add(executorService);
        });
    }

    private <T> List<List<T>> chopped(List<T> list, Integer partsCount) {
        Integer counter = 0;
        List<List<T>> parts = new ArrayList<>(partsCount);
        for (int i = 0; i < partsCount; i++){
            parts.add(new ArrayList<>());
        }
        /*Integer partsSize = list.size() / partsCount;
        System.out.println(list.size() + " / " + partsCount + " - " + partsSize);
        parts.get(0).addAll(list.subList(0, partsSize));
        Integer last = partsSize;
        for (int i = 1; i < partsCount; i++){
            Integer start = last + 1;
            Integer end = last + partsSize > list.size() ? list.size() - 1 : start + partsSize;
            System.out.println(start + " : " + end);
            parts.get(i).addAll(list.subList(start, end));
            last = end;
        }*/
        for (T t : list) {
            parts.get(counter).add(t);
            counter = ((counter + 1) >= loadersCount) ? 0 : counter + 1;
        }
        return parts;
    }

    private static List<Path> getFilesList(String path) {
        List<Path> paths = null;
        try {
            paths = Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paths;
    }

    private void deleteDangerFiles(){

    }

    private String connectLibs(){
        getFilesList(FilesManager.userFolderPath + "/libraries").forEach(file -> {
            System.out.print(file + ";");
        });
        /*try {
            Files.walk(Paths.get())
                    .filter(Files::isRegularFile)
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return null;
    }

    private List<String> fetchChild(File file, List<String> libs){
        if (file.isDirectory()){
            File[] children = file.listFiles();
            for (File child : children) {
                libs.addAll(fetchChild(child, libs));
            }
        } else {
            if (file.getAbsolutePath().endsWith(".jar")){
                libs.add(file.getAbsolutePath());
            }
        }
        return libs;
    }

    private void LaunchMinecraft(){

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
