package ru.otus.l14.app.messages;

import ru.otus.l14.app.MsgToDB;
import ru.otus.l14.db.base.UserDataSet;
import ru.otus.l14.db.dbService.DBService;
import ru.otus.l14.messageSystem.Address;

import java.sql.SQLException;

public class MsgAuthUser extends MsgToDB {
    private final String login;
    private final String password;

    public MsgAuthUser(Address from, Address to, String login, String password) {
        super(from, to);
        this.login = login;
        this.password = password;
    }

    @Override
    public void exec(DBService dbService) {
        try {
            UserDataSet user = dbService.load(login, UserDataSet.class);
            boolean authResult = false;
            if (password.equals(user.getPassword()))
                authResult = true;
            dbService.getMS().sendMessage(new MsgAuthUserAnswer(getTo(), getFrom(), authResult, getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
