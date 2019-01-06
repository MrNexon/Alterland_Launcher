package ru.alterland.controllers.fragments;


import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import ru.alterland.Main;
import ru.alterland.controllers.MainWrapper;
import ru.alterland.java.values.Fragments;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class ToolBar implements Initializable {

    private static Logger log = Logger.getLogger(ToolBar.class.getName());

    @FXML
    private Label servers, news, shop;

    @FXML
    private Pane selector_button;

    @FXML
    private FlowPane nickname_wrapper;

    private List<Label> menuItems;
    private Label currentMenuItem;
    private MainWrapper mainWrapper;
    private String nickname;

    public ToolBar(MainWrapper mainWrapper, String nickname) {
        this.mainWrapper = mainWrapper;
        this.nickname = nickname;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Init");
        menuItems = new ArrayList<>();
        menuItems.add(servers);
        menuItems.add(news);
        menuItems.add(shop);
        currentMenuItem = servers;
        try {
            nickname_wrapper.getChildren().setAll(new Fragments(mainWrapper).loadToolBarNickname(nickname));
        } catch (IOException e) {
            Main.fatalError(mainWrapper, e);
        }
    }

    public void button_entered(MouseEvent mouseEvent) {
        FadeTransition transition = new FadeTransition(Duration.millis(150), (Node) mouseEvent.getSource());
        transition.setToValue(1.0);
        transition.play();
    }

    public void button_exited(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == currentMenuItem) return;
        FadeTransition transition = new FadeTransition(Duration.millis(150), (Node) mouseEvent.getSource());
        transition.setToValue(0.6);
        transition.play();
    }

    public void menu_item_clicked(MouseEvent mouseEvent) {
        Label label = (Label) mouseEvent.getSource();
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(selector_button.prefWidthProperty(), label.getWidth() - 1);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(150), keyValue);
        timeline.getKeyFrames().add(keyFrame);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(150), selector_button);
        log.info("Clicked menu item. Index: " + menuItems.indexOf(label));
        double x = (6 * menuItems.indexOf(label));
        for (int i = menuItems.indexOf(label) - 1; i >= 0; i--){
            x += menuItems.get(i).getWidth();
        }
        translateTransition.setToX(x);
        translateTransition.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition transition = new FadeTransition(Duration.millis(150), currentMenuItem);
        transition.setToValue(0.6);

        transition.play();
        translateTransition.play();
        timeline.play();
        currentMenuItem = label;
    }
}
