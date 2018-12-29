package ru.alterland.java.values;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import ru.alterland.controllers.MainWrapper;
import ru.alterland.controllers.popups.UserAction;

import java.io.IOException;
import java.util.logging.Logger;

public class Popups {
    private MainWrapper mainWrapper;
    private static Logger log = Logger.getLogger(Popups.class.getName());

    private UserAction userActionController;

    public Popups(MainWrapper mainWrapper) {
        this.mainWrapper = mainWrapper;
    }

    public Node loadUserAction(String nickname, String uuid) throws IOException {
        log.info("Load user actions popup");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ru/alterland/gui/fxml/popups/UserAction.fxml"));
        userActionController = new UserAction(mainWrapper, nickname, uuid);
        fxmlLoader.setController(userActionController);
        return fxmlLoader.load();
    }

    public UserAction getUserActionController() {
        return userActionController;
    }
}
