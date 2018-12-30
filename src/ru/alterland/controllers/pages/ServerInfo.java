package ru.alterland.controllers.pages;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import ru.alterland.controllers.MainWrapper;
import ru.alterland.java.Card;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerInfo implements Initializable {


    private MainWrapper mainWrapper;
    private MainServers mainServers;
    private Card card;

    @FXML
    private StackPane cover_wrapper;

    @FXML
    private Label title, description;

    @FXML
    private JFXButton play_button, back_button;

    public ServerInfo(MainWrapper mainWrapper, MainServers mainServers, Card card){
        this.mainServers = mainServers;
        this.mainWrapper = mainWrapper;
        this.card = card;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ImageView imageView = new ImageView(card.getCover());
        imageView.setFitWidth(270);
        imageView.setFitHeight(400);
        cover_wrapper.getChildren().setAll(imageView);
        title.setText(card.getTitle());
        description.setText(card.getDescription());
        switch (card.getArticleStatus()){
            case Installed:
                play_button.setText("Играть");
                break;
            case Buy:
                play_button.setText("Купить");
                break;
            case Purchase:
                play_button.setText("Установить");
                break;
            case Downloading:
                play_button.setDisable(true);
                play_button.setText("Установка...");
                break;
        }
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
    public void play(MouseEvent mouseEvent) {
        mainWrapper.showProgressBar();
    }
    public void back(MouseEvent mouseEvent){
        mainWrapper.lastScene(MainWrapper.Direction.Up);
    }
}
