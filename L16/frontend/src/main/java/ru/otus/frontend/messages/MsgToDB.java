package ru.otus.frontend.messages;

import ru.otus.l16.dbService.DBService;
import ru.otus.l16.messageSystem.Address;
import ru.otus.l16.messageSystem.Addressee;
import ru.otus.l16.messageSystem.Message;

public abstract class MsgToDB extends Message {

    public MsgToDB(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof DBService)
            exec((DBService) addressee);
    }

    public abstract void exec(DBService dbService);
}

