package ru.alterland.java;

import javafx.scene.image.Image;

import java.net.InetAddress;

public class Card {
    private Image cover;
    private String id;
    private String title;
    private String description;
    private ArticleStatus articleStatus;
    private ServerStatus serverStatus;
    private InetAddress serverAddress;

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

    public InetAddress getServerAddress() {
        return serverAddress;
    }

    public void setServerStatus(ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }

    public enum ArticleStatus {
        Purchase, Installed, Damaged, Buy, Close
    }

    public enum ServerStatus {
        Online, Offline, Close, Service, Restart, Unavailable
    }

    public Card(String url, String id, String title, String description, ArticleStatus articleStatus, InetAddress serverAddress) {
        this.cover = new Image(url, 290, 418, false, true, false);
        this.id = id;
        this.title = title;
        this.description = description;
        this.articleStatus = articleStatus;
        this.serverAddress = serverAddress;
    }

    public Card(String url, String id, String title, String description, ArticleStatus articleStatus, ServerStatus serverStatus, InetAddress serverAddress) {
        this.cover = new Image(url, 290, 418, false, true, false);
        this.id = id;
        this.title = title;
        this.description = description;
        this.articleStatus = articleStatus;
        this.serverStatus = serverStatus;
        this.serverAddress = serverAddress;
    }
}
