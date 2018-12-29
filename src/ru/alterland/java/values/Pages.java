package ru.alterland.java.values;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import ru.alterland.controllers.MainWrapper;
import ru.alterland.controllers.pages.Auth;
import ru.alterland.controllers.pages.MainServers;
import ru.alterland.controllers.pages.ServerInfo;
import ru.alterland.java.Card;

import java.io.IOException;
import java.util.logging.Logger;

public class Pages {

    private MainWrapper mainWrapper;
    private static Logger log = Logger.getLogger(Pages.class.getName());

    private MainServers mainServersController;
    private Auth authController;
    private ServerInfo serverInfoController;

    public Pages(MainWrapper mainWrapper) {
        this.mainWrapper = mainWrapper;
    }

    public Node loadAuth() throws IOException {
        log.info("Load auth page");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ru/alterland/gui/fxml/pages/Auth.fxml"));
        authController = new Auth(mainWrapper);
        fxmlLoader.setController(authController);
        return fxmlLoader.load();
    }

    public Node loadMainServers() throws IOException {
        log.info("Load main servers page");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ru/alterland/gui/fxml/pages/MainServers.fxml"));
        mainServersController = new MainServers(mainWrapper);
        fxmlLoader.setController(mainServersController);
        return fxmlLoader.load();
    }

    public Node loadServerInfo(MainServers mainServers, Card card) throws IOException {
        log.info("Load apiServer info page");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ru/alterland/gui/fxml/pages/ServerInfo.fxml"));
        serverInfoController = new ServerInfo(mainWrapper, mainServers, card);
        fxmlLoader.setController(serverInfoController);
        return fxmlLoader.load();
    }

    public Auth getAuthController() {
        return authController;
    }

    public MainServers getMainServersController() {
        return mainServersController;
    }

    public ServerInfo getServerInfoController() {return serverInfoController;}
}
