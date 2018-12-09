package ru.alterland.controllers.pages;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import ru.alterland.controllers.MainWrapper;

import java.net.URL;
import java.util.ResourceBundle;


public class Auth implements Initializable {

    @FXML
    private ImageView image_shape;

    /*@FXML
    private GridPane login_wrapper;*/

    private MainWrapper mainWrapper;

    public Auth(MainWrapper mainWrapper){
        this.mainWrapper = mainWrapper;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TranslateTransition transitionX = new TranslateTransition(Duration.millis(4569), image_shape);
        transitionX.setToX(5);
        transitionX.setAutoReverse(true);
        transitionX.setCycleCount(TranslateTransition.INDEFINITE);
        transitionX.play();

        TranslateTransition transitionY = new TranslateTransition(Duration.millis(5731), image_shape);
        transitionY.setToY(5);
        transitionY.setAutoReverse(true);
        transitionY.setCycleCount(TranslateTransition.INDEFINITE);
        transitionY.play();


    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        mainWrapper.showMainScreen();
    }
}
