package ru.alterland.controllers.popups;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import ru.alterland.controllers.MainWrapper;

import java.net.URL;
import java.util.ResourceBundle;

public class DialogWindow extends Popups implements Initializable {
    @FXML
    private StackPane dialog_wrapper, background;

    @FXML
    private Label dialog_text;

    @FXML
    private JFXButton false_button, true_button;

    @FXML
    private VBox dialog_vbox;

    private String text;
    private DialogButtons dialogButtons;
    private Runnable runnable;
    private ClickButton clickButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        background.setOpacity(0);
        dialog_wrapper.setScaleX(0);
        dialog_wrapper.setScaleY(0.1);
        dialog_vbox.setOpacity(0);
        dialog_text.setText(text);
        switch (dialogButtons) {
            case Ok:
                true_button.setText("Ок");
                false_button.setOpacity(0);
                break;
            case YesNo:
                true_button.setText("Да");
                false_button.setText("Нет");
                break;
            case Cancel:
                true_button.setText("Отмена");
                false_button.setOpacity(0);
                break;
            case OkCancel:
                true_button.setText("Ок");
                false_button.setText("Отмена");
            case YesCancel:
                true_button.setText("Да");
                false_button.setText("Отмена");
        }
    }

    public enum DialogButtons {
        Ok, Cancel, YesNo, OkCancel, YesCancel
    }

    public enum ClickButton{
        Ok, Cancel, Yes, No
    }

    public DialogWindow(MainWrapper mainWrapper, String text, DialogButtons dialogButtons){
        super(mainWrapper);
        this.text = text;
        this.dialogButtons = dialogButtons;
    }

    public ClickButton getClickButton() {
        return clickButton;
    }

    @Override
    public void show() {
        show(null);
    }

    @Override
    public void show(Runnable runnable) {
        getMainWrapper().blurScene();
        getMainWrapper().getDialog_container().setOpacity(1);
        getMainWrapper().getDialog_container().setDisable(false);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(140), background);
        fadeTransition.setToValue(0.5);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(70), dialog_wrapper);
        scaleTransition.setToX(1);
        scaleTransition.setOnFinished(e -> {
            ScaleTransition scaleTransition1 = new ScaleTransition(Duration.millis(70), dialog_wrapper);
            scaleTransition1.setToY(1);
            scaleTransition1.setOnFinished(e1 -> {
                FadeTransition fadeTransition1 = new FadeTransition(Duration.millis(140), dialog_vbox);
                fadeTransition1.setToValue(1);
                fadeTransition1.play();
            });
            scaleTransition1.play();
        });
        scaleTransition.play();
        fadeTransition.play();
        this.runnable = runnable;
    }

    @Override
    public void hide() {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(140), dialog_vbox);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(e -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(70), dialog_wrapper);
            scaleTransition.setToY(0.1);
            scaleTransition.setOnFinished(e1 -> {

                ScaleTransition scaleTransition1 = new ScaleTransition(Duration.millis(70), dialog_wrapper);
                scaleTransition1.setToX(0);
                scaleTransition1.setOnFinished(e2 -> {
                    getMainWrapper().getDialog_container().setDisable(true);
                    getMainWrapper().getDialog_container().setOpacity(0);
                });
                scaleTransition1.play();
                getMainWrapper().unBlurScene();
            });
            FadeTransition fadeTransition1 = new FadeTransition(Duration.millis(140), background);
            fadeTransition1.setToValue(0);
            fadeTransition1.play();
            scaleTransition.play();
        });
        fadeTransition.play();
    }

    public void falseButton(MouseEvent mouseEvent) {
        switch (dialogButtons) {
            case YesNo:
                clickButton = ClickButton.No;
                break;
            default:
                clickButton = ClickButton.Cancel;
                break;
        }
        hide();
        runnable.run();
    }

    public void trueButton(MouseEvent mouseEvent) {
        switch (dialogButtons){
            case YesCancel:
                clickButton = ClickButton.Yes;
                break;
            case YesNo:
                clickButton = ClickButton.Yes;
                break;
            case Cancel:
                clickButton = ClickButton.Cancel;
                break;
            default:
                clickButton = ClickButton.Ok;
        }
        hide();
        runnable.run();
    }
}
