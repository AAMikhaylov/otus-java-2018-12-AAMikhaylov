package ru.otus.l16.messageSystem.message;

import ru.otus.l16.messageSystem.Address;

public class MsgGetUsers extends Message {
    private final String userId;
    private final String usersJsonList;

    public MsgGetUsers(Address from, Address to, String userId) {
        super(MsgGetUsers.class, from, to);
        this.userId = userId;
        this.usersJsonList = null;
    }

    public MsgGetUsers(MsgGetUsers origMsg, Address from, Address to, String usersJsonList) {
        super(origMsg.getId(), MsgGetUsers.class, from, to);
        this.userId = origMsg.userId;
        this.usersJsonList = usersJsonList;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsersJsonList() {
        return usersJsonList;
    }
}
