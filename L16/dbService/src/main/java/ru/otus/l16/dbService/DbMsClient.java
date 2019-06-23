package ru.otus.l16.dbService;

import com.google.gson.Gson;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import ru.otus.l16.dbService.base.AddressDataSet;
import ru.otus.l16.dbService.base.PhoneDataSet;
import ru.otus.l16.messageSystem.Address;
import ru.otus.l16.messageSystem.channel.MsgChannel;
import ru.otus.l16.dbService.base.UserDataSet;
import ru.otus.l16.messageSystem.message.*;

import ru.otus.l16.messageSystem.MsClient;
import ru.otus.l16.messageSystem.workers.SocketMsgWorker;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbMsClient implements MsClient {
    private final MsgChannel msgChannel;
    private final Address address;
    private final DBService dbService;
    private final Logger logger;

    public DbMsClient(DBService dbService, MsgChannel msgChannel, Address address) {
        logger = Logger.getLogger(SocketMsgWorker.class.getName() + "." + address.getId());
        this.dbService = dbService;
        this.msgChannel = msgChannel;
        this.address = address;
    }

    public void shutdown(MsgShutdown message) {
        dbService.shutdown();
        try {
            msgChannel.setCanRestart(false);
            msgChannel.close();
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public void addNewUser(MsgAddUser message) {
        boolean success = false;
        try {
            AddressDataSet addr = new AddressDataSet(message.getUserAddress());
            String[] phonesStr = message.getUserPhones().split(",");
            PhoneDataSet[] phones = new PhoneDataSet[phonesStr.length];
            for (int i = 0; i < phonesStr.length; i++)
                phones[i] = new PhoneDataSet(phonesStr[i].trim());
            UserDataSet user = new UserDataSet(message.getLogin(),
                    message.getPassword(),
                    message.getUserName(),
                    message.getAge(),
                    addr, phones);
            dbService.save(user);
            success = true;
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        MsgAddUser answer = new MsgAddUser(message, address, message.getFrom(), success);
        sendMessage(answer);


    }

    public void getUsersCount(MsgGetUsersCount message) {
        String result;
        try {
            result = Long.toString(dbService.count());
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            result = "0";
        }
        MsgGetUsersCount answer = new MsgGetUsersCount(message, address, message.getFrom(), result);
        sendMessage(answer);
    }

    public void getUsers(MsgGetUsers message) {
        List<UserDataSet> users = null;
        String usersJsonList = null;
        try {
            if (message.getUserId() != null) {
                users = new ArrayList<>();
                int id = Integer.parseInt(message.getUserId().trim());
                UserDataSet user = dbService.load(id, UserDataSet.class);
                users.add(user);
            } else
                users = dbService.load(UserDataSet.class);
            Gson gson = new Gson();
            usersJsonList = gson.toJson(users);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        MsgGetUsers answer = new MsgGetUsers(message, address, message.getFrom(), usersJsonList);
        sendMessage(answer);
    }

    public void authenticate(MsgAuthUser message) {
        boolean authResult = false;
        String userName = null;
        if (message.getLogin() != null)
            try {
                UserDataSet user = dbService.load(message.getLogin(), UserDataSet.class);
                if (user != null) {
                    userName = user.getName();
                    if (message.getPassword() != null) {
                        authResult = message.getPassword().equals(user.getPassword());
                        if (authResult) {
                            user.setSessionID(message.getSessionID());
                            dbService.update(user);
                        }
                    } else {
                        if (message.getSessionID() != null)
                            authResult = message.getSessionID().equals(user.getSessionID());
                    }
                }
            } catch (SQLException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        MsgAuthUser answer = new MsgAuthUser(message, address, message.getFrom(), userName, authResult);
        sendMessage(answer);
    }


    @Override
    public void sendMessage(Message message) {
        msgChannel.send(message);
    }

    @Override
    public void messageHandler(Message message) {

        if (message.getClass().equals(MsgAuthUser.class))
            authenticate((MsgAuthUser) message);

        if (message.getClass().equals(MsgGetUsers.class))
            getUsers((MsgGetUsers) message);

        if (message.getClass().equals(MsgGetUsersCount.class))
            getUsersCount((MsgGetUsersCount) message);

        if (message.getClass().equals(MsgAddUser.class))
            addNewUser((MsgAddUser) message);
        if (message.getClass().equals(MsgShutdown.class))
            shutdown((MsgShutdown) message);

    }

}
