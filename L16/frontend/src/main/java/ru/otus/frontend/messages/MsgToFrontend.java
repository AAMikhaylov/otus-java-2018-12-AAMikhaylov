package ru.otus.frontend.messages;

import ru.otus.frontend.FrontendService;
import ru.otus.l16.messageSystem.Address;
import ru.otus.l16.messageSystem.Addressee;
import ru.otus.l16.messageSystem.Message;

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
