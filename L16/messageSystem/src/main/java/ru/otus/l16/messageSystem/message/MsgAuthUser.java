package ru.otus.l16.messageSystem.message;

import ru.otus.l16.messageSystem.Address;

public class MsgAuthUser extends Message {
    private final String login;
    private final String userName;
    private final String password;
    private final String sessionID;
    private final boolean auth;

    public MsgAuthUser(Address from, Address to, String login, String password, String sessionID) {
        super(MsgAuthUser.class, from, to);
        this.login = login;
        this.password = password;
        this.sessionID = sessionID;
        this.userName = null;
        auth = false;

    }

    public MsgAuthUser(MsgAuthUser origMsg, Address from, Address to, String userName, boolean auth) {
        super(origMsg.getId(), MsgAuthUser.class, from, to);
        this.login = origMsg.login;
        this.password = origMsg.password;
        this.sessionID = origMsg.sessionID;
        this.userName = userName;
        this.auth = auth;
    }


    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getSessionID() {
        return sessionID;
    }

    public boolean isAuth() {
        return auth;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "MsgAuthUser{" +
                "login='" + login + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", sessionID='" + sessionID + '\'' +
                ", auth=" + auth +
                '}';
    }
}
