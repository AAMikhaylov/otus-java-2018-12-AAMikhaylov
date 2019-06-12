package ru.otus.l16.frontend;

import ru.otus.l16.channel.MsgChannel;

import ru.otus.l16.messageSystem.Address;
import ru.otus.l16.messages.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class FrontendServiceImpl implements FrontendService {

    private final Map<Message, LinkedBlockingQueue<Message>> answers;
    private final MsgChannel msgChannel;
    private final Address address;
    private final Address[] dbAddresses;
    private int addrIdx = -1;

    public FrontendServiceImpl(MsgChannel msgChannel, Address address, Address... dbAddresses) {
        this.msgChannel = msgChannel;
        this.address = address;
        this.dbAddresses = dbAddresses;
        answers = new HashMap<>();
    }

    private void accept(Message message) {
        LinkedBlockingQueue<Message> queue = answers.get(message);
        queue.add(message);
    }


    @Override
    public void init() {
        msgChannel.setAcceptHandler(this::accept);
        msgChannel.start();
    }


    @Override
    public Message getAnswer(Message msgSource) {
        LinkedBlockingQueue<Message> queue = answers.get(msgSource);
        if (queue != null)
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
        msgChannel.send(message);
    }

    @Override
    public Address getDbAddress() {
        if (dbAddresses.length == 0)
            throw new IndexOutOfBoundsException("dbAddresses array is undefined.");
        addrIdx++;
        if (addrIdx > dbAddresses.length - 1)
            addrIdx = 0;
        return dbAddresses[addrIdx];
    }

    @Override
    public Address getAddress() {
        return address;
    }

}
