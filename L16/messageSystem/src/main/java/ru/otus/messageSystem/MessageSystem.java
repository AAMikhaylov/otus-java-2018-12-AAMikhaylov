package ru.otus.messageSystem;

import org.apache.log4j.Logger;
import ru.otus.messageSystem.channel.SocketMsgWorker;
import ru.otus.messageSystem.client.MsgSocketClient;
import ru.otus.messageSystem.messages.Message;


import java.io.IOException;
import java.util.HashMap;

import java.util.Map;


public class MessageSystem {

    private final Map<Address, SocketMsgWorker> socketWorkers;
    private final Map<Address, MsgSocketClient> socketClients;


    public static void main(String[] args) throws IOException, InterruptedException {
        MessageSystem ms = new MessageSystem();

//        MsgSocketClient msgSocketClient =new MsgSocketClient("java -Dfile.encoding=UTF-8 -jar ../dbService/target/dbService.jar 5500", "DirCmd");
//        MsgSocketClient msgSocketClient =new MsgSocketClient("java -Dfile.encoding=UTF-8 -jar ../dbService/target/dbService.jar", "DirCmd");
//        MsgSocketClient msgSocketClient = new MsgSocketClient("dir", "DirCmd");
        Logger logger = Logger.getLogger(MessageSystem.class.getName());
        logger.info("Message system starting.");
        Address dbAddress1 = new Address("DB1");
        ms.addSocketWorker(dbAddress1, 5551);
//        ms.addClient(dbAddress1, "java -Dfile.encoding=UTF-8 -jar ../dbService/target/dbService1.jar 5551");
//        Address dbAddress2 = new Address("DB2");
//        ms.addSocketWorker(dbAddress2, 5552);
//        ms.addClient(dbAddress2, "java -Dfile.encoding=UTF-8 -jar ../dbService/target/dbService1.jar 5552");
//        Address feAddress = new Address("FE");
//        ms.addSocketWorker(feAddress, 5553);
        ms.msgWorkersWaiting();
        logger.info("Shutdown complete.");

//
//        ms.addClient(new Address("DB1"), 5551, "java -Dfile.encoding=UTF-8 -jar ../dbService/target/dbService.jar 5551");
//        ms.addClient(new Address("DB2"), 5552, "java -Dfile.encoding=UTF-8 -jar ../dbService/target/dbService.jar 5552");


//        msgWorker.close();


    }

    public MessageSystem() {
        socketWorkers = new HashMap<>();
        socketClients = new HashMap<>();

    }

    private void msgWorkersWaiting() {
        socketWorkers.forEach((k, v) -> v.join());

    }

    public void routingMessage(Message msg) {
        SocketMsgWorker socketWorker = socketWorkers.get(msg.getTo());

        if (socketWorker != null)
            socketWorker.send(msg);
    }

    public void addSocketWorker(Address address, int port) {
        SocketMsgWorker socketWorker = new SocketMsgWorker(this, port, address);
        socketWorkers.put(address, socketWorker);
        socketWorker.start();
    }

    public void addClient(Address address, String clientStartCmd) {
        MsgSocketClient client = new MsgSocketClient(address, clientStartCmd);
        socketClients.put(address, client);
        client.start();
    }

    public void dispose() {
        Thread shutdownThread = new Thread(() -> {
            socketClients.forEach((k, v) -> v.destroy());
            socketWorkers.forEach((k, v) -> v.close());
        });
        shutdownThread.start();
    }

}
