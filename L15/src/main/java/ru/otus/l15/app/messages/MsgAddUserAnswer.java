package ru.otus.l15.app.messages;

import ru.otus.l15.app.FrontendService;
import ru.otus.l15.messageSystem.Address;
import ru.otus.l15.messageSystem.Message;

public class MsgAddUserAnswer extends MsgToFrontend {
    private final Message msgSource;

    public MsgAddUserAnswer(Address from, Address to, Message msgSource) {
        super(from, to);
        this.msgSource = msgSource;
    }

    @Override
    public void exec(FrontendService frontendService) {
        frontendService.saveAnswer(this, msgSource);
    }
}
