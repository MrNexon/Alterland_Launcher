package ru.alterland.main;

import animatefx.animation.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static Stage MainStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../gui/fxml/MainWrapper.fxml"));
        primaryStage.setTitle("AlterLand | Игровые сервера");
        Scene scene = new Scene(root, 1000, 600);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setIconified(false);
        primaryStage.show();
        MainStage = primaryStage;
        new FadeIn(root).setSpeed(1.5).play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
