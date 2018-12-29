package ru.alterland.controllers;

import animatefx.animation.*;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.alterland.controllers.fragments.Header;
import ru.alterland.java.launcher.EffectUtilities;
import ru.alterland.java.values.Fragments;
import ru.alterland.java.values.Pages;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainWrapper implements Initializable {
    private static Logger log = Logger.getLogger(MainWrapper.class.getName());

    @FXML
    private StackPane main_container;

    @FXML
    private BorderPane layout_wrapper;

    @FXML
    private Pane popup_container;

    @FXML
    private StackPane header_wrapper;

    @FXML
    private StackPane toolbar_wrapper;

    @FXML
    private StackPane wrapper_first;

    @FXML
    private StackPane wrapper_second;

    @FXML
    private Pane drag_pane;

    @FXML
    private StackPane blockAction_pane;

    @FXML
    private StackPane message_pane;

    @FXML
    private Label message_label;

    private Header header_controller;

    private String nickname;

    private List<StackPane> wrappers;
    private List<Node> screensHistory;

    private Integer wrapperIndex = 0;

    private double speed = 2, speedUpDown = 3;


    public enum Direction {
        Up, Right, Down, Left
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Init");
        wrappers = new ArrayList<>();
        screensHistory = new ArrayList<>();
        wrappers.add(wrapper_first);
        wrappers.add(wrapper_second);
        try {
            Fragments fragments = new Fragments(this);
            header_wrapper.getChildren().setAll(fragments.loadHeader());
            header_controller = fragments.getHeaderController();
            wrappers.get(0).getChildren().setAll(new Pages(this).loadAuth());
        } catch (IOException e) {
            log.log(Level.WARNING, "Exception Error:" + e.getMessage());
        }
    }

    public void showToolbar(String nickname) {
        this.nickname = nickname;
        try {
            getToolbar_wrapper().getChildren().setAll(new Fragments(this).loadToolBar(this.nickname));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new SlideInUp(getToolbar_wrapper()).setSpeed(1.5).play();
    }

    public Header getHeader_controller(){
        return header_controller;
    }

    public StackPane getMain_container() {
        return main_container;
    }

    public Pane getPopup_container() {
        return popup_container;
    }

    public StackPane getNextWrapper(){
        Integer index = wrapperIndex;
        index++;
        if (index >= wrappers.size()) {
            index = 0;
        }
        return wrappers.get(index);
    }

    public StackPane getCurrentWrapper(){
        return wrappers.get(wrapperIndex);
    }

    public void addWrapperIndex(){
        if (wrapperIndex + 1 >= wrappers.size()){
            wrapperIndex = 0;
        } else {
            wrapperIndex++;
        }
    }

    public StackPane getToolbar_wrapper() {
        return toolbar_wrapper;
    }

    public void nextScene(Node load, Direction direction) {
        log.info("Next scene");
        AnimationFX animation = null, nextAnimation = null, additionalAnimationIn = null, additionalAnimationOut = null;
        double speed = this.speed;
        switch (direction) {
            case Down:
                animation = new SlideOutUp(getCurrentWrapper());
                nextAnimation = new SlideInUp(getNextWrapper());
                additionalAnimationIn = new FadeIn(getNextWrapper());
                additionalAnimationOut = new FadeOut(getCurrentWrapper());
                speed = this.speedUpDown;
                break;
            case Left:
                animation = new SlideOutRight(getCurrentWrapper());
                nextAnimation = new SlideInLeft(getNextWrapper());
                break;
            case Up:
                animation = new SlideOutDown(getCurrentWrapper());
                nextAnimation = new SlideInDown(getNextWrapper());
                additionalAnimationIn = new FadeIn(getNextWrapper());
                additionalAnimationOut = new FadeOut(getCurrentWrapper());
                speed = this.speedUpDown;
                break;
            case Right:
                animation = new SlideOutLeft(getCurrentWrapper());
                nextAnimation = new SlideInRight(getNextWrapper());
                break;
        }
        animation.setSpeed(speed);
        nextAnimation.setSpeed(speed);
        animation.setResetOnFinished(true);
        getCurrentWrapper().setDisable(true);
        getNextWrapper().setDisable(false);
        blockAction_pane.setDisable(false);
        animation.getTimeline().setOnFinished(e -> {
            getCurrentWrapper().getChildren().clear();
            blockAction_pane.setDisable(true);
            addWrapperIndex();
        });
        getNextWrapper().getChildren().setAll(load);
        screensHistory.add(load);
        if (additionalAnimationIn != null) {
            additionalAnimationIn.setSpeed(speed);
            additionalAnimationOut.setSpeed(speed);
            additionalAnimationIn.play();
            additionalAnimationOut.play();
        }
        animation.play();
        nextAnimation.play();
    }

    public void lastScene(Direction direction) {
        log.info("Back scene");
        AnimationFX animation = null, nextAnimation = null, additionalAnimationIn = null, additionalAnimationOut = null;
        double speed = this.speed;
        switch (direction) {
            case Down:
                animation = new SlideOutUp(getCurrentWrapper());
                nextAnimation = new SlideInUp(getNextWrapper());
                additionalAnimationOut = new FadeOut(getCurrentWrapper());
                additionalAnimationIn = new FadeIn(getNextWrapper());
                speed = this.speedUpDown;
                break;
            case Left:
                animation = new SlideOutRight(getCurrentWrapper());
                nextAnimation = new SlideInLeft(getNextWrapper());
                break;
            case Up:
                animation = new SlideOutDown(getCurrentWrapper());
                nextAnimation = new SlideInDown(getNextWrapper());
                additionalAnimationOut = new FadeOut(getCurrentWrapper());
                additionalAnimationIn = new FadeIn(getNextWrapper());
                speed = this.speedUpDown;
                break;
            case Right:
                animation = new SlideOutLeft(getCurrentWrapper());
                nextAnimation = new SlideInRight(getNextWrapper());
                break;
        }
        animation.setSpeed(speed);
        nextAnimation.setSpeed(speed);
        animation.setResetOnFinished(true);
        getCurrentWrapper().setDisable(true);
        getNextWrapper().setDisable(false);
        blockAction_pane.setDisable(false);
        animation.getTimeline().setOnFinished(e -> {
            getCurrentWrapper().getChildren().clear();
            blockAction_pane.setDisable(true);
            addWrapperIndex();
        });
        getNextWrapper().getChildren().setAll(screensHistory.get(screensHistory.size() - 2));
        screensHistory.remove(screensHistory.size() - 1);
        if (additionalAnimationIn != null) {
            additionalAnimationIn.setSpeed(speed);
            additionalAnimationOut.setSpeed(speed);
            additionalAnimationIn.play();
            additionalAnimationOut.play();
        }
        animation.play();
        nextAnimation.play();
    }

    public void blurScene() {
        log.info("Scene blur");
        GaussianBlur gaussianBlur = new GaussianBlur(0);
        layout_wrapper.setEffect(gaussianBlur);
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(gaussianBlur.radiusProperty(), 10);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(120), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    public void unBlurScene(){
        log.info("Scene unblur");
        GaussianBlur gaussianBlur = new GaussianBlur(10);
        layout_wrapper.setEffect(gaussianBlur);
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(gaussianBlur.radiusProperty(), 0);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(120), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    public void showMessage(String message){
        message_label.setText(message);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(120), message_pane);
        fadeTransition.setToValue(1);
        message_pane.setDisable(false);
        blurScene();
        fadeTransition.play();
    }

    public void hideMessage(){
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(120), message_pane);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(e -> message_pane.setDisable(true));
        unBlurScene();
        fadeTransition.play();
    }

    public void registerStage(Stage stage) {
        EffectUtilities.makeDraggable(stage, drag_pane);
    }

}
