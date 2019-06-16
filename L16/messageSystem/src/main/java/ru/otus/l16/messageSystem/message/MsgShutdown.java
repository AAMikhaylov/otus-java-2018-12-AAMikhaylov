package ru.otus.l16.messageSystem.message;

import ru.otus.l16.messageSystem.Address;

public class MsgShutdown extends Message {
    private final boolean stoppedDbService;

    public MsgShutdown(Address from, Address to) {
        super(MsgShutdown.class, from, to);
        stoppedDbService = false;
    }

    public MsgShutdown(MsgShutdown origMsg, Address from, Address to, boolean stoppedDbService) {
        super(origMsg.getId(), MsgShutdown.class, from, to);
        this.stoppedDbService = stoppedDbService;
    }

    public boolean isStoppedDbService() {
        return stoppedDbService;
    }
}
