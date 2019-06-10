package ru.otus.l16.channel;

import ru.otus.l16.messages.Message;
import ru.otus.l16.workers.MsgWorker;
import ru.otus.l16.workers.SocketMsgWorker;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;


public class MsgClientChannel implements MsgChannel {
    private final static int RESTART_MIN_INTERVAL = 5000;
    private final int serverPort;
    private final String serverHost;
    private final Consumer<Message> acceptHandler;
    private final MsgWorker worker;
    private Socket socket;

    public MsgClientChannel(int serverPort, String serverHost, String socketName, Consumer<Message> acceptHandler) {
        this.serverPort = serverPort;
        this.serverHost = serverHost;
        this.acceptHandler = acceptHandler;
        worker = new SocketMsgWorker(socketName, this);
    }

    @Override
    public void start() {
        try {
            socket = new Socket(serverHost, serverPort);
            worker.start(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void restart() {
        System.out.println("Restarting server socket...");
        try {
            if (socket != null)
                socket.close();
            Thread.sleep(RESTART_MIN_INTERVAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        start();
        System.out.println("Restarting server socket complete.");
    }


    @Override
    public void join() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void send(Message msg) {
        worker.send(msg);
    }

    @Override
    public void accept(Message message) {
        acceptHandler.accept(message);
    }

    @Override
    public void close() throws IOException {
        worker.stop();
        if (socket != null)
            socket.close();
    }
}
