package ru.otus.l16.messages;

import ru.otus.l16.messageSystem.Address;

public class MsgGetUsers extends Message {

    public MsgGetUsers(Address from, Address to) {
        super(MsgGetUsers.class, from, to);
    }
}
