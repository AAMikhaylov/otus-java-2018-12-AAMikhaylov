package ru.otus.l16.messageSystem.workers;

import com.google.gson.Gson;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.otus.l16.messageSystem.channel.MsgChannel;
import ru.otus.l16.messageSystem.Address;
import ru.otus.l16.messageSystem.message.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class SocketMsgWorker implements MsgWorker {
    private final static int THREADS_COUNT = 2;
    private final BlockingQueue<Message> output = new LinkedBlockingQueue<>();
    private final Logger logger;
    private final MsgChannel msgChannel;
    private ExecutorService executorTasks;
    private boolean Started;
    private final AtomicBoolean canRestart;
    private Socket socket;

    public SocketMsgWorker(Address address, MsgChannel msgChannel) {
        logger = Logger.getLogger(SocketMsgWorker.class.getName() + "." + address.getId());
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
            canRestart.set(false);
            logger.debug("Socket message worker: stopping....");
            try {
                if (!socket.isClosed())
                    socket.close();
                executorTasks.shutdownNow();
            } catch (Exception e) {
                logger.error("Socket message worker: " + ExceptionUtils.getStackTrace(e));
            }
            Started = false;
            logger.debug("Socket message worker: shutdown complete.");
        }
    }

    private void restartChannel() {
        synchronized (canRestart) {
            if (!canRestart.get())
                return;
            canRestart.set(false);
        }
        new Thread(msgChannel::restart).start();
    }

    private void sendMessage() {
        logger.debug("Socket message worker: starting Thread \"sendMessage\" on socket " + socket);
        Message msg = null;
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true, Charset.forName("UTF-8"))) {
            while (!Thread.interrupted()) {
                msg = output.take();
                String json = new Gson().toJson(msg);
                out.println(json);
                logger.trace("Socket message worker: Sending message " + json);
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

    private void receiveMessage() {
        logger.debug("Socket message worker: starting Thread \"receiveMessage\" on socket " + socket);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")))) {
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
                        logger.trace("Socket message worker: Receive message:: " + stringBuilder.toString());
                        msgChannel.accept(msg);
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
    public void send(Message message) {

        output.add(message);
    }

    @Override
    public void join() {
        try {
            executorTasks.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            logger.error("Socket message worker: " + ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    public void setCanRestart(boolean canRestart) {
        synchronized (this.canRestart) {
            this.canRestart.set(canRestart);
        }
    }

}
