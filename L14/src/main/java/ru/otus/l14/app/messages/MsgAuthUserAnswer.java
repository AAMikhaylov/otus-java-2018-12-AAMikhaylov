package ru.otus.l14.app.messages;

import ru.otus.l14.app.FrontendService;
import ru.otus.l14.app.MsgToFrontend;
import ru.otus.l14.messageSystem.Address;

public class MsgAuthUserAnswer extends MsgToFrontend {
    private final boolean authResult;
    private final String msgSourceId;


    public MsgAuthUserAnswer(Address from, Address to, boolean authResult, String msgSourceId) {
        super(from, to);
        this.authResult = authResult;
        this.msgSourceId = msgSourceId;
    }

    @Override
    public void exec(FrontendService frontendService) {
        frontendService.authenticateResp(authResult,msgSourceId);
    }
}
