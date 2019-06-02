package ru.otus.messageSystem.messages;


import ru.otus.messageSystem.Address;

public class MsgAddUser extends Message {

    public MsgAddUser(Address from, Address to) {
        super(MsgAddUser.class, from, to);
    }

}
