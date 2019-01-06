package ru.alterland.java.values;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import ru.alterland.controllers.MainWrapper;
import ru.alterland.controllers.fragments.Header;
import ru.alterland.controllers.fragments.Nickname;
import ru.alterland.controllers.fragments.ServerCard;
import ru.alterland.controllers.fragments.ToolBar;
import ru.alterland.controllers.popups.UserAction;
import ru.alterland.java.ServerData;

import java.io.IOException;
import java.util.logging.Logger;

public class Fragments {
    private MainWrapper mainWrapper;
    private static Logger log = Logger.getLogger(Fragments.class.getName());

    private ToolBar toolBarController;
    private Header headerController;
    private Nickname nicknameController;
    private ServerCard serverCardController;

    public Fragments(MainWrapper mainWrapper){
        this.mainWrapper = mainWrapper;
    }

    public Node loadHeader() throws IOException {
        log.info("Load header fragment");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ru/alterland/gui/fxml/fragments/Header.fxml"));
        headerController = new Header(mainWrapper);
        fxmlLoader.setController(headerController);
        return fxmlLoader.load();
    }

    public Node loadToolBar(String nickname) throws IOException {
        log.info("Load toolbar fragment");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ru/alterland/gui/fxml/fragments/ToolBar.fxml"));
        toolBarController = new ToolBar(mainWrapper, nickname);
        fxmlLoader.setController(toolBarController);
        return fxmlLoader.load();
    }

    public Node loadToolBarNickname(String nickname) throws IOException {
        log.info("Load first toolbar nickname fragment");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ru/alterland/gui/fxml/fragments/Nickname.fxml"));
        nicknameController = new Nickname(mainWrapper, nickname);
        fxmlLoader.setController(nicknameController);
        return fxmlLoader.load();
    }

    public Node loadToolBarNickname(UserAction userAction, String nickname) throws IOException {
        log.info("Load second toolbar nickname fragment");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ru/alterland/gui/fxml/fragments/Nickname.fxml"));
        nicknameController = new Nickname(mainWrapper, userAction, nickname);
        fxmlLoader.setController(nicknameController);
        return fxmlLoader.load();
    }

    public Node loadServerCard(ServerData serverData) throws IOException {
        log.info("Load apiServer serverData fragment");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ru/alterland/gui/fxml/fragments/ServerCard.fxml"));
        serverCardController = new ServerCard(serverData);
        fxmlLoader.setController(serverCardController);
        return fxmlLoader.load();
    }

    public ToolBar getToolBarController(){
        return toolBarController;
    }

    public Header getHeaderController(){
        return headerController;
    }

    public Nickname getNicknameController() {
        return nicknameController;
    }

    public ServerCard getServerCardController() {return serverCardController; }
}
