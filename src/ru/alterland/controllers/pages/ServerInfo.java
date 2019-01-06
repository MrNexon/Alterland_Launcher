package ru.alterland.controllers.pages;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import ru.alterland.controllers.MainWrapper;
import ru.alterland.java.ServerData;
import ru.alterland.java.launcher.MinecraftManager;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerInfo implements Initializable {


    private MainWrapper mainWrapper;
    private MainServers mainServers;
    private ServerData serverData;

    @FXML
    private StackPane cover_wrapper;

    @FXML
    private Label title, description;

    @FXML
    private JFXButton play_button, back_button;

    public ServerInfo(MainWrapper mainWrapper, MainServers mainServers, ServerData serverData){
        this.mainServers = mainServers;
        this.mainWrapper = mainWrapper;
        this.serverData = serverData;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ImageView imageView = new ImageView(serverData.getCover());
        imageView.setFitWidth(250);
        imageView.setFitHeight(380);
        cover_wrapper.getChildren().setAll(imageView);
        title.setText(serverData.getTitle());
        description.setText(serverData.getDescription());
        switch (serverData.getArticleStatus()){
            case Buy:
                play_button.setText("Купить");
                break;
            case Purchase:
                play_button.setText("Играть");
                break;
        }
        if (mainWrapper.getStatus()) {
            play_button.setText("В игре");
        }
        play_button.setDisable(mainWrapper.getStatus());
    }

    public ServerData getServerData() {
        return serverData;
    }

    public void play(MouseEvent mouseEvent) {
        play_button.setDisable(true);
        play_button.setText("В игре");
        mainWrapper.setProgressBarColor(serverData.getBrandColor());
        new MinecraftManager(serverData, mainServers.getServerCards(), mainWrapper).startClient();
    }
    public void back(MouseEvent mouseEvent){
        mainWrapper.lastScene(MainWrapper.Direction.Up);
    }
}
