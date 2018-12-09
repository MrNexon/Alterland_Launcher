package ru.alterland.controllers.popups;

import ru.alterland.controllers.MainWrapper;

public class UserAction extends Popups {

    private String nickname, uuid;

    public UserAction(MainWrapper mainWrapper, String nickname, String uuid) {
        super(mainWrapper);
        this.nickname = nickname;
        this.uuid = uuid;
    }

    @Override
    public void show() {

    }
}
