package ru.otus.frontend;
import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.Addressee;
import ru.otus.messageSystem.Message;

public interface FrontendService extends Addressee {

    void init();

    void sendMessage(Message message);

    void saveAnswer(Message msgAnswer, Message msgSource);

    Address getDbAddress();

    Message getAnswer(Message srcMsg);
}
