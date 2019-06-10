package ru.otus.frontend;

import ru.otus.l16.channel.MsgChannel;
import ru.otus.l16.channel.MsgClientChannel;

import ru.otus.l16.messageSystem.Address;
import ru.otus.l16.messages.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class FrontendServiceImpl implements FrontendService {

    private final Map<Message, LinkedBlockingQueue<Message>> answers;
    private final MsgChannel msgChannel;

    public FrontendServiceImpl(int serverPort, String serverHost, String socketName) {
        msgChannel = new MsgClientChannel(serverPort, serverHost, socketName, (msg) -> System.out.println(msg));
        answers = new HashMap<>();
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
    public Address getDbAddress() {
        return null;
    }

    @Override
    public void sendMessage(Message message) {
        answers.put(message, new LinkedBlockingQueue<>());
        msgChannel.send(message);
    }
}
