package ru.otus.messageSystem.messages;

import ru.otus.messageSystem.Address;

public class MsgGetUsersCount extends Message {
    public MsgGetUsersCount(Address from, Address to) {
        super(MsgGetUsersCount.class, from, to);
    }

}
