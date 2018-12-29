package ru.alterland.java;

public class UserData {
    private String nickname;
    private String accessToken;
    private Role role;
    private String lastLoginTime;
    private String uuid;
    private Boolean earlyAccess;

    public enum Role {
        user, admin, moderator, support, systemAdmin
    }

    public UserData(String nickname, String accessToken, Role role, String lastLoginTime, String uuid, Boolean earlyAccess){
        this.nickname = nickname;
        this.accessToken = accessToken;
        this.role = role;
        this.lastLoginTime = lastLoginTime;
        this.uuid = uuid;
        this.earlyAccess = earlyAccess;
    }
    public String getNickname() {
        return nickname;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Role getRole() {
        return role;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public String getUuid() {
        return uuid;
    }

    public Boolean getEarlyAccess() {
        return earlyAccess;
    }
}
