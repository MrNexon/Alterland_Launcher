package ru.alterland.java.values;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import ru.alterland.Main;
import ru.alterland.controllers.MainWrapper;
import ru.alterland.controllers.popups.DialogWindow;
import ru.alterland.controllers.popups.UserAction;

import java.io.IOException;
import java.util.logging.Logger;

public class Popups {
    private MainWrapper mainWrapper;
    private static Logger log = Logger.getLogger(Popups.class.getName());

    private UserAction userActionController;
    private DialogWindow dialogWindowController;

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

    public Node loadDialogWindow(String text, DialogWindow.DialogButtons dialogButtons) {
        log.info("Load dialog window popup");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ru/alterland/gui/fxml/popups/DialogWindow.fxml"));
        dialogWindowController = new DialogWindow(mainWrapper, text, dialogButtons);
        fxmlLoader.setController(dialogWindowController);
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            Main.shutdown();
            return null;
        }
    }

    public UserAction getUserActionController() {
        return userActionController;
    }
    public DialogWindow getDialogWindowController() {
        return dialogWindowController;
    }
}
