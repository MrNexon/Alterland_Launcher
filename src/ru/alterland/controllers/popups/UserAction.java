package ru.alterland.controllers.popups;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import ru.alterland.controllers.MainWrapper;
import ru.alterland.controllers.fragments.Nickname;
import ru.alterland.java.values.Fragments;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class UserAction extends Popups implements Initializable {

    private static Logger log = Logger.getLogger(UserAction.class.getName());

    @FXML
    private FlowPane nickname_container;

    @FXML
    private VBox buttons_list_container, more_action_list;

    @FXML
    private StackPane background;

    private String nickname, uuid;
    private Fragments nickname_fragment;
    private Nickname originalNicknameController;

    public UserAction(MainWrapper mainWrapper, String nickname, String uuid) {
        super(mainWrapper);
        this.nickname = nickname;
        this.uuid = uuid;
    }

    public void setOriginalNicknameController(Nickname nickname){
        this.originalNicknameController = nickname;
    }

    @Override
    public void show() {
        log.info("Show");
        getMainWrapper().blurScene();
        getMainWrapper().getPopup_container().setOpacity(1);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(120), background);
        fadeTransition.setToValue(0.5);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(120), more_action_list);
        scaleTransition.setToY(1);
        scaleTransition.setOnFinished(e -> {
            FadeTransition fadeTransition1 = new FadeTransition(Duration.millis(120), buttons_list_container);
            fadeTransition1.setToValue(1);
            fadeTransition1.play();
        });
        getMainWrapper().getPopup_container().setDisable(false);
        scaleTransition.play();
        fadeTransition.play();
        nickname_fragment.getNicknameController().rotateDown();
    }

    @Override
    public void hide() {
        log.info("Hide");
        FadeTransition fadeTransition1 = new FadeTransition(Duration.millis(120), buttons_list_container);
        fadeTransition1.setToValue(0);
        fadeTransition1.setOnFinished(e -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(120), more_action_list);
            scaleTransition.setToY(0);
            getMainWrapper().unBlurScene();
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(120), background);
            fadeTransition.setToValue(0);
            fadeTransition.setOnFinished(e1 -> {
                getMainWrapper().getPopup_container().setDisable(true);
                getMainWrapper().getPopup_container().setOpacity(0);
                originalNicknameController.nickname_container.setOpacity(1);
            });
            fadeTransition.play();
            scaleTransition.play();
        });
        nickname_fragment.getNicknameController().rotateUp();
        fadeTransition1.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Init");
        nickname_fragment = new Fragments(getMainWrapper());
        try {
            nickname_container.getChildren().setAll(nickname_fragment.loadToolBarNickname(this, nickname));
        } catch (IOException e) {
            e.printStackTrace();
        }
        more_action_list.setScaleY(0);
        buttons_list_container.setOpacity(0);
        background.setOpacity(0);
    }

    public void button_entered(MouseEvent mouseEvent) {
        FadeTransition transition = new FadeTransition(Duration.millis(150), (Node) mouseEvent.getSource());
        transition.setToValue(1.0);
        transition.play();
    }

    public void button_exited(MouseEvent mouseEvent) {
        FadeTransition transition = new FadeTransition(Duration.millis(150), (Node) mouseEvent.getSource());
        transition.setToValue(0.6);
        transition.play();
    }
}
