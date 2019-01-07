package ru.alterland.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import ru.alterland.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadingScreen implements Initializable {

    @FXML
    private Label loading_shadow, loading_title;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*FilesManager.checkJRE(() -> {
            loading_shadow.setText("Загрузка библиотек\n" + FilesManager.filesComplete + " из " + FilesManager.filesCount);
            loading_title.setText("Загрузка библиотек\n" + FilesManager.filesComplete + " из " + FilesManager.filesCount);
        });*/
    }

    public void exit(MouseEvent mouseEvent) {
        Main.shutdown();
    }
}
