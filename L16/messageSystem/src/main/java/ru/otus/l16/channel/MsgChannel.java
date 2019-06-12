package ru.otus.l16.channel;

import ru.otus.l16.messages.Message;

import java.io.Closeable;
import java.util.function.Consumer;

public interface MsgChannel extends Closeable {

    void start();

    void restart();

    void send(Message msg);

    void accept(Message message);

    void setAcceptHandler(Consumer<Message> acceptHandler);
}
