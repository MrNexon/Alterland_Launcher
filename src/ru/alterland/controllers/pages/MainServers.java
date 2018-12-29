package ru.alterland.controllers.pages;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import ru.alterland.Main;
import ru.alterland.controllers.MainWrapper;
import ru.alterland.controllers.fragments.ServerCard;
import ru.alterland.java.Card;
import ru.alterland.java.api.Exceptions.ApiExceptions;
import ru.alterland.java.api.Servers;
import ru.alterland.java.values.Fragments;
import ru.alterland.java.values.Pages;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainServers implements Initializable {

    private static Logger log = Logger.getLogger(MainServers.class.getName());

    @FXML
    private HBox servers_hbox;

    private MainWrapper mainWrapper;
    private Node prevNode;
    private List<Node> serverCards;
    private List<ServerCard> serverCardsControllers;

    public MainServers(MainWrapper mainWrapper){
        this.mainWrapper = mainWrapper;
        serverCards = new ArrayList<>();
        serverCardsControllers = new ArrayList<>();
        mainWrapper.getHeader_controller().showMinimizeIcon();
        try {
            List<Card> cards = Servers.getServerList(Main.userData);
            cards.forEach(card -> {
                try {
                    Fragments fragments = new Fragments(this.mainWrapper);
                    Node node = fragments.loadServerCard(card);
                    serverCardsControllers.add(fragments.getServerCardController());
                    node.setOnMouseEntered(this::button_entered);
                    node.setOnMouseClicked(this::choose_server);
                    node.setCursor(Cursor.HAND);
                    servers_hbox.setMargin(node, new Insets(20, 20, 20, 20));
                    if (serverCards.size() > 0){
                        node.setOpacity(0.6);
                        node.setScaleX(0.95);
                        node.setScaleY(0.95);
                    }
                    System.out.println(node);
                    serverCards.add(node);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (ApiExceptions apiExceptions) {
            apiExceptions.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Init");
        prevNode = serverCards.get(0);
        servers_hbox.getChildren().setAll(serverCards);
    }

    public void button_entered(MouseEvent mouseEvent) {
        Node inputNode = (Node) mouseEvent.getSource();
        log.info("Mouse over: " + inputNode);
        if (inputNode == prevNode) return;
        else button_exited(prevNode);

        FadeTransition transitionFade = new FadeTransition(Duration.millis(120), inputNode);
        transitionFade.setToValue(1);

        ScaleTransition transition = new ScaleTransition(Duration.millis(120), inputNode);
        transition.setToX(1.0);
        transition.setToY(1.0);

        transitionFade.play();
        transition.play();

        prevNode = inputNode;
    }

    public void button_exited(Node inputNode) {
        FadeTransition transitionFade = new FadeTransition(Duration.millis(120), inputNode);
        transitionFade.setToValue(0.6);

        ScaleTransition transition = new ScaleTransition(Duration.millis(120), inputNode);
        transition.setToX(0.95);
        transition.setToY(0.95);

        transitionFade.play();
        transition.play();
    }

    public void choose_server(MouseEvent mouseEvent){
        try {
            Node node = (Node) mouseEvent.getSource();
            Card card = serverCardsControllers.get(serverCards.indexOf(node)).getCard();
            this.mainWrapper.nextScene(new Pages(mainWrapper).loadServerInfo(this,  card), MainWrapper.Direction.Down);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
