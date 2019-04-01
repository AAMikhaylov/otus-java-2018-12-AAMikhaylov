package ru.otus.l10;

import ru.otus.l10.base.DBService;
import ru.otus.l10.base.DBServiceImpl;
import ru.otus.l10.dbcommon.DBHelper;
import ru.otus.l10.user.HumanDataSet;
import ru.otus.l10.user.UserDAO;
import ru.otus.l10.user.UserDataSet;
import ru.otus.l10.user.UserExecutorDAO;


import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DBHelper.getMySQLConnection()) {
            DBService dbService = new DBServiceImpl(connection, UserDataSet.class, HumanDataSet.class);
            UserDAO dao = new UserExecutorDAO(connection);
            HumanDataSet h1 = new HumanDataSet((byte) 20, false, (short) 30, 'A', 100, 0.30f, 10000L, 0.00009, "Andrew");
            UserDataSet u1 = new UserDataSet("Alex", 30);
            dao.save(h1);
            dao.save(u1);
            HumanDataSet humanFromdb = dao.load(1, HumanDataSet.class);
            UserDataSet userFromDb = dao.load(1, UserDataSet.class);
            System.out.println(humanFromdb);
            System.out.println(userFromDb);
            humanFromdb.setFirstName(humanFromdb.getFirstName() + " Ivanov");
            userFromDb.setName(userFromDb.getName() + " Petrov");
            dao.save(humanFromdb);
            dao.save(userFromDb);
            humanFromdb = dao.load(1, HumanDataSet.class);
            userFromDb = dao.load(133, UserDataSet.class);
            System.out.println(humanFromdb);
            System.out.println(userFromDb);
            dbService.dropTable(UserDataSet.class);
            dbService.dropTable(HumanDataSet.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
