package ru.alterland.java.values;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import ru.alterland.controllers.pages.Auth;
import ru.alterland.controllers.pages.MainServers;
import ru.alterland.controllers.MainWrapper;

import java.io.IOException;

public class Pages {

    private MainWrapper mainWrapper;

    private MainServers mainServersController;
    private Auth authController;

    public Pages(MainWrapper mainWrapper) {
        this.mainWrapper = mainWrapper;
    }

    public Node loadAuth() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../gui/fxml/pages/Auth.fxml"));
        authController = new Auth(mainWrapper);
        fxmlLoader.setController(authController);
        return fxmlLoader.load();
    }

    public Node loadMainServers() throws  IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../gui/fxml/pages/MainServers.fxml"));
        mainServersController = new MainServers(mainWrapper);
        fxmlLoader.setController(mainServersController);
        return fxmlLoader.load();
    }

    public Auth getAuthController() {
        return authController;
    }

    public MainServers getMainServersController() {
        return mainServersController;
    }
}
