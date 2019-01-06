package ru.alterland.controllers.fragments;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import ru.alterland.controllers.MainWrapper;
import ru.alterland.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class Header implements Initializable {

    @FXML
    private MaterialDesignIconView minimize_icon;

    private MainWrapper mainWrapper;

    private boolean minimizeActive = false;

    public Header(MainWrapper mainWrapper){
        this.mainWrapper = mainWrapper;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void navigate_button_entered(MouseEvent mouseEvent) {
        if (!minimizeActive && mouseEvent.getSource() == minimize_icon) return;
        FadeTransition transition = new FadeTransition(Duration.millis(150), (Node) mouseEvent.getSource());
        transition.setToValue(1.0);
        transition.play();
    }

    public void navigate_button_exited(MouseEvent mouseEvent) {
        if (!minimizeActive && mouseEvent.getSource() == minimize_icon) return;
        FadeTransition transition = new FadeTransition(Duration.millis(150), (Node) mouseEvent.getSource());
        transition.setToValue(0.6);
        transition.play();
    }

    public void close_application(MouseEvent mouseEvent) {
        Main.shutdown();
    }

    public void minimize_application(MouseEvent mouseEvent) {
        Main.getMainStage().setIconified(true);
    }


    public void showMinimizeIcon(){
        minimizeActive = true;
        FadeTransition transition = new FadeTransition(Duration.millis(150), minimize_icon);
        transition.setToValue(0.6);
        transition.play();
    }

}
