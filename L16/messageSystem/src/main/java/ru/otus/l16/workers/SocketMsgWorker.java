package ru.otus.l16.workers;

import com.google.gson.Gson;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.otus.l16.channel.MsgChannel;
import ru.otus.l16.messageSystem.Address;
import ru.otus.l16.messageSystem.MessageSystem;
import ru.otus.l16.messages.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class SocketMsgWorker implements MsgWorker {
    private final static int THREADS_COUNT = 2;
    private final BlockingQueue<Message> output = new LinkedBlockingQueue<>();
    private final BlockingQueue<Message> input = new LinkedBlockingQueue<>();
    private final Logger logger;
    private final MsgChannel msgChannel;
    private ExecutorService executorTasks;
    private boolean Started;
    private final AtomicBoolean canRestart;
    private Socket socket;

    public SocketMsgWorker(String socketName, MsgChannel msgChannel) {
        logger = Logger.getLogger(SocketMsgWorker.class.getName() + "." + socketName);
        this.msgChannel = msgChannel;
        canRestart = new AtomicBoolean(false);
        Started = false;
    }

    public void start(Socket socket) {
        if (Started)
            stop();
        this.socket = socket;
        logger.debug("Socket message worker: Starting...");
        executorTasks = Executors.newFixedThreadPool(THREADS_COUNT);
        executorTasks.execute(this::receiveMessage);
        executorTasks.execute(this::sendMessage);
        Started = true;
        logger.debug("Socket message worker: Startup complete.");
        canRestart.set(true);
    }

    public void stop() {
        if (Started) {
            logger.debug("Socket message worker: stopping....");
            try {
                if (!socket.isClosed())
                    socket.close();
                executorTasks.shutdownNow();
                executorTasks.awaitTermination(1, TimeUnit.MINUTES);
            } catch (Exception e) {
                logger.error("Socket message worker: " + ExceptionUtils.getStackTrace(e));
            }
            Started = false;
            logger.debug("Socket message worker: shutdown complete.");
        }
    }

    private void sendMessage() {
        logger.debug("Socket message worker: starting Thread \"sendMessage\" on socket " + socket);
        Message msg = null;
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            while (!Thread.interrupted()) {
                msg = output.take();
                String json = new Gson().toJson(msg);
                out.println(json);
                out.println();
            }
        } catch (InterruptedException e) {
            logger.trace("Socket message worker: " + ExceptionUtils.getStackTrace(e));
        } catch (SocketException e) {
            logger.trace("Socket message worker: " + ExceptionUtils.getStackTrace(e));
            restartChannel();
            if (msg != null)
                output.add(msg);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        logger.debug("Socket message worker: Thread \"sendMessage\" on socket " + socket + " stopped.");
    }

    private void restartChannel() {
        if (!canRestart.get())
            return;
        canRestart.set(false);
        new Thread(msgChannel::restart).start();
    }

    private void receiveMessage() {
        logger.debug("Socket message worker: starting Thread \"receiveMessage\" on socket " + socket);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while (!Thread.interrupted()) {
                inputLine = in.readLine();
                if (inputLine == null) {
                    restartChannel();
                    break;
                }
                stringBuilder.append(inputLine);
                if (inputLine.isEmpty()) {
                    try {
                        Message msg = getMsgFromJSON(stringBuilder.toString());
                        input.add(msg);
                    } catch (ParseException | ClassNotFoundException e) {
                        logger.warn(e);
                        logger.trace("Socket message worker: " + ExceptionUtils.getStackTrace(e));
                    }
                    stringBuilder = new StringBuilder();
                }
            }
        } catch (SocketException e) {
            logger.trace("Socket message worker: " + ExceptionUtils.getStackTrace(e));
            restartChannel();
        } catch (Exception e) {
            logger.error("Socket message worker: " + ExceptionUtils.getStackTrace(e));
        }

        logger.debug("Socket message worker: Thread \"receiveMessage\" on socket " + socket + " stopped.");
    }

    private Message getMsgFromJSON(String json) throws ParseException, ClassNotFoundException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
        String className = (String) jsonObject.get(Message.CLASS_NAME_VARIABLE);
        Class<?> msgClass = Class.forName(className);
        return (Message) new Gson().fromJson(json, msgClass);
    }

    @Override
    public Message pool() {
        return input.poll();
    }

    @Override
    public void send(Message message) {
        output.add(message);
    }

    @Override
    public Message take() throws InterruptedException {
        return input.take();
    }
}
