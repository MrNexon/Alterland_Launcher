package ru.alterland.controllers.fragments;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import ru.alterland.controllers.MainWrapper;
import ru.alterland.controllers.popups.UserAction;
import ru.alterland.java.values.Popups;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class Nickname implements Initializable {
    private static Logger log = Logger.getLogger(Nickname.class.getName());

    @FXML
    private Label nickname_label;

    @FXML
    public HBox nickname_container;

    @FXML
    private MaterialDesignIconView more_button;

    private MainWrapper mainWrapper;
    private String nickname;
    private Boolean is_first = true;
    private Popups userAction;
    private UserAction userActionController;
    private boolean moreExpanded = false;

    public Nickname(MainWrapper mainWrapper, String nickname){
        this.mainWrapper = mainWrapper;
        this.nickname = nickname;
    }

    public Nickname(MainWrapper mainWrapper, UserAction userAction, String nickname){
        this.mainWrapper = mainWrapper;
        this.userActionController = userAction;
        this.nickname = nickname;
        this.is_first = false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Init");
        log.info("Is fisrt: " + is_first);
        nickname_label.setText(nickname);
        userAction = new Popups(mainWrapper);
        if (is_first){
            try {
                mainWrapper.getPopup_container().getChildren().setAll(userAction.loadUserAction(nickname, "0"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void show_more(MouseEvent mouseEvent) {
        log.info("Showing more");
        if (is_first) {
            nickname_container.setOpacity(0);
            userAction.getUserActionController().setOriginalNicknameController(this);
            userAction.getUserActionController().show();
        } else {
            userActionController.hide();
        }
    }

    public void rotateUp() {
        RotateTransition rotate = new RotateTransition(Duration.millis(100), more_button);
        rotate.setToAngle(0);
        rotate.setCycleCount(1);
        rotate.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition transition = new FadeTransition(Duration.millis(150), more_button);
        transition.setToValue(0.6);

        rotate.play();
        transition.play();

        moreExpanded = false;
    }

    public void rotateDown() {
        RotateTransition rotate = new RotateTransition(Duration.millis(100), more_button);
        rotate.setToAngle(180);
        rotate.setCycleCount(1);
        rotate.setInterpolator(Interpolator.EASE_OUT);
        rotate.play();

        FadeTransition transition = new FadeTransition(Duration.millis(150), more_button);
        transition.setToValue(1.0);
        transition.play();

        moreExpanded = true;
    }

    public void button_entered(MouseEvent mouseEvent) {
        FadeTransition transition = new FadeTransition(Duration.millis(150), (Node) mouseEvent.getSource());
        transition.setToValue(1.0);
        transition.play();
    }

    public void button_exited(MouseEvent mouseEvent) {
        if (moreExpanded && mouseEvent.getSource() == more_button) return;
        FadeTransition transition = new FadeTransition(Duration.millis(150), (Node) mouseEvent.getSource());
        transition.setToValue(0.6);
        transition.play();
    }
}
