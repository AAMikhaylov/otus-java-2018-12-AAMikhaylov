package ru.otus.l14.app;

import ru.otus.l14.messageSystem.Address;
import ru.otus.l14.messageSystem.Addressee;
import ru.otus.l14.messageSystem.Message;


public interface FrontendService extends Addressee {

    void init();

    void sendMessage(Message message);

    void saveAnswer(Message msgAnswer, Message msgSource);

    Address getDbAddress();

    Message getAnswer(Message srcMsg);
}
