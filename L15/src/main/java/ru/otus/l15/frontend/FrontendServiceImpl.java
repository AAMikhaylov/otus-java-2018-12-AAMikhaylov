package ru.otus.l15.frontend;

import ru.otus.l15.app.*;

import ru.otus.l15.messageSystem.Address;
import ru.otus.l15.messageSystem.Message;
import ru.otus.l15.messageSystem.MessageSystemContext;
import ru.otus.l15.messageSystem.MessageSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class FrontendServiceImpl implements FrontendService {


    private final MessageSystemContext context;
    private final Address address;
    private final Map<Message, LinkedBlockingQueue<Message>> answers = new HashMap<>();

    public FrontendServiceImpl(MessageSystemContext context, Address address) {
        this.context = context;
        this.address = address;
    }

    @Override
    public Message getAnswer(Message msgSource) {
        LinkedBlockingQueue<Message> queue = answers.get(msgSource);
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveAnswer(Message msgAnswer, Message msgSource) {
        LinkedBlockingQueue<Message> queue = answers.get(msgSource);
        queue.add(msgAnswer);
    }

    @Override
    public void sendMessage(Message message) {
        answers.put(message, new LinkedBlockingQueue<>());
        getMS().sendMessage(message);
    }

    @Override
    public void init() {
        context.getMessageSystem().addAddressee(this);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public Address getDbAddress() {
        return context.getDbAddress();
    }

    @Override
    public MessageSystem getMS() {
        return context.getMessageSystem();
    }

}
