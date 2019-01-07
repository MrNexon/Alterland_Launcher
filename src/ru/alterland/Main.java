package ru.alterland;

import animatefx.animation.FadeIn;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.alterland.controllers.MainWrapper;
import ru.alterland.controllers.popups.DialogWindow;
import ru.alterland.java.UserData;
import ru.alterland.java.api.exceptions.ApiExceptions;
import ru.alterland.java.launcher.FilesManager;
import ru.alterland.java.values.Popups;

import java.util.Timer;
import java.util.logging.Logger;

public class Main extends Application {

    private static Stage MainStage;
    private static UserData userData;
    private static Timer updateServersTimer = new Timer();
    private static Logger log = Logger.getLogger(Main.class.getName());
    private static Boolean error = false;

    public static Stage getMainStage() {
        return MainStage;
    }

    public static void setMainStage(Stage mainStage) {
        MainStage = mainStage;
    }

    public static UserData getUserData() {
        return userData;
    }

    public static void setUserData(UserData userData) {
        Main.userData = userData;
    }

    public static Timer getUpdateServersTimer(){
        return updateServersTimer;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gui/fxml/MainWrapper.fxml"));
        log.info("Start render scene");
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("AlterLand | Игровые сервера");
        Scene scene = new Scene(root, 1000, 600);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setIconified(false);
        primaryStage.show();
        MainWrapper mainWrapper = fxmlLoader.getController();
        mainWrapper.registerStage(primaryStage);
        setMainStage(primaryStage);
        new FadeIn(root).setSpeed(1.2).play();
        log.info("Showing stage");

    }

    public void init(){
        log.info("Launcher Init");
    }

    public static void main(String[] args) {
        try {
            FilesManager.checkLauncherFolder();
        } catch (ApiExceptions apiExceptions) {
            apiExceptions.printStackTrace();
            shutdown();
        }
        /*System.out.println("Download start");
        Loader.LoadFile("http://api.alterland.ru/mask.png", "H:\\image.png", "0b1b73c0102cc516a418df6dfae5440c");
        System.out.println("Complete");*/
        /*System.out.println(Settings.get("YYY"));
        Settings.add("LOLKEK", "testValue");
        Settings.add("ываыа", "testVцукцукцalue");
        Settings.save();*/
        //System.getProperties().list(System.out);
        log.info("Launcher Starting");
        launch(args);

        /*File folder = new File("K:/RolePlay/libraries/forge");
        String[] files = folder.list((folder1, name) -> name.endsWith(".jar"));

        for ( String fileName : files ) {
            System.out.println("K:/RolePlay/libraries/forge/" + fileName);
        }

        folder = new File("K:/RolePlay/libraries/lwjgl");
        files = folder.list((folder1, name) -> name.endsWith(".jar"));

        for ( String fileName : files ) {
            System.out.println("K:/RolePlay/libraries/lwjgl/" + fileName);
        }

        folder = new File("K:/RolePlay/libraries");
        files = folder.list((folder1, name) -> name.endsWith(".jar"));

        for ( String fileName : files ) {
            System.out.println("K:/RolePlay/libraries/" + fileName);
        }*/

       /* try {
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(new FileReader("C:/Users/Belyash/Desktop/1.7.10.json"));
            JsonObject obj = element.getAsJsonObject();
            JsonObject objects = obj.getAsJsonObject("objects");
            Set<Map.Entry<String, JsonElement>> entries = objects.entrySet();
            for (Map.Entry<String, JsonElement> entry: entries) {
                JsonObject fi = entry.getValue().getAsJsonObject();
                System.out.println(fi);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    public static void fatalError(MainWrapper mainWrapper, Exception e) {
        if (error) return;
        Platform.runLater(() -> {
            error = true;
            updateServersTimer.cancel();
            Popups dialogWindow = new Popups(mainWrapper);
            mainWrapper.getDialog_container().getChildren().setAll(dialogWindow.loadDialogWindow("Возникла непредвиденная ошибка", DialogWindow.DialogButtons.Ok));
            e.printStackTrace();
            dialogWindow.getDialogWindowController().show(Main::shutdown);
        });
    }

    public static void fatalError(String text, MainWrapper mainWrapper, Exception e) {
        if (error) return;
        Platform.runLater(() -> {
            error = true;
            updateServersTimer.cancel();
            Popups dialogWindow = new Popups(mainWrapper);
            mainWrapper.getDialog_container().getChildren().setAll(dialogWindow.loadDialogWindow(text, DialogWindow.DialogButtons.Ok));
            e.printStackTrace();
            dialogWindow.getDialogWindowController().show(Main::shutdown);
        });
    }

    public static void shutdown(){
        System.exit(0);
    }

    public static void hide() {
        MainStage.setIconified(true);
    }

    public static void show() {
        MainStage.setIconified(false);
    }
}
