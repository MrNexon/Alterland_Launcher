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
    private MinecraftManager minecraftManager;

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
        back_button.setDisableVisualFocus(true);
        play_button.setDisableVisualFocus(true);
        System.out.println(serverData.getServerStatus());
        ImageView imageView = new ImageView(serverData.getCover());
        imageView.setFitWidth(250);
        imageView.setFitHeight(380);
        cover_wrapper.getChildren().setAll(imageView);
        title.setText(serverData.getTitle());
        description.setText(serverData.getDescription());
        buttonsUpdate();
    }

    public ServerData getServerData() {
        return serverData;
    }

    public void buttonsUpdate() {
        play_button.setDisable(mainWrapper.getLaunchActionState());
        if (mainWrapper.getLaunchActionState()) {
            back_button.setFocusTraversable(false);
            play_button.setText("Проверка...");
            back_button.getStyleClass().remove("primary-text-color");
            back_button.getStyleClass().add("text-red-color");
            back_button.setText("Отмена");
        } else{
            switch (serverData.getArticleStatus()){
                case Buy:
                    play_button.setText("Купить");
                    break;
                case Purchase:
                    play_button.setText("Играть");
                    break;
            }

            switch (serverData.getServerStatus()){
                case Offline:
                    play_button.setText("Отключен");
                    play_button.getStyleClass().remove("primary-color");
                    play_button.getStyleClass().add("red-color");
                    play_button.setDisable(true);
                    break;
            }

            back_button.getStyleClass().add("primary-text-color");
            back_button.getStyleClass().remove("text-red-color");
            back_button.setText("Назад");
        }
    }

    public void minecraftLaunch(){
        back_button.setText("Выйти");
        play_button.setText("В игре");
    }

    public void play(MouseEvent mouseEvent) {
        mainWrapper.setProgressBarColor(serverData.getBrandColor());
        minecraftManager = new MinecraftManager(serverData, mainServers.getServerCards(), mainWrapper);
        minecraftManager.startClient(() -> minecraftLaunch());
        minecraftManager.setMinecraftExitAction(() -> buttonsUpdate());
        buttonsUpdate();
    }
    public void back(MouseEvent mouseEvent){
        if (mainWrapper.getLaunchActionState()) {
            stopLaunching();
            buttonsUpdate();
        } else mainWrapper.lastScene(MainWrapper.Direction.Up);
    }

    public void stopLaunching() {
        minecraftManager.cancelLaunching();
        minecraftManager = null;
    }
}
