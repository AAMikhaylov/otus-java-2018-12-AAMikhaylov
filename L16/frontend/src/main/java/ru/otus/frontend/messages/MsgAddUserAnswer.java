package ru.otus.frontend.messages;


import ru.otus.frontend.FrontendService;
import ru.otus.l16.messageSystem.Address;
import ru.otus.l16.messageSystem.Message;

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
