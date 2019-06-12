package ru.otus.l16.messageSystem;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import ru.otus.l16.channel.MsgChannel;
import ru.otus.l16.channel.MsgChannelServer;
import ru.otus.l16.messages.Message;

import java.io.IOException;
import java.util.HashMap;

import java.util.Map;


public class MessageSystem {

    private final Map<Address, MsgChannel> channels;
    private final Logger logger;

    public static void main(String[] args) {
        MessageSystem ms = new MessageSystem();
//        SocketClientProcess msgSocketClient =new SocketClientProcess("java -Dfile.encoding=UTF-8 -jar ../dbService/target/dbService.jar 5500", "DirCmd");
//        SocketClientProcess msgSocketClient =new SocketClientProcess("java -Dfile.encoding=UTF-8 -jar ../dbService/target/dbService.jar", "DirCmd");
//        SocketClientProcess msgSocketClient = new SocketClientProcess("dir", "DirCmd");

//        logger.info("Message system starting...");
        Address dbAddress1 = new Address("DB1");
        Address frontAddress = new Address("Frontend");
//        ms.addSocketWorker(dbAddress1, 5551);
//        ms.addClient(dbAddress1, "java -Dfile.encoding=UTF-8 -jar ../dbService/target/dbService.jar 5551");
//
//        Address dbAddress2 = new Address("DB2");
//        ms.addSocketWorker(dbAddress2, 5552);
////        ms.addClient(dbAddress2, "java -Dfile.encoding=UTF-8 -jar ../dbService/target/dbService1.jar 5552");
//        Address feAddress = new Address("FE");
//        ms.addSocketWorker(feAddress, 5553);
//        logger.info("Message system started.");
        MsgChannel channel = new MsgChannelServer(dbAddress1, 5551, "java -Dfile.encoding=UTF-8 -jar ../dbService/target/dbService.jar 5551 localhost");
        channel.setAcceptHandler(ms::routingMessage);
        ms.channels.put(dbAddress1, channel);
        channel.start();
        channel = new MsgChannelServer(frontAddress, 5555, "");
        channel.setAcceptHandler(ms::routingMessage);
        ms.channels.put(frontAddress, channel);
        channel.start();
//        logger.info("Message system: shutdown complete.");
//
//        ms.addClient(new Address("DB1"), 5551, "java -Dfile.encoding=UTF-8 -jar ../dbService/target/dbService.jar 5551");
//        ms.addClient(new Address("DB2"), 5552, "java -Dfile.encoding=UTF-8 -jar ../dbService/target/dbService.jar 5552");


//        msgWorker.close();


    }

    public MessageSystem() {
        channels = new HashMap<>();
        logger = Logger.getLogger(MessageSystem.class.getName());
    }


    public void routingMessage(Message msg) {
        MsgChannel channel = channels.get(msg.getTo());

        if (channel != null)
            channel.send(msg);
    }


    public void shutdown() {
        Thread shutdownThread = new Thread(() -> {
            channels.forEach((k, v) -> {
                try {
                    v.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
//        try {
//            Thread.sleep(60000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        shutdownThread.start();
        try {
            shutdownThread.join();
        } catch (InterruptedException e) {
            logger.error("Message system: " + ExceptionUtils.getStackTrace(e));
        }
        logger.info("Message system: shutdown complete.");
    }

}
