package ru.otus.l16.channel;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import ru.otus.l16.app.ClientProcess;
import ru.otus.l16.client.SocketClientProcess;
import ru.otus.l16.messageSystem.Address;
import ru.otus.l16.messages.Message;
import ru.otus.l16.workers.MsgWorker;
import ru.otus.l16.workers.SocketMsgWorker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;

public class MsgChannelServer implements MsgChannel {
    private final static int RESTART_MIN_INTERVAL = 5000;
    private final int port;
    private final Logger logger;
    private Consumer<Message> acceptHandler;

    private ServerSocket serverSocket;
    private final MsgWorker worker;

    private boolean started = false;


    public MsgChannelServer(Address address, int port) {
        logger = Logger.getLogger(MsgChannelServer.class.getName() + "." + address.getId());
        this.port = port;
        worker = new SocketMsgWorker(address, this);
    }

    private void openServerSocket() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            this.serverSocket = serverSocket;
            logger.info("Server channel: Listening port " + port);
            Socket socket = serverSocket.accept();
            worker.start(socket);
            logger.info("Server channel: Connection to port " + port + " success!");
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    public void start() {
        if (acceptHandler == null) {
            throw new NullPointerException("Server channel: Undefined accept messages handler");
        }
        if (started) {
            logger.error("Server channel: Can't be restarted!");
            return;
        }
        new Thread(this::openServerSocket).start();
        started = true;
    }

    @Override
    public void restart() {
        logger.info("Server channel: Restarting server socket...");
        try {
            if (serverSocket != null)
                serverSocket.close();
            Thread.sleep(RESTART_MIN_INTERVAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        openServerSocket();
        logger.info("Server channel: Restarting server socket complete.");
    }

    @Override
    public void close() throws IOException {
        worker.stop();
        serverSocket.close();
    }

    @Override
    public void send(Message msg) {
        worker.send(msg);
    }

    public void accept(Message message) {
        acceptHandler.accept(message);
    }

    @Override
    public void setAcceptHandler(Consumer<Message> acceptHandler) {
        this.acceptHandler = acceptHandler;
    }

}
