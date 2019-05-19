package ru.otus.l15.frontend;

import ru.otus.l15.app.*;

import ru.otus.l15.messageSystem.Address;
import ru.otus.l15.messageSystem.Message;
import ru.otus.l15.messageSystem.MessageSystemContext;
import ru.otus.l15.messageSystem.MessageSystem;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FrontendServiceImpl implements FrontendService {


    private final MessageSystemContext context;
    private final Address address;
    private final ConcurrentMap<Message, Message> answers = new ConcurrentHashMap<>();

    public FrontendServiceImpl(MessageSystemContext context, Address address) {
        this.context = context;
        this.address = address;
    }


    @Override
    public Message getAnswer(Message srcMsg) {
        Message answer = null;
        while (true)
            synchronized (srcMsg) {
                try {
                    answer = answers.get(srcMsg);
                    if (answer == null)
                        srcMsg.wait();
                    else break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        return answer;
    }

    @Override
    public void saveAnswer(Message msgAnswer, Message msgSource) {
        synchronized (msgSource) {
            answers.put(msgSource, msgAnswer);
            msgSource.notifyAll();
        }
    }

    @Override
    public void sendMessage(Message message) {
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
