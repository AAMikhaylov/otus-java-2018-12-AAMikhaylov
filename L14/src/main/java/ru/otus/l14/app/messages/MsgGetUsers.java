package ru.otus.l14.app.messages;

import com.google.gson.Gson;
import ru.otus.l14.app.MsgToDB;
import ru.otus.l14.db.base.UserDataSet;
import ru.otus.l14.db.dbService.DBService;
import ru.otus.l14.messageSystem.Address;

import java.util.ArrayList;
import java.util.List;

public class MsgGetUsers extends MsgToDB {
    private final String userId;

    public MsgGetUsers(Address from, Address to, String userId) {
        super(from, to);
        this.userId = userId;
    }

    @Override
    public void exec(DBService dbService) {
        List<UserDataSet> users = null;
        try {
            if (userId != null) {
                users = new ArrayList<>();
                int id = Integer.parseInt(userId);
                UserDataSet user = dbService.load(id, UserDataSet.class);
                users.add(user);
            } else
                users = dbService.load(UserDataSet.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        String usersJsonList = gson.toJson(users);
        dbService.getMS().sendMessage(new MsgGetUsersAnswer(getTo(), getFrom(), usersJsonList, this));
    }
}
