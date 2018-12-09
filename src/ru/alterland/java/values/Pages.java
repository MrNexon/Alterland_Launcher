package ru.alterland.java.values;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import ru.alterland.controllers.pages.Auth;
import ru.alterland.controllers.pages.MainServers;
import ru.alterland.controllers.MainWrapper;

import java.io.IOException;

public class Pages {

    private MainWrapper mainWrapper;

    public Pages(MainWrapper mainWrapper) {
        this.mainWrapper = mainWrapper;
    }

    public Node loadAuth() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../gui/fxml/pages/Auth.fxml"));
        Auth auth = new Auth(mainWrapper);
        fxmlLoader.setController(auth);
        return fxmlLoader.load();
    }

    public Node loadMainServers() throws  IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../gui/fxml/pages/MainServers.fxml"));
        MainServers mainServers = new MainServers(mainWrapper);
        fxmlLoader.setController(mainServers);
        return fxmlLoader.load();
    }
}
