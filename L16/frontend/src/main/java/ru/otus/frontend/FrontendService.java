package ru.otus.frontend;
import ru.otus.l16.messageSystem.Address;
import ru.otus.l16.messageSystem.Addressee;
import ru.otus.l16.messageSystem.Message;

public interface FrontendService extends Addressee {

    void init();

    void sendMessage(Message message);

    void saveAnswer(Message msgAnswer, Message msgSource);

    Address getDbAddress();

    Message getAnswer(Message srcMsg);
}
