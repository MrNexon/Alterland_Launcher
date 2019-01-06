package ru.alterland.java;

import javafx.scene.image.Image;

public class ServerData {
    private Image cover;
    private String brandColor;
    private String name;
    private String title;
    private String description;
    private ArticleStatus articleStatus;
    private ServerStatus serverStatus;

    public Image getCover() {
        return cover;
    }

    public String getBrandColor() {return brandColor;}

    public String getName() {
        return name;
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
        Buy, Purchase
    }

    public enum ServerStatus {
        Online, Offline, Close, Service, Restart, Unavailable
    }

    public ServerData(String url, String brandColor, String name, String title, String description, ArticleStatus articleStatus) {
        this.cover = new Image(url, 250, 380, false, true, true);
        this.brandColor = brandColor;
        this.name = name;
        this.title = title;
        this.description = description;
        this.articleStatus = articleStatus;
    }

    public ServerData(String url, String brandColor, String name, String title, String description, ArticleStatus articleStatus, ServerStatus serverStatus) {
        this.cover = new Image(url, 250, 380, false, true, false);
        this.brandColor = brandColor;
        this.name = name;
        this.title = title;
        this.description = description;
        this.articleStatus = articleStatus;
        this.serverStatus = serverStatus;
    }
}
