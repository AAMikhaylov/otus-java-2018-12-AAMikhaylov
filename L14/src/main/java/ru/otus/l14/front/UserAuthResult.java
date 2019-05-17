package ru.otus.l14.front;

public class UserAuthResult {
    private final boolean authResult;
    private final String login;
    private final String userName;

    public UserAuthResult(boolean authResult, String login, String userName) {
        this.authResult = authResult;
        this.login = login;
        this.userName = userName;
    }

    public boolean isAuth() {
        return authResult;
    }

    public String getLogin() {
        return login;
    }

    public String getUserName() {
        return userName;
    }
}
