package ru.otus.l16.workers;

import ru.otus.l16.messages.Message;

import java.net.Socket;

public interface MsgWorker {

    void start(Socket socket);

    void stop();

    void send(Message msg);



}
