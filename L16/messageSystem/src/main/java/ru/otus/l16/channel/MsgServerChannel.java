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

public class MsgServerChannel implements MsgChannel {
    private final int RESTART_MIN_INTERVAL = 5000;
    private final int port;
    private final Logger logger;
    private final Thread routingThread;
    private final MessageSystem ms;
    private ServerSocket serverSocket;
    private final MsgWorker worker;
    private final ClientProcess clientProcess;
    private boolean started = false;

    public MsgServerChannel(MessageSystem ms, Address address, int port, String startClientCommand) {
        logger = Logger.getLogger(MsgServerChannel.class.getName() + "." + address.getId());
        this.port = port;
        this.ms = ms;
        worker = new SocketMsgWorker(address.getId(), this);
        routingThread = new Thread(this::routingMsg);
        clientProcess = new SocketClientProcess(address, startClientCommand);
    }

    private void routingMsg() {
        try {
            while (!routingThread.isInterrupted()) {
                Message msg = take();
                ms.routingMessage(msg);
            }
        } catch (InterruptedException e) {
            logger.trace(ExceptionUtils.getStackTrace(e));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
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
        routingThread.start();
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

        try {
            routingThread.join();
        } catch (InterruptedException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    public void close() throws IOException {
        worker.stop();
        routingThread.interrupt();
        serverSocket.close();
    }

    @Override
    public void send(Message msg) {
        worker.send(msg);
    }

    @Override
    public Message take() throws InterruptedException {
        return worker.take();

    }
}
