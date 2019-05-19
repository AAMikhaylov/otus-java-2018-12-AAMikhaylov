package ru.otus.l15.app.messages;

import ru.otus.l15.app.DBService;
import ru.otus.l15.db.base.UserDataSet;
import ru.otus.l15.messageSystem.Address;


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
