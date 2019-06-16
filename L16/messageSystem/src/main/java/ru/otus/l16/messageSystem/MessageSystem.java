package ru.otus.l16.messageSystem;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import ru.otus.l16.messageSystem.clientProcess.ClientProcess;
import ru.otus.l16.messageSystem.channel.MsgChannel;
import ru.otus.l16.messageSystem.channel.MsgChannelServer;
import ru.otus.l16.messageSystem.clientProcess.ClientProcessImpl;
import ru.otus.l16.messageSystem.message.Message;
import ru.otus.l16.messageSystem.message.MsgShutdown;

import java.io.IOException;
import java.util.HashMap;

import java.util.Map;
import java.util.concurrent.*;


public class MessageSystem {
    private final int TIME_BEFORE_CLOSE_CHAN = 1;
    private final Map<Address, MsgChannel> channels;
    private final Logger logger;
    private final Map<Address, ClientProcess> clientProcesses;

    public static void main(String[] args) {
        MessageSystem ms = new MessageSystem();
        ms.logger.info("Message system starting...");
        Address address = new Address("DB1");
        ClientProcess clientProcess = new ClientProcessImpl(address, "java -Dfile.encoding=UTF-8 -jar dbService/target/dbService.jar address=DB1 serverHost=localhost serverPort=5550 createSchema=true");
        ms.clientProcesses.put(address, clientProcess);
        MsgChannel channel = new MsgChannelServer(address, 5550);
        channel.setAcceptHandler(ms::routingMessage);
        ms.channels.put(address, channel);
        address = new Address("DB2");
        clientProcess = new ClientProcessImpl(address, "java -Dfile.encoding=UTF-8 -jar dbService/target/dbService.jar address=DB2 serverHost=localhost serverPort=5551 createSchema=false");
        ms.clientProcesses.put(address, clientProcess);
        channel = new MsgChannelServer(address, 5551);
        channel.setAcceptHandler(ms::routingMessage);
        ms.channels.put(address, channel);
        address = new Address("Frontend");
        channel = new MsgChannelServer(address, 5552);
        channel.setAcceptHandler(ms::routingMessage);
        ms.channels.put(address, channel);
        ms.start();
    }

    public MessageSystem() {
        channels = new HashMap<>();
        clientProcesses = new HashMap<>();
        logger = Logger.getLogger(MessageSystem.class.getName());
    }


    public void routingMessage(Message msg) {
        if (msg.getClass().equals(MsgShutdown.class)) {
            channels.forEach((k, v) -> {
                v.send(msg);
                v.setCanRestart(false);
            });
            try {
                Thread.sleep(TIME_BEFORE_CLOSE_CHAN);
            } catch (InterruptedException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
            shutdown();
            return;
        }
        MsgChannel channel = channels.get(msg.getTo());
        if (channel != null)
            channel.send(msg);
    }

    public void start() {
        channels.forEach((k, v) -> v.start());
        clientProcesses.forEach((k, v) -> v.start());
    }


    public void shutdown() {
        Thread shutdownThread = new Thread(() -> {
            channels.forEach((k, v) -> {
                try {
                    v.close();
                } catch (IOException e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
            });
            clientProcesses.forEach((k, v) -> v.shutdown());
        });
        shutdownThread.start();

    }
}
