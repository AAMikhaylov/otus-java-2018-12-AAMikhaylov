package ru.otus.l16.messages;

import ru.otus.l16.messageSystem.Address;

public class MsgAuthUser extends Message {
    public MsgAuthUser(Address from, Address to) {
        super(MsgAuthUser.class,from, to);
    }
}