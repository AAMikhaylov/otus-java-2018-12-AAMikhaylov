package ru.otus.l16.messageSystem;

import org.apache.log4j.Logger;
import ru.otus.l16.app.ClientProcess;
import ru.otus.l16.channel.MsgChannel;
import ru.otus.l16.channel.MsgChannelServer;
import ru.otus.l16.client.SocketClientProcess;
import ru.otus.l16.messages.Message;

import java.io.IOException;
import java.util.HashMap;

import java.util.Map;


public class MessageSystem {

    private final Map<Address, MsgChannel> channels;
    private final Logger logger;
    private final Map<Address, ClientProcess> clientProcesses;

    public static void main(String[] args) {
        System.out.println("Working Directory = " +   System.getProperty("user.dir"));

        MessageSystem ms = new MessageSystem();
        ms.logger.info("Message system starting...");
        Address dbAddress1 = new Address("DB1");
        ClientProcess clientProcess = new SocketClientProcess(dbAddress1, "java -Dfile.encoding=UTF-8 -jar dbService/target/dbService.jar 5550 localhost");

        ms.clientProcesses.put(dbAddress1, clientProcess);
        MsgChannel channel = new MsgChannelServer(dbAddress1, 5550);
        channel.setAcceptHandler(ms::routingMessage);
        ms.channels.put(dbAddress1, channel);
        Address frontAddress = new Address("Frontend");
        channel = new MsgChannelServer(frontAddress, 5552);
        channel.setAcceptHandler(ms::routingMessage);
        ms.channels.put(frontAddress, channel);
        ms.start();
    }

    public MessageSystem() {
        channels = new HashMap<>();
        clientProcesses = new HashMap<>();
        logger = Logger.getLogger(MessageSystem.class.getName());
    }


    public void routingMessage(Message msg) {
        MsgChannel channel = channels.get(msg.getTo());

        if (channel != null)
            channel.send(msg);
    }

    public void start() {
        channels.forEach((k, v) -> v.start());
        clientProcesses.forEach((k, v) -> v.start());
    }


    public void shutdown() throws InterruptedException {
        Thread shutdownThread = new Thread(() -> {
            clientProcesses.forEach((k, v) -> v.shutdown());
            channels.forEach((k, v) -> {
                try {
                    v.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        shutdownThread.start();
        shutdownThread.join();
        logger.info("Message system: shutdown complete.");
    }

}
