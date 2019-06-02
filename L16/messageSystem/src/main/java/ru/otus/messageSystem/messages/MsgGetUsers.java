package ru.otus.messageSystem.messages;

import ru.otus.messageSystem.Address;

public class MsgGetUsers extends Message {

    public MsgGetUsers(Address from, Address to) {
        super(MsgGetUsers.class, from, to);
    }
}
