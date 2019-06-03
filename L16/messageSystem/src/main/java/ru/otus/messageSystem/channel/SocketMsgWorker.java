package ru.otus.messageSystem.channel;

import com.google.gson.Gson;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.MessageSystem;
import ru.otus.messageSystem.messages.Message;
import ru.otus.messageSystem.app.MsgWorker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;


public class SocketMsgWorker implements MsgWorker {
    private final static int THREADS_COUNT = 2;
    private final int port;
    private final MessageSystem messageSystem;
    private final BlockingQueue<Message> output = new LinkedBlockingQueue<>();
    private final Logger logger;
    private final Thread mainThread;
    private ServerSocket serverSocket;
    private Socket socket;
    private ExecutorService executor;

    public SocketMsgWorker(MessageSystem messageSystem, int port, Address address) {
        this.port = port;
        this.messageSystem = messageSystem;
        mainThread = new Thread(this::openServerSocket);
        logger = Logger.getLogger(SocketMsgWorker.class.getName() + "." + address.getId());
    }

    public void start() {
        mainThread.start();
    }

    private void startMsgTasks() {
        executor = Executors.newFixedThreadPool(THREADS_COUNT);
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }

    private void stopMsgTasks() {
        try {
            if (socket != null && !socket.isClosed()&&!executor.isTerminated()) {
                socket.shutdownInput();
                socket.shutdownOutput();
                executor.shutdownNow();
                executor.awaitTermination(1, TimeUnit.MINUTES);
            }
        } catch (IOException | InterruptedException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    private void openServerSocket() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            this.serverSocket = serverSocket;
            logger.info("Listening port " + port);
            while (!serverSocket.isClosed()) {
                Socket newSocket = serverSocket.accept();
                stopMsgTasks();
                socket=newSocket;
                startMsgTasks();
                logger.info("Connection to port " + port + " success!");
            }
        } catch (IOException e) {
            logger.trace(ExceptionUtils.getStackTrace(e));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }

    }

    private void sendMessage() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            while (!executor.isShutdown() && !socket.isClosed()) {
                Message msg = output.take();
                String json = new Gson().toJson(msg);
                out.println(json);
                out.println();
            }
        } catch (IOException | InterruptedException e) {
            logger.trace(ExceptionUtils.getStackTrace(e));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    private void receiveMessage() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while (!executor.isShutdown() && !socket.isClosed() && (inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
                if (inputLine.isEmpty()) {
                    try {
                        if (stringBuilder.toString().trim().equals("shutdown"))
                            messageSystem.dispose();
                        else {
                            Message msg = getMsgFromJSON(stringBuilder.toString());
                            messageSystem.routingMessage(msg);
                        }
                    } catch (ParseException | ClassNotFoundException e) {
                        logger.warn(ExceptionUtils.getStackTrace(e));
                    }
                    stringBuilder = new StringBuilder();
                }
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    private Message getMsgFromJSON(String json) throws ParseException, ClassNotFoundException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
        String className = (String) jsonObject.get(Message.CLASS_NAME_VARIABLE);
        Class<?> msgClass = Class.forName(className);
        return (Message) new Gson().fromJson(json, msgClass);
    }

    @Override
    public void send(Message msg) {
        output.add(msg);
    }

    @Override
    public void join() throws InterruptedException {
        mainThread.join();
    }

    @Override
    public void close() {
        try {
            stopMsgTasks();
            serverSocket.close();
            logger.info("Shutdown complete.");
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
