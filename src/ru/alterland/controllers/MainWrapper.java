package ru.alterland.controllers;

import animatefx.animation.AnimationFX;
import animatefx.animation.SlideInRight;
import animatefx.animation.SlideInUp;
import animatefx.animation.SlideOutLeft;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import ru.alterland.controllers.fragments.Header;
import ru.alterland.java.values.Fragments;
import ru.alterland.java.values.Pages;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWrapper implements Initializable {
    @FXML
    public StackPane main_container;

    @FXML
    private BorderPane layout_wrapper;

    @FXML
    private Pane alert_pane;

    @FXML
    private StackPane header_wrapper;

    @FXML
    private StackPane toolbar_wrapper;

    @FXML
    public StackPane wrapper_first;

    @FXML
    public StackPane wrapper_second;

    private Header header_controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Fragments fragments = new Fragments(this);
            header_wrapper.getChildren().setAll(fragments.loadHeader());
            header_controller = fragments.getHeaderController();
            wrapper_first.getChildren().setAll(new Pages(this).loadAuth());
            /*new SlideInUp(toolbar_wrapper).setSpeed(2.5).play();
            toolbar_wrapper.getChildren().setAll(new fragments(this).loadToolBar("Xonis1"));*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainScreen() {
        header_controller.showMinimizeIcon();
        AnimationFX animation = new SlideOutLeft(wrapper_first);
        animation.setSpeed(1.8);
        animation.setResetOnFinished(true);
        animation.getTimeline().setOnFinished(e -> {
            try {
                wrapper_first.getChildren().clear();
                toolbar_wrapper.getChildren().setAll(new Fragments(this).loadToolBar("Xonis1"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            new SlideInUp(toolbar_wrapper).setSpeed(2.5).play();
        });
        animation.play();
        wrapper_first.getChildren().removeAll();
        new SlideInRight(wrapper_second).setSpeed(1.8).play();
        wrapper_second.setDisable(false);
        try {
            wrapper_second.getChildren().setAll(new Pages(this).loadMainServers());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void blurScene() {
        GaussianBlur gaussianBlur = new GaussianBlur(0);
        layout_wrapper.setEffect(gaussianBlur);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(150), alert_pane);
        fadeTransition.setToValue(0.5);
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(gaussianBlur.radiusProperty(), 10);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(150), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        alert_pane.setDisable(false);
        fadeTransition.play();
        timeline.play();
    }
}
