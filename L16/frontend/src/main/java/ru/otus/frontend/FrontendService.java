package ru.otus.frontend;
import ru.otus.l16.messageSystem.Address;
import ru.otus.l16.messages.Message;


public interface FrontendService {


    void sendMessage(Message message);

    void saveAnswer(Message msgAnswer, Message msgSource);

    Address getDbAddress();

    Message getAnswer(Message srcMsg);
}
