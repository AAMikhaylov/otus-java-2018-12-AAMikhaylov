package ru.otus.messageSystem.messages;

import ru.otus.messageSystem.Address;

public class MsgAuthUser extends Message {
    public MsgAuthUser(Address from, Address to) {
        super(MsgAuthUser.class,from, to);
    }
}