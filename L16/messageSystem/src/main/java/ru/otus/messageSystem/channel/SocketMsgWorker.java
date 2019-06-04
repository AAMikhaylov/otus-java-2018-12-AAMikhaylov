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
import java.util.*;
import java.util.concurrent.*;


public class SocketMsgWorker implements MsgWorker {
    private final static int THREADS_COUNT = 2;
    private final int port;
    private final MessageSystem messageSystem;
    private final BlockingQueue<Message> output = new LinkedBlockingQueue<>();
    private final Logger logger;
    private final Thread serverSocketThread;
    private final Set<Callable<Integer>> tasks = new HashSet<>();
    private List<Future<Integer>> futures;
    private ExecutorService executorTasks;
    private boolean tasksStarted;

//    private final MsgWorkerTasks msgWorkerTasks;

    private ServerSocket serverSocket;
    private Socket socket;


    public SocketMsgWorker(MessageSystem messageSystem, int port, Address address) {
        this.port = port;
        this.messageSystem = messageSystem;
        serverSocketThread = new Thread(this::openServerSocket);
        logger = Logger.getLogger(SocketMsgWorker.class.getName() + "." + address.getId());
        tasks.add(this::sendMessage);
        tasks.add(this::receiveMessage);
        tasksStarted = false;

//        msgWorkerTasks = new MsgWorkerTasks(this::sendMessage, this::receiveMessage, address);

    }

    private void startTasks() {
        if (!tasksStarted) {
            futures = new ArrayList<>(THREADS_COUNT);
            executorTasks = Executors.newFixedThreadPool(THREADS_COUNT);
            for (Callable<Integer> task : tasks) {
                Future<Integer> future = executorTasks.submit(task);
                futures.add(future);
            }
            tasksStarted = true;
        }
    }

    private void shutdownTasks() {
        if (tasksStarted) {
            try {
                socket.shutdownInput();
//                socket.shutdownOutput();
                executorTasks.shutdownNow();
                executorTasks.awaitTermination(1, TimeUnit.MINUTES);
            } catch (IOException | InterruptedException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
            tasksStarted = false;
        }
    }

    public void start() {
        serverSocketThread.start();
    }

    private void openServerSocket() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            this.serverSocket = serverSocket;
            logger.info("Listening port " + port);
            while (!serverSocket.isClosed()) {
                Socket newSocket = serverSocket.accept();
                if (tasksStarted)
                    shutdownTasks();
                socket = newSocket;
                startTasks();
                logger.info("Connection to port " + port + " success!");
            }
        } catch (IOException e) {
            logger.trace(ExceptionUtils.getStackTrace(e));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }

    }

    private Integer sendMessage() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            while (!Thread.interrupted()) {
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
        return 0;
    }

    private Integer receiveMessage() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while (!socket.isInputShutdown() && (inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
                if (inputLine.isEmpty()) {
                    try {
                        if (stringBuilder.toString().trim().equals("shutdown")) {
                            messageSystem.dispose();
                        } else {
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
        return 0;
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
    public void join() {
        try {
            serverSocketThread.join();
        } catch (InterruptedException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    public void close() {
        try {
            shutdownTasks();

//            socket.close();

//            msgWorkerTasks.destroy();
            serverSocket.close();
            logger.info("Shutdown complete.");
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
