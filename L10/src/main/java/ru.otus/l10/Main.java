package ru.otus.l10;

import ru.otus.l10.base.DBService;
import ru.otus.l10.base.DBServiceImpl;
import ru.otus.l10.dbcommon.DBHelper;
import ru.otus.l10.user.UserDAO;
import ru.otus.l10.user.UserDataSet;
import ru.otus.l10.user.UserExecutorDAO;
import ru.otus.l10.user.UserTestDataSet;


import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DBHelper.getMySQLConnection()) {
            DBService dbService = new DBServiceImpl(connection, UserDataSet.class);
//            UserTestDataSet u = new UserTestDataSet((byte)20,false, (short) 30,'A',100, 0.30f,10000L,0.00009,"Andrew");
//            UserDataSet u = new UserDataSet("Alex",30);
            UserDAO dao = new UserExecutorDAO(connection);
            UserDataSet u1= dao.load(1,UserDataSet.class);
            System.out.println(u1);
//            dao.save(u);
//            dbService.dropTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
