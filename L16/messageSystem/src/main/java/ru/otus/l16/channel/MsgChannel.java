package ru.otus.l16.channel;

import ru.otus.l16.messages.Message;

import java.io.Closeable;

public interface MsgChannel extends Closeable {

    void start();

    void restart();

    void join();

    void send(Message msg);

    Message take() throws InterruptedException;
}
