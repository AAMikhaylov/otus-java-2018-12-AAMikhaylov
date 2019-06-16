package ru.otus.l16.messageSystem.channel;

import ru.otus.l16.messageSystem.message.Message;

import java.io.Closeable;
import java.util.function.Consumer;

public interface MsgChannel extends Closeable {

    void start();

    void restart();

    void send(Message msg);

    void accept(Message message);

    void setAcceptHandler(Consumer<Message> acceptHandler);

    void setCanRestart(boolean canRestart);

}
