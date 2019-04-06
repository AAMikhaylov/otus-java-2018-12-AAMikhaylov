package ru.otus.l11;

import ru.otus.l11.base.DBService;
import ru.otus.l11.base.dataSets.AddressDataSet;
import ru.otus.l11.base.dataSets.PhoneDataSet;
import ru.otus.l11.dbService.DBServiceHibernateImpl;
import ru.otus.l11.dbService.DBServiceImpl;
import ru.otus.l11.dbcommon.DBHelper;
import ru.otus.l11.base.dataSets.HumanDataSet;
import ru.otus.l11.base.dataSets.UserDataSet;


import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    private static void myORM() {
        try (Connection connection = DBHelper.getMySQLConnection()) {
            DBService dbService = new DBServiceImpl(connection, UserDataSet.class, HumanDataSet.class);
            HumanDataSet h1 = new HumanDataSet((byte) 20, false, (short) 30, 'A', 100, 0.30f, 10000L, 0.00009, "Andrew");
            UserDataSet u1 = new UserDataSet("Alex", 30, new AddressDataSet("Ленинский проспект, 40"), new PhoneDataSet[]{new PhoneDataSet("111-222-333"), new PhoneDataSet("333-444-555")});
            dbService.save(h1);
            dbService.save(u1);
            HumanDataSet humanFromdb = dbService.load(1, HumanDataSet.class);
            UserDataSet userFromDb = dbService.load(1, UserDataSet.class);
            System.out.println(humanFromdb);
            System.out.println(userFromDb);
            humanFromdb.setFirstName(humanFromdb.getFirstName() + " Ivanov");
            userFromDb.setName(userFromDb.getName() + " Petrov");
            dbService.save(humanFromdb);
            dbService.save(userFromDb);
            humanFromdb = dbService.load(1, HumanDataSet.class);
            userFromDb = dbService.load(133, UserDataSet.class);
            System.out.println(humanFromdb);
            System.out.println(userFromDb);
            dbService.dropTable(UserDataSet.class);
            dbService.dropTable(HumanDataSet.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void hibernateORM() {
        DBService dbService = new DBServiceHibernateImpl();
        UserDataSet user = new UserDataSet("Alex", 19, new AddressDataSet("Ленинский проспект, 40"), new PhoneDataSet[]{new PhoneDataSet("111-222-333"), new PhoneDataSet("333-444-555")});
        try {
            dbService.save(user);
            UserDataSet ud = dbService.load(1, UserDataSet.class);
            System.out.println(ud);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        hibernateORM();

    }
}
