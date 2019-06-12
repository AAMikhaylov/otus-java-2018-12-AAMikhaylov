package ru.otus.l16.messages;

import ru.otus.l16.messageSystem.Address;

public class MsgGetUsersCount extends Message {
    private final Integer count;

    public MsgGetUsersCount(Address from, Address to) {
        super(MsgGetUsersCount.class, from, to);
        count = null;

    }

    public MsgGetUsersCount(Address from, Address to, int count) {
        super(MsgGetUsersCount.class, from, to);
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }
}
