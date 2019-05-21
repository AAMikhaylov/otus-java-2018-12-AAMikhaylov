package ru.otus.l15.messageSystem;

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

    public MessageSystem() {
        System.out.println("Creating message system!");
        workers = new ArrayList<>();
        addresseeMap = new HashMap<>();
        messagesMap = new HashMap<>();
    }

    public void addAddressee(Addressee addressee) {
        addresseeMap.put(addressee.getAddress(), addressee);
        messagesMap.put(addressee.getAddress(), new LinkedBlockingQueue<>());
    }

    public void sendMessage(Message message) {
        LinkedBlockingQueue<Message> l = messagesMap.get(message.getTo());
        l.add(message);
    }

    public void start() {
        System.out.println("Start ms!");
        for (Map.Entry<Address, Addressee> entry : addresseeMap.entrySet()) {
            String name = "MS-worker-" + entry.getKey().getId();
            Thread thread = new Thread(() -> {
                LinkedBlockingQueue<Message> queue = messagesMap.get(entry.getKey());
                while (true) {
                    try {
                        Message message = queue.take();
                        System.out.println("Getting message!");
                        message.exec(entry.getValue());
                    } catch (InterruptedException e) {
                        logger.log(Level.INFO, "Thread interrupted. Finishing: " + name);
                        return;
                    }
                }
            });
            thread.setName(name);
            System.out.println("starting " + name);
            thread.start();
            workers.add(thread);
        }
    }

    public void dispose() {
        workers.forEach(Thread::interrupt);
    }


}
