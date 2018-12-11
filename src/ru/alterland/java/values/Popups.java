package ru.alterland.java.values;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import ru.alterland.controllers.MainWrapper;
import ru.alterland.controllers.popups.UserAction;

import java.io.IOException;

public class Popups {
    private MainWrapper mainWrapper;

    private UserAction userActionController;

    public Popups(MainWrapper mainWrapper) {
        this.mainWrapper = mainWrapper;
    }

    public Node loadUserAction(String nickname, String uuid) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../gui/fxml/popups/UserAction.fxml"));
        userActionController = new UserAction(mainWrapper, nickname, uuid);
        fxmlLoader.setController(userActionController);
        return fxmlLoader.load();
    }

    public UserAction getUserActionController() {
        return userActionController;
    }
}
