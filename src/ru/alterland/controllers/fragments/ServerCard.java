package ru.alterland.controllers.fragments;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import ru.alterland.java.ServerData;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerCard implements Initializable {

    @FXML
    private ImageView cover_image;

    private ServerData serverData;

    public ServerCard(ServerData serverData){
        this.serverData = serverData;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cover_image.setImage(serverData.getCover());
    }

    public ServerData getServerData(){
        return serverData;
    }
}
