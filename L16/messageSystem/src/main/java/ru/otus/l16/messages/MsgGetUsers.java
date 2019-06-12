package ru.otus.l16.messages;

import ru.otus.l16.messageSystem.Address;

public class MsgGetUsers extends Message {
    private final String userId;
    private final String usersJsonList;

    public MsgGetUsers(Address from, Address to, String userId, String usersJsonList) {
        super(MsgGetUsers.class, from, to);
        this.userId = userId;
        this.usersJsonList = usersJsonList;
    }

    public String getUsersJsonList() {
        return usersJsonList;
    }
}
