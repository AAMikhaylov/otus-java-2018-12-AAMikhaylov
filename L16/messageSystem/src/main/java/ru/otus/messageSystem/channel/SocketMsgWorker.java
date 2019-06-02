package ru.otus.messageSystem.channel;

import com.google.gson.Gson;
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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketMsgWorker implements MsgWorker {
    private final static int THREAD_COUNT = 3;
    private final int port;
    private final MessageSystem messageSystem;
    private final BlockingQueue<Message> output = new LinkedBlockingQueue<>();
    private ServerSocket serverSocket;
    private Socket socket;

    private ExecutorService executor;

    public SocketMsgWorker(MessageSystem messageSystem, int port) {
        this.port = port;
        this.messageSystem = messageSystem;
        executor = Executors.newFixedThreadPool(THREAD_COUNT);
    }

    public void start() {
        executor.execute(this::openServerSocket);


    }

    private void openServerSocket() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            this.serverSocket = serverSocket;
            while (!executor.isShutdown()) {
                socket = serverSocket.accept();
                executor.execute(this::sendMessage);
                executor.execute(this::receiveMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            while (socket.isConnected()) {
                Message msg = output.take();
                String json = new Gson().toJson(msg);
                out.println(json);
                out.println();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receiveMessage() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
                if (inputLine.isEmpty()) {
                    try {
                        Message msg = getMsgFromJSON(stringBuilder.toString());
                        messageSystem.routingMessage(msg);
                    } catch (ParseException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    stringBuilder = new StringBuilder();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
    public void close() {
        executor.shutdown();
        System.out.println("Shutdown!");
        if (serverSocket != null)
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
