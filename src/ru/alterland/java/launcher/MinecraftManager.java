package ru.alterland.java.launcher;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import ru.alterland.Main;
import ru.alterland.controllers.MainWrapper;
import ru.alterland.controllers.fragments.ServerCard;
import ru.alterland.java.ServerData;
import ru.alterland.java.api.Client;
import ru.alterland.java.api.Natives;
import ru.alterland.java.api.exceptions.ApiExceptions;
import ru.alterland.java.api.pojo.DownloadFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private Integer filesDownloaded = 1;
    private Integer filesCount = 0;
    private Boolean[] downloadersState = new Boolean[loadersCount];
    private Boolean stopLoaders = false;
    private Boolean stopMinecraft = false;
    private List<Loader> loaders = new ArrayList<>();
    private List<ExecutorService> executorServices = new ArrayList<>();
    private MainWrapper mainWrapper;
    private ServerData serverData;
    private List<ServerCard> serverCards;
    private String clientPath;
    private String jrePath;
    private String action = "Проверка";
    private Runnable runnable;
    private Runnable minecraftExitAction;

    public MinecraftManager(ServerData serverData, List<ServerCard> serverCards, MainWrapper mainWrapper){
        this.serverData = serverData;
        this.serverCards = serverCards;
        this.mainWrapper = mainWrapper;
        clientPath = FilesManager.userFolderPath + FilesManager.clientsFolder + serverData.getName() + FilesManager.fileSeparator;
        jrePath = FilesManager.userFolderPath + FilesManager.jreFolder;
        for (int i = 0; i < loadersCount; i++){
            downloadersState[i] = false;
        }
    }

    public void startClient(Runnable runnable){
        this.runnable = runnable;
        mainWrapper.showProgressBar();
        mainWrapper.setProgressLabelText("Инициализация...");
        mainWrapper.setLaunchActionState(true);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                FilesManager.checkClientFolder(serverData);
                List<DownloadFile> jreFiles = Natives.getLibFiles(Natives.Lib.jre);
                Platform.runLater(() -> mainWrapper.setProgressLabelText("Проверка виртуальной машины..."));
                checkFiles(jreFiles, jrePath, () -> {
                    ExecutorService executorService1 = Executors.newSingleThreadExecutor();
                    executorService1.submit(() -> {
                        Platform.runLater(() -> mainWrapper.setProgressBarValue(ProgressBar.INDETERMINATE_PROGRESS));
                        Platform.runLater(() -> mainWrapper.setProgressLabelText("Загрузка списка файлов клиента..."));
                        ExecutorService executorService2 = Executors.newSingleThreadExecutor();
                        executorService2.submit(() -> {
                            try {
                                List<DownloadFile> downloadFiles = Client.getClientFiles(serverData, Main.getUserData());
                                checkFiles(downloadFiles, clientPath, this::launchMinecraft);
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
                    });
                });
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
        Double progress = (filesDownloaded * 0.5) / averageFileCount;
        mainWrapper.setProgressLabelText(action + " файлов: " + filesDownloaded + " из " + filesCount);
        progress = (progress > 1) ? 1 : progress;
        mainWrapper.setProgressBarValue(progress);
    }

    private void fileState(Boolean state) {
        action = state ? "Проверка" : "Загрузка";
        mainWrapper.setProgressLabelText(action + " файлов: " + filesDownloaded + " из " + filesCount);
    }

    private void checkLoadersStatus(Integer id, Runnable runnable) {
        downloadersState[id] = true;
        Integer stopCounts = 0;
        for (Boolean aBoolean : downloadersState) {
            if (aBoolean) stopCounts += 1;
        }

        if (stopCounts.equals(loadersCount)) {
            System.out.println(filesDownloaded + " : " + filesCount);
            if (filesDownloaded.intValue() == filesCount.intValue()) {
                checkFilesComplete();
                if (runnable != null) runnable.run();
            } else {
                Main.fatalError("Не удалось загрузить файлы, перезапустите лаунчер или повторите попытку позже", mainWrapper, new ApiExceptions("Files download error", ApiExceptions.Type.UnknownError));
            }
        }
    }


    private void checkFilesComplete() {
        System.out.println("COMPLETE");
    }

    private void checkFiles(List<DownloadFile> downloadFiles, String path, Runnable runnableTask){
        filesCount = 0;
        averageFileCount = 0;
        filesDownloaded = 0;
        for (int i = 0; i < loadersCount; i++){
            downloadersState[i] = false;
        }
        loaders.clear();
        filesCount = downloadFiles.size();
        averageFileCount = downloadFiles.size() / 2;
        List<List<DownloadFile>> parts = chopped(downloadFiles, loadersCount);
        for (int i = 0; i < loadersCount; i++){
            loaders.add(new Loader(i, parts.get(i)));
        }
        loaders.forEach(loader -> {
            Runnable runnable = () -> {
                loader.getDownloadFiles().forEach(downloadFile -> {
                    if (stopLoaders) return;
                    try {
                        Boolean check = loader.checkFile(path + downloadFile.getLocalPath(), downloadFile.getHash());
                        Platform.runLater(() -> fileState(check));
                        if (!check) loader.loadFile(downloadFile, path);
                        Platform.runLater(() -> fileDownloadComplete(downloadFile));
                        /*Boolean state = loader.loadFile(downloadFile, path);
                        Platform.runLater(() -> fileDownloadComplete(downloadFile));*/
                    } catch (Exception e) {
                        e.printStackTrace();
                        stopLoaders = true;
                        Main.fatalError("Не удалось загрузить файлы, перезапустите лаунчер или повторите попытку позже. " + downloadFile.getLocalPath(), mainWrapper, new ApiExceptions("Files download error", ApiExceptions.Type.UnknownError));
                    }
                });
                if (!stopLoaders) Platform.runLater(() -> checkLoadersStatus(loader.getId(), runnableTask));
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

    private static List<Path> getFoldersList(String path) {
        List<Path> paths = null;
        try {
            paths = Files.walk(Paths.get(path))
                    .filter(Files::isDirectory)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paths;
    }

    private String connectLibs(){
        final String[] libs = {""};
        getFilesList(FilesManager.userFolderPath + FilesManager.clientsFolder + serverData.getName() + FilesManager.fileSeparator + FilesManager.librariesFolder).forEach(file -> {
            libs[0] += file + ";";
        });
        return libs[0];
    }

    private String findNatives() {
        final String[] nativesPath = new String[1];
        getFoldersList(FilesManager.userFolderPath + FilesManager.clientsFolder + serverData.getName() + FilesManager.fileSeparator).forEach(folder -> {
            if (folder.toString().contains("natives")) {
                nativesPath[0] = folder.toString();
                return;
            }
        });
        return nativesPath[0];
    }

    private String findClient() {
        final String[] path = new String[1];
        getFilesList(FilesManager.userFolderPath + FilesManager.clientsFolder + serverData.getName() + FilesManager.fileSeparator + FilesManager.versionFolder).forEach(file -> {
            if (file.toString().endsWith(".jar")) {
                path[0] = file.toString();
                return;
            }
        });
        return path[0];
    }

    private void launchMinecraft(){
        Platform.runLater(() -> mainWrapper.setProgressBarValue(ProgressBar.INDETERMINATE_PROGRESS));
        Platform.runLater(() -> mainWrapper.setProgressLabelText("Запуск клиента..."));
        System.out.println(runnable);
        Platform.runLater(runnable);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            String nativesFolder = "-Djava.library.path=\"" + findNatives() + "\"";
            String cp = " -cp \"" + connectLibs() + findClient() + "\" ";
            String serverArgs = serverData.getArguments().replace("\"", "");
            String userDataArgs = " --username " + Main.getUserData().getNickname();
            userDataArgs += " --version Forge 1.12.2";
            userDataArgs += " --gameDir " + FilesManager.userFolderPath + FilesManager.clientsFolder + serverData.getName();
            userDataArgs += " --assetsDir " + FilesManager.userFolderPath + FilesManager.clientsFolder + serverData.getName() + FilesManager.fileSeparator + FilesManager.assetsFolder;
            userDataArgs += " --assetIndex 1.12";
            userDataArgs += " --uuid " + Main.getUserData().getUuid();
            userDataArgs += " --accessToken " + Main.getUserData().getAccessToken();
            userDataArgs += " --userType mojang";
            userDataArgs += " --versionType Forge";
            String command = FilesManager.userFolderPath + FilesManager.jreBinFolder + "java.exe" + " " + nativesFolder + cp + serverArgs + userDataArgs;
            System.out.println(command);
            try {
                Process process = Runtime.getRuntime().exec(command);
                Platform.runLater(() -> mainWrapper.hideProgressBar());
                Platform.runLater(Main::hide);
                InputStream stream = process.getErrorStream();
                InputStream stdout = process.getInputStream();
                InputStreamReader errors = new InputStreamReader(stream);
                InputStreamReader isrStdout = new InputStreamReader(stdout);
                BufferedReader brStdout = new BufferedReader(isrStdout);
                BufferedReader errorBuffer = new BufferedReader(errors);
                String line = null;
                while((line = brStdout.readLine()) != null || (line = errorBuffer.readLine()) != null) {
                    System.out.println(line);
                    if (stopMinecraft) process.destroy();
                }
                int exitVal = process.waitFor();
                Platform.runLater(Main::show);
                if (exitVal != 0 && stopMinecraft) System.out.println("Process destroyed");
                else if (exitVal != 0) {
                    Main.fatalError(mainWrapper, new Exception("Minecraft crash"));
                    return;
                } else minecraftExitEvent();
            } catch (IOException | InterruptedException e) {
                Main.fatalError(mainWrapper, e);
            }
        });
    }

    public void cancelLaunching(){
        stopLoaders = true;
        stopMinecraft = true;
        mainWrapper.setLaunchActionState(false);
        executorServices.forEach(executorService -> executorService.shutdown());
        System.out.println("LOADERS STOP!");
        mainWrapper.hideProgressBar();
    }

    public void setMinecraftExitAction(Runnable runnable) {
        minecraftExitAction = runnable;
    }

    public void minecraftExitEvent() {
        mainWrapper.setLaunchActionState(false);
        System.out.println("Minecraft has been close");
        if (minecraftExitAction != null) Platform.runLater(minecraftExitAction);
    }
}
