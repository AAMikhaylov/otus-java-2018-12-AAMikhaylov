package ru.otus.l14.front;

import ru.otus.l14.app.FrontendService;
import ru.otus.l14.app.MessageSystemContext;
import ru.otus.l14.app.messages.*;
import ru.otus.l14.db.base.AddressDataSet;
import ru.otus.l14.db.base.PhoneDataSet;
import ru.otus.l14.db.base.UserDataSet;
import ru.otus.l14.messageSystem.Address;
import ru.otus.l14.messageSystem.Message;
import ru.otus.l14.messageSystem.MessageSystem;

import javax.servlet.http.HttpServletRequest;
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

    public void addNewUser(HttpServletRequest req) {

        AddressDataSet addr = new AddressDataSet(req.getParameter("address").trim());
        String[] phonesStr = req.getParameter("phones").split(",");
        PhoneDataSet[] phones = new PhoneDataSet[phonesStr.length];
        for (int i = 0; i < phonesStr.length; i++)
            phones[i] = new PhoneDataSet(phonesStr[i].trim());
        UserDataSet user = new UserDataSet(req.getParameter("login").trim(),
                req.getParameter("password").trim(),
                req.getParameter("userName").trim(),
                Integer.parseInt(req.getParameter("age")),
                addr, phones
        );

        Message msg = new MsgAddUser(getAddress(), context.getDbAddress(), user);
        getMS().sendMessage(msg);
        MsgAddUserAnswer answer = (MsgAddUserAnswer) getAnswer(msg);
    }


    private Message getAnswer(Message srcMsg) {
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
    public boolean authenticate(HttpServletRequest req) {
        Message msg = new MsgAuthUser(getAddress(), context.getDbAddress(), req);
        getMS().sendMessage(msg);
        MsgAuthUserAnswer answer = (MsgAuthUserAnswer) getAnswer(msg);
        if (answer != null && answer.isAuth())
            return true;
        return false;
    }

    @Override
    public boolean authenticate(String login, String password, HttpServletRequest req) {
        Message msg = new MsgAuthUser(getAddress(), context.getDbAddress(), login, password, req);
        getMS().sendMessage(msg);
        MsgAuthUserAnswer answer = (MsgAuthUserAnswer) getAnswer(msg);
        if (answer != null && answer.isAuth())
            return true;
        return false;
    }


    @Override
    public String getUsersCount() {
        Message msg = new MsgGetUsersCount(getAddress(), context.getDbAddress());
        getMS().sendMessage(msg);
        MsgGetUsersCountAnswer answer = (MsgGetUsersCountAnswer) getAnswer(msg);
        if (answer != null)
            return answer.getUsersCount();
        return null;
    }

    @Override
    public String getUsers(String idStr) {
        Message msg = new MsgGetUsers(getAddress(), context.getDbAddress(), idStr);
        getMS().sendMessage(msg);
        MsgGetUsersAnswer answer = (MsgGetUsersAnswer) getAnswer(msg);
        if (answer != null)
            return answer.getUsersJsonList();
        return null;
    }


    @Override
    public void saveAnswer(Message msgAnswer, Message msgSource) {
        synchronized (msgSource) {
            answers.put(msgSource, msgAnswer);
            msgSource.notifyAll();
        }
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
    public MessageSystem getMS() {
        return context.getMessageSystem();
    }
}
