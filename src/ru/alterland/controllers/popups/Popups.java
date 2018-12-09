package ru.alterland.controllers.popups;

import ru.alterland.controllers.MainWrapper;

abstract public class Popups {
    private MainWrapper mainWrapper;

    public Popups(MainWrapper mainWrapper){
        this.mainWrapper = mainWrapper;
    }

    public abstract void show();

    public MainWrapper getMainWrapper() {
        return mainWrapper;
    }
}
