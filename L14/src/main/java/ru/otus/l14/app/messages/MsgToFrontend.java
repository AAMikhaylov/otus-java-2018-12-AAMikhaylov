package ru.otus.l14.app.messages;

import ru.otus.l14.app.FrontendService;
import ru.otus.l14.messageSystem.Address;
import ru.otus.l14.messageSystem.Addressee;
import ru.otus.l14.messageSystem.Message;

public abstract class MsgToFrontend extends Message {
    public MsgToFrontend(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof FrontendService)
            exec((FrontendService) addressee);
    }

    public abstract void exec(FrontendService frontendService);
}
