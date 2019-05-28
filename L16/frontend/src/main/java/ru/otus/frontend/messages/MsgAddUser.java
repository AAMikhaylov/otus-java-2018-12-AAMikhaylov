package ru.otus.frontend.messages;

import ru.otus.dbService.DBService;
import ru.otus.dbService.base.UserDataSet;
import ru.otus.messageSystem.Address;

public class MsgAddUser extends MsgToDB {
    private final UserDataSet user;

    public MsgAddUser(Address from, Address to, UserDataSet user) {
        super(from, to);
        this.user = user;
    }

    @Override
    public void exec(DBService dbService) {
        try {
            dbService.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbService.getMS().sendMessage(new MsgAddUserAnswer(getTo(), getFrom(), this));
    }
}
