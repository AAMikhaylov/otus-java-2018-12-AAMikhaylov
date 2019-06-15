package ru.otus.l16.messages;

import ru.otus.l16.messageSystem.Address;

public class MsgAuthUser extends Message {
    private final String login;
    private final String password;
    private final String userName;
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

    void exec(DBService dbService) {


    }
    public boolean isAuth() {
        return auth;
    }

    public String getUserName() {
        return userName;
    }
}