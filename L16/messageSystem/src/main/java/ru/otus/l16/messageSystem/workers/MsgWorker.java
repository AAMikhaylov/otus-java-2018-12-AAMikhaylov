package ru.otus.l16.messageSystem.workers;

import ru.otus.l16.messageSystem.message.Message;

import java.net.Socket;

public interface MsgWorker {

    void start(Socket socket);

    void stop();

    void send(Message msg);

    void join();

    void setCanRestart(boolean canRestart);


}
