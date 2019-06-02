package ru.otus.messageSystem;

import ru.otus.messageSystem.channel.SocketMsgWorker;
import ru.otus.messageSystem.client.MsgSocketClient;
import ru.otus.messageSystem.messages.Message;


import java.io.IOException;
import java.util.HashMap;

import java.util.Map;
import java.util.logging.Logger;

public class MessageSystem {
    private final static Logger logger = Logger.getLogger(MessageSystem.class.getName());
    private final Map<Address, SocketMsgWorker> socketWorkers;

    public static void main(String[] args) throws IOException, InterruptedException {
        MessageSystem ms = new MessageSystem();
//        MsgSocketClient msgSocketClient =new MsgSocketClient("java -Dfile.encoding=UTF-8 -jar ../dbService/target/dbService.jar 5500", "DirCmd");
//        MsgSocketClient msgSocketClient =new MsgSocketClient("java -Dfile.encoding=UTF-8 -jar ../dbService/target/dbService.jar", "DirCmd");
//        MsgSocketClient msgSocketClient = new MsgSocketClient("dir", "DirCmd");

        ms.addClient(new Address("DB"), 555, "java -Dfile.encoding=UTF-8 -jar ../dbService/target/dbService.jar");


//        msgWorker.close();
        System.out.println("start complete!");

    }

    public MessageSystem() {
        System.out.println("MS create!");
        socketWorkers = new HashMap<>();

    }

    public void routingMessage(Message msg) {
        SocketMsgWorker socketWorker = socketWorkers.get(msg.getTo());
        if (socketWorker != null)
            socketWorker.send(msg);
    }

    public void addClient(Address address, int port, String clientStartCmd) {
        SocketMsgWorker socketWorker = new SocketMsgWorker(this, port);
        socketWorkers.put(address, socketWorker);
        socketWorker.start();
        if (!clientStartCmd.isEmpty()) {
            MsgSocketClient client = new MsgSocketClient(clientStartCmd);
            client.start();
        }
    }


    public void dispose() {
        socketWorkers.forEach((k, v) -> v.close());
    }
}
