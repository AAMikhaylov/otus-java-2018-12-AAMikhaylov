package ru.otus.frontend.messages;

import ru.otus.dbService.DBService;
import ru.otus.messageSystem.Address;

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
