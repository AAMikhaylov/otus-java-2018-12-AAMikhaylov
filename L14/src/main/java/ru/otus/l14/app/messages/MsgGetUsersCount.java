package ru.otus.l14.app.messages;

import ru.otus.l14.app.MsgToDB;
import ru.otus.l14.db.dbService.DBService;
import ru.otus.l14.messageSystem.Address;

public class MsgGetUsersCount extends MsgToDB {
    public MsgGetUsersCount(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(DBService dbService) {
        String result;
        try {
            result = Long.toString(dbService.count());
        } catch (Exception e) {
            e.printStackTrace();
            result = "0";
        }
        dbService.getMS().sendMessage(new MsgGetUsersCountAnswer(getTo(), getFrom(), result, this));
    }
}
