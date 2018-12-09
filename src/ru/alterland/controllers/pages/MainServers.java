package ru.alterland.controllers.pages;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import ru.alterland.controllers.MainWrapper;


import java.net.URL;
import java.util.ResourceBundle;

public class MainServers implements Initializable {

    @FXML
    private Pane pane_1;

    @FXML
    private Pane pane_2;

    @FXML
    private Pane pane_3;

    @FXML
    private StackPane servers_wrapper;

    private MainWrapper mainWrapper;

    public MainServers(MainWrapper mainWrapper){
        this.mainWrapper = mainWrapper;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
