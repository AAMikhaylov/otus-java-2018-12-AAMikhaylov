package ru.otus.l16.messages;


import ru.otus.l16.messageSystem.Address;

public class MsgAddUser extends Message {

    public MsgAddUser(Address from, Address to) {
        super(MsgAddUser.class, from, to);
    }

}
