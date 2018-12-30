package ru.alterland.java;

import javafx.scene.image.Image;

public class Card {
    private Image cover;
    private String id;
    private String title;
    private String description;
    private ArticleStatus articleStatus;
    private ServerStatus serverStatus;

    public Image getCover() {
        return cover;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ArticleStatus getArticleStatus() {
        return articleStatus;
    }

    public ServerStatus getServerStatus() {
        return serverStatus;
    }


    public void setServerStatus(ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }

    public enum ArticleStatus {
        Buy, Purchase, Downloading, Installed
    }

    public enum ServerStatus {
        Online, Offline, Close, Service, Restart, Unavailable
    }

    public Card(String url, String id, String title, String description, ArticleStatus articleStatus) {
        this.cover = new Image(url, 250, 380, false, true, true);
        this.id = id;
        this.title = title;
        this.description = description;
        this.articleStatus = articleStatus;
    }

    public Card(String url, String id, String title, String description, ArticleStatus articleStatus, ServerStatus serverStatus) {
        this.cover = new Image(url, 250, 380, false, true, false);
        this.id = id;
        this.title = title;
        this.description = description;
        this.articleStatus = articleStatus;
        this.serverStatus = serverStatus;
    }
}
