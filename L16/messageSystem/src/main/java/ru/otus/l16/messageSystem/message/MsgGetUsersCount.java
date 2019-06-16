package ru.otus.l16.messageSystem.message;

import ru.otus.l16.messageSystem.Address;

public class MsgGetUsersCount extends Message {
    private final String count;

    public MsgGetUsersCount(Address from, Address to) {
        super(MsgGetUsersCount.class, from, to);
        count = null;
    }

    public MsgGetUsersCount(MsgGetUsersCount origMsg, Address from, Address to, String count) {
        super(origMsg.getId(), MsgGetUsersCount.class, from, to);
        this.count = count;
    }

    public String getCount() {
        return count;
    }

}
