package ru.otus.l16.messages;

import ru.otus.l16.messageSystem.Address;

public class MsgGetUsersCount extends Message {
    public MsgGetUsersCount(Address from, Address to) {
        super(MsgGetUsersCount.class, from, to);
    }

}
