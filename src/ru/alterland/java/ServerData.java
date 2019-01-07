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
    private String arguments;

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

    public String getArguments() {
        return arguments;
    }

    public void setServerStatus(ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }

    public enum ArticleStatus {
        Buy, Purchase
    }

    public enum ServerStatus {
        Offline, Online, Close, Service, Restart, Unavailable
    }

    public ServerData(String url, String brandColor, String name, String title, String description, ArticleStatus articleStatus, String arguments) {
        this.cover = new Image(url, 250, 380, false, true, true);
        this.brandColor = brandColor;
        this.name = name;
        this.title = title;
        this.description = description;
        this.articleStatus = articleStatus;
        this.arguments = arguments;
    }

    public ServerData(String url, String brandColor, String name, String title, String description, ArticleStatus articleStatus, ServerStatus serverStatus, String arguments) {
        this.cover = new Image(url, 250, 380, false, true, false);
        this.brandColor = brandColor;
        this.name = name;
        this.title = title;
        this.description = description;
        this.articleStatus = articleStatus;
        this.serverStatus = serverStatus;
        this.arguments = arguments;
    }
}
