package ru.alterland.java.values;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import ru.alterland.controllers.fragments.Header;
import ru.alterland.controllers.MainWrapper;
import ru.alterland.controllers.fragments.Nickname;
import ru.alterland.controllers.fragments.ToolBar;
import java.io.IOException;

public class Fragments {
    private MainWrapper mainWrapper;

    private ToolBar toolBarController;
    private Header headerController;
    private Nickname nicknameController;

    public Fragments(MainWrapper mainWrapper){
        this.mainWrapper = mainWrapper;
    }

    public Node loadHeader() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../gui/fxml/fragments/Header.fxml"));
        headerController = new Header(mainWrapper);
        fxmlLoader.setController(headerController);
        return fxmlLoader.load();
    }

    public Node loadToolBar(String nickname) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../gui/fxml/fragments/ToolBar.fxml"));
        toolBarController = new ToolBar(mainWrapper, nickname);
        fxmlLoader.setController(toolBarController);
        return fxmlLoader.load();
    }

    public Node loadToolBarNickname(String nickname) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../gui/fxml/fragments/Nickname.fxml"));
        nicknameController = new Nickname(mainWrapper, nickname, false);
        fxmlLoader.setController(nicknameController);
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
}
