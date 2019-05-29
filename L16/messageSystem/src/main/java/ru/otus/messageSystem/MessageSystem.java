package ru.otus.messageSystem;

import ru.otus.messageSystem.client.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageSystem {
    private final static Logger logger = Logger.getLogger(MessageSystem.class.getName());
    private final List<Thread> workers;
    private final Map<Address, Addressee> addresseeMap;
    private final Map<Address, LinkedBlockingQueue<Message>> messagesMap;

    public static void main(String[] args) throws IOException, InterruptedException {
//        MessageSystem ms = new MessageSystem();
        Client client =new Client("java -jar ../dbService/target/dbService.jar", "DirCmd");
//        Client client =new Client("dir", "DirCmd");
        client.start();
        Thread.sleep(5000);
        client.destroy();
        client.start();
        Thread.sleep(5000);
        client.destroy();
    }

    public MessageSystem() {
        System.out.println("MS create!");
        workers = new ArrayList<>();
        addresseeMap = new HashMap<>();
        messagesMap = new HashMap<>();
    }

    public void addAddressee(Addressee addressee) {
        System.out.println("Adding addressee");
        addresseeMap.put(addressee.getAddress(), addressee);
        LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();
        messagesMap.put(addressee.getAddress(), queue);
        String name = "MS-worker-" + addressee.getAddress().getId();
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Message message = queue.take();
                    message.exec(addressee);
                } catch (InterruptedException e) {
                    logger.log(Level.INFO, "Thread interrupted. Finishing: " + name);
                    return;
                }
            }
        });
        thread.setName(name);
        thread.start();
        workers.add(thread);
    }

    public void sendMessage(Message message) {
        LinkedBlockingQueue<Message> l = messagesMap.get(message.getTo());
        l.add(message);
    }


    public void dispose() {
        workers.forEach(Thread::interrupt);
    }
}
