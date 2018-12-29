package ru.alterland.controllers.fragments;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import ru.alterland.java.Card;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerCard implements Initializable {

    @FXML
    private ImageView cover_image;

    private Card card;

    public ServerCard(Card card){
        this.card = card;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cover_image.setImage(card.getCover());
    }

    public Card getCard(){
        return card;
    }
}
