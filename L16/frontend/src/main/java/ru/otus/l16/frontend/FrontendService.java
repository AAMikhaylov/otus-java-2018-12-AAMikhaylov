package ru.otus.l16.frontend;

import ru.otus.l16.messageSystem.Address;
import ru.otus.l16.messages.Message;


public interface FrontendService {

    void init();

    void sendMessage(Message message);

    void saveAnswer(Message msgAnswer, Message msgSource);

    Message getAnswer(Message srcMsg);

    Address getAddress();

    Address getDbAddress();
}
