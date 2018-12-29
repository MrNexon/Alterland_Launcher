package ru.alterland;

import animatefx.animation.FadeIn;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.alterland.controllers.MainWrapper;
import ru.alterland.java.UserData;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Logger;

public class Main extends Application {

    public static Stage MainStage;
    public static UserData userData;
    private static Logger log = Logger.getLogger(Main.class.getName());

    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("Start render scene");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gui/fxml/MainWrapper.fxml"));
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
        MainStage = primaryStage;
        new FadeIn(root).setSpeed(1.2).play();
        log.info("Showing stage");
    }

    public void init(){
        log.info("Launcher Init");
    }

    public static void main(String[] args) {
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
        }

        try {
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(new FileReader("C:/Users/Belyash/Desktop/1.7.10.json"));
            JsonObject obj = element.getAsJsonObject();
            JsonObject objects = obj.getAsJsonObject("objects");
            Set<Map.Entry<String, JsonElement>> entries = objects.entrySet();
            for (Map.Entry<String, JsonElement> entry: entries) {
                JsonObject fi = entry.getValue().getAsJsonObject();
                fi.get
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


    private static void downloadUsingNIO(String urlStr, String file, Long size) throws IOException {
        URL url = new URL(urlStr);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, size);
        fos.close();
        rbc.close();
    }


}
