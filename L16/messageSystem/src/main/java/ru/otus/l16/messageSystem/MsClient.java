package ru.otus.l16.messageSystem;

import ru.otus.l16.messageSystem.message.Message;

public interface MsClient {
    void sendMessage(Message message);

    void messageHandler(Message message);

}
