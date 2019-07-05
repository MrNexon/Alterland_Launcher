package ru.alterland.controllers;

import animatefx.animation.*;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.alterland.Main;
import ru.alterland.controllers.fragments.Header;
import ru.alterland.java.launcher.EffectUtilities;
import ru.alterland.java.values.Fragments;
import ru.alterland.java.values.Pages;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainWrapper implements Initializable {
    private static Logger log = Logger.getLogger(MainWrapper.class.getName());

    @FXML
    private StackPane main_container, header_wrapper, toolbar_wrapper, wrapper_first, wrapper_second, blockAction_pane, message_pane, dialog_container;
    @FXML
    private BorderPane layout_wrapper;
    @FXML
    private Pane popup_container, drag_pane;
    @FXML
    private Label message_label, progress_label;
    @FXML
    private ProgressBar loading_bar;
    @FXML
    private ImageView drag_image;

    private Header header_controller;

    private String nickname;

    private List<StackPane> wrappers;
    private List<Node> screensHistory;

    private Integer wrapperIndex = 0;

    private double speed = 2, speedUpDown = 3;

    private Boolean launchActionState = false;

    public enum Direction {
        Up, Right, Down, Left
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Init");
        loading_bar.setScaleX(0);
        progress_label.setOpacity(0);
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
            Main.fatalError(this, e);
        }
    }

    public Boolean getLaunchActionState() {
        return launchActionState;
    }

    public void setLaunchActionState(Boolean launchActionState){
        this.launchActionState = launchActionState;
    }

    public void showToolbar(String nickname) {
        this.nickname = nickname;
        try {
            getToolbar_wrapper().getChildren().setAll(new Fragments(this).loadToolBar(this.nickname));
        } catch (IOException e) {
            Main.fatalError(this, e);
        }
        new SlideInUp(getToolbar_wrapper()).setSpeed(1.5).play();
    }

    public void hideToolbar() {
        SlideOutDown slideOutDown = new SlideOutDown(getToolbar_wrapper());
        slideOutDown.setSpeed(1.5).getTimeline().setOnFinished((event) -> getToolbar_wrapper().getChildren().clear());
        slideOutDown.play();
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

    public StackPane getDialog_container() { return dialog_container; }

    public void addWrapperIndex(){
        if (wrapperIndex + 1 >= wrappers.size()){
            wrapperIndex = 0;
        } else {
            wrapperIndex++;
        }
    }

    public ProgressBar getLoading_bar(){
        return loading_bar;
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

    public Node getCurrentPage() {
        return screensHistory.get(screensHistory.size() - 1);
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
        loading_bar.setEffect(gaussianBlur);
        progress_label.setEffect(gaussianBlur);
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
        loading_bar.setEffect(gaussianBlur);
        progress_label.setEffect(gaussianBlur);
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(gaussianBlur.radiusProperty(), 0);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(120), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    public void showMessage(String message){
        message_label.setText(message);
        if (message_pane.getOpacity() == 1) return;
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
        EffectUtilities.makeDraggable(stage, drag_image);
    }

    public void showProgressBar(){
        setProgressBarValue(ProgressBar.INDETERMINATE_PROGRESS);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), progress_label);
        fadeTransition.setToValue(0.6);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), loading_bar);
        scaleTransition.setToX(1);
        fadeTransition.play();
        scaleTransition.play();
    }

    public void hideProgressBar(){
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), progress_label);
        fadeTransition.setToValue(0);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), loading_bar);
        scaleTransition.setToX(0);
        fadeTransition.play();
        scaleTransition.play();
    }

    public void setProgressBarColor(String color){
        loading_bar.setStyle("loading-color: " + color);
    }

    public void setProgressBarValue(double value){
        loading_bar.setProgress(value);
    }

    public void setProgressLabelText(String text) {
        progress_label.setText(text);
    }
}
