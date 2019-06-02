package ru.otus.messageSystem.app;

import ru.otus.messageSystem.messages.Message;

import java.io.Closeable;

public interface MsgWorker extends Closeable {
    void send(Message msg);

    void close();
}
