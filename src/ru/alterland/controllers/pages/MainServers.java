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
import ru.alterland.java.ServerData;
import ru.alterland.java.launcher.ScheduledTasks;
import ru.alterland.java.values.Fragments;
import ru.alterland.java.values.Pages;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

public class MainServers implements Initializable {

    private static Logger log = Logger.getLogger(MainServers.class.getName());

    @FXML
    private HBox servers_hbox;

    private MainWrapper mainWrapper;
    private Node prevNode;
    private List<Node> serverCards;
    private List<ServerCard> serverCardsControllers;

    public MainServers(MainWrapper mainWrapper, List<ServerData> serverData){
        this.mainWrapper = mainWrapper;
        mainWrapper.getHeader_controller().showMinimizeIcon();
        renderCardList(serverData);
        TimerTask timerTask = new ScheduledTasks.UpdateServerList(this);
        Main.getUpdateServersTimer().schedule(timerTask, 15000, 15000);
    }

    public MainWrapper getMainWrapper() { return mainWrapper; }

    public List<ServerCard> getServerCards(){
        return serverCardsControllers;
    }

    public List<ServerData> getServersData(){
        List<ServerData> serversData = new ArrayList<>();
        serverCardsControllers.forEach(serverCard -> serversData.add(serverCard.getServerData()));
        return serversData;
    }

    public void renderCardList(List<ServerData> serversData) {
        serverCards = new ArrayList<>();
        serverCardsControllers = new ArrayList<>();
        if (servers_hbox != null) { servers_hbox.getChildren().clear(); }
        serversData.forEach(this::renderServerCard);
    }

    public void renderServerCard(ServerData serverData) {
        try {
            Fragments fragments = new Fragments(this.mainWrapper);
            Node node = fragments.loadServerCard(serverData);
            serverCardsControllers.add(fragments.getServerCardController());
            node.setOnMouseEntered(this::button_entered);
            node.setOnMouseClicked(this::choose_server);
            node.setCursor(Cursor.HAND);
            HBox.setMargin(node, new Insets(20, 20, 20, 20));
            if (serverCards.size() > 0) {
                node.setOpacity(0.6);
                node.setScaleX(0.95);
                node.setScaleY(0.95);
            }
            serverCards.add(node);
            if (servers_hbox != null) {
                servers_hbox.getChildren().add(node);
            }
        } catch (IOException e) {
            Main.fatalError(mainWrapper, e);
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
            ServerData serverData = serverCardsControllers.get(serverCards.indexOf(node)).getServerData();
            this.mainWrapper.nextScene(new Pages(mainWrapper).loadServerInfo(this, serverData), MainWrapper.Direction.Down);
        } catch (IOException e) {
            Main.fatalError(mainWrapper, e);
        }
    }
}
