package ru.otus.l16.messageSystem.channel;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import ru.otus.l16.messageSystem.Address;
import ru.otus.l16.messageSystem.message.Message;
import ru.otus.l16.messageSystem.workers.MsgWorker;
import ru.otus.l16.messageSystem.workers.SocketMsgWorker;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.function.Consumer;


public class MsgChannelClient implements MsgChannel {
    private final static int RESTART_MIN_INTERVAL = 5000;
    private final int serverPort;
    private final String serverHost;
    private Consumer<Message> acceptHandler;
    private final MsgWorker worker;
    private Socket socket;
    private boolean closed = false;
    private boolean canRestart = true;
    private final Logger logger;


    public MsgChannelClient(Address address, String serverHost, int serverPort) {
        logger = Logger.getLogger(MsgChannelClient.class.getName() + "." + address.getId());
        this.serverPort = serverPort;
        this.serverHost = serverHost;
        worker = new SocketMsgWorker(address, this);

    }

    @Override
    public void start() {
        logger.trace("Client channel: Starting...");

        if (acceptHandler == null) {
            throw new NullPointerException("Client channel: Undefined accept message handler");
        }
        try {
            while (!closed) {
                try {
                    socket = new Socket(serverHost, serverPort);
                    break;
                } catch (ConnectException e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                    Thread.sleep(RESTART_MIN_INTERVAL);
                }
            }
            logger.info("Client channel: Connection to " + serverHost + ":" + serverPort + " successfull.");
            worker.start(socket);
        } catch (IOException | InterruptedException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        logger.trace("Client channel: Startup complete...");
    }

    @Override
    public void restart() {
        if (!canRestart)
            return;
        logger.info("Client channel: Restarting clientProcess socket...");
        try {
            if (socket != null)
                socket.close();
            Thread.sleep(RESTART_MIN_INTERVAL);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        start();
        logger.info("Client channel: Restarting clientProcess socket complete.");
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
        closed = true;
        worker.stop();
        if (socket != null)
            socket.close();
        logger.info("Client channel: Message channel closed.");
    }

    @Override
    public void setAcceptHandler(Consumer<Message> acceptHandler) {
        this.acceptHandler = acceptHandler;
    }

    @Override
    public void setCanRestart(boolean canRestart) {
        this.canRestart = canRestart;
        worker.setCanRestart(canRestart);
    }
}
