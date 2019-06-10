package ru.otus.l16.channel;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import ru.otus.l16.app.ClientProcess;
import ru.otus.l16.client.SocketClientProcess;
import ru.otus.l16.messageSystem.Address;
import ru.otus.l16.messageSystem.MessageSystem;
import ru.otus.l16.messages.Message;
import ru.otus.l16.workers.MsgWorker;
import ru.otus.l16.workers.SocketMsgWorker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;

public class MsgServerChannel implements MsgChannel {
    private final static int RESTART_MIN_INTERVAL = 5000;
    private final int port;
    private final Logger logger;
    private final Consumer<Message> acceptHandler;

    private ServerSocket serverSocket;
    private final MsgWorker worker;
    private final ClientProcess clientProcess;
    private boolean started = false;


    public MsgServerChannel(Address address, int port, String startClientCommand, Consumer<Message> acceptHandler) {
        logger = Logger.getLogger(MsgServerChannel.class.getName() + "." + address.getId());
        this.port = port;
        this.acceptHandler=acceptHandler;
        worker = new SocketMsgWorker(address.getId(), this);
        clientProcess = new SocketClientProcess(address, startClientCommand);

    }
    private void openServerSocket() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            this.serverSocket = serverSocket;
            logger.info("Listening port " + port);
            Socket socket = serverSocket.accept();
            worker.start(socket);
            logger.info("Connection to port " + port + " success!");
        } catch (SocketException e) {
            logger.trace(ExceptionUtils.getStackTrace(e));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }
    @Override
    public void start() {
        if (started) {
            logger.error("Can't be restarted!");
            return;
        }
        openServerSocket();
        clientProcess.start();
        started = true;
    }

    @Override
    public void restart() {
        logger.info("Restarting server socket...");
        try {
            if (serverSocket != null)
                serverSocket.close();
            Thread.sleep(RESTART_MIN_INTERVAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        openServerSocket();
        logger.info("Restarting server socket complete.");
    }

    @Override
    public void join() {

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

    public void accept( Message message) {
        acceptHandler.accept(message);
    }
}
