package ru.otus.l15.app.messages;

import ru.otus.l15.app.FrontendService;
import ru.otus.l15.messageSystem.Address;
import ru.otus.l15.messageSystem.Message;

public class MsgAuthUserAnswer extends MsgToFrontend {
    private final Message msgSource;
    private final Boolean userAuthResult;


    public MsgAuthUserAnswer(Address from, Address to, Boolean userAuthResult, Message msgSource) {
        super(from, to);
        this.msgSource = msgSource;
        this.userAuthResult = userAuthResult;
    }

    public Boolean isAuth() {
        return userAuthResult;
    }

    @Override
    public void exec(FrontendService frontendService) {
        frontendService.saveAnswer(this, msgSource);
    }
}
