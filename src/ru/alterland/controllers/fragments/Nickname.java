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
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;
import ru.alterland.controllers.MainWrapper;

import java.net.URL;
import java.util.ResourceBundle;

public class Nickname implements Initializable {
    @FXML
    private Label nickname_label;

    @FXML
    private MaterialDesignIconView more_button;

    private MainWrapper mainWrapper;
    private String nickname;
    private boolean moreExpanded = false;

    public Nickname(MainWrapper mainWrapper, String nickname, Boolean is_active){
        this.mainWrapper = mainWrapper;
        this.nickname = nickname;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nickname_label.setText(nickname);
    }

    public void show_more(MouseEvent mouseEvent) {
        if (moreExpanded) {
            RotateTransition rotate = new RotateTransition(Duration.millis(100), (Node) mouseEvent.getSource());
            rotate.setToAngle(0);
            rotate.setCycleCount(1);
            rotate.setInterpolator(Interpolator.EASE_OUT);
            rotate.play();
            moreExpanded = !moreExpanded;
        } else {
            RotateTransition rotate = new RotateTransition(Duration.millis(100), (Node) mouseEvent.getSource());
            rotate.setToAngle(180);
            rotate.setCycleCount(1);
            rotate.setInterpolator(Interpolator.EASE_OUT);
            rotate.play();
            moreExpanded = !moreExpanded;

            FadeTransition transition = new FadeTransition(Duration.millis(150), (Node) mouseEvent.getSource());
            transition.setToValue(1.0);
            transition.play();
        }
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
