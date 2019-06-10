package ru.otus.l16.channel;

import ru.otus.l16.messages.Message;
import ru.otus.l16.workers.MsgWorker;
import ru.otus.l16.workers.SocketMsgWorker;

import java.io.IOException;
import java.net.Socket;

public class MsgClientChannel implements MsgChannel {
    private final int serverPort;
    private final String serverHost;
    private final MsgWorker worker;
    public MsgClientChannel(int serverPort, String serverHost, String socketName) {
        this.serverPort = serverPort;
        this.serverHost = serverHost;
        worker = new SocketMsgWorker(socketName);

    }

    @Override
    public void start() {
        try {
            Socket socket = new Socket(serverHost, serverPort);
            worker.start(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void join() {


    }

    @Override
    public void send(Message msg) {

    }

    @Override
    public Message take() {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
