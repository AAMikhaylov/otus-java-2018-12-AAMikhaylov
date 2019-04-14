package ru.otus.l12;

import org.hibernate.cfg.Configuration;
import ru.otus.l12.dbService.DBService;
import ru.otus.l12.base.AddressDataSet;
import ru.otus.l12.base.PhoneDataSet;
import ru.otus.l12.dbService.DBServiceHibernateImpl;
import ru.otus.l12.dbService.DBServiceImpl;
import ru.otus.l12.dbcommon.DBHelper;
import ru.otus.l12.base.HumanDataSet;
import ru.otus.l12.base.UserDataSet;
import ru.otus.l12.dbcommon.DDLService;
import ru.otus.l12.webserver.JettyServer;
import ru.otus.l12.webserver.UserHttpService;


import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    private static void myORM() {
        try (Connection connection = DBHelper.getMySQLConnection()) {
            DDLService ddlService = new DDLService(connection);
            DBService dbService = new DBServiceImpl(connection, ddlService, UserDataSet.class, HumanDataSet.class);
            HumanDataSet h1 = new HumanDataSet((byte) 20, false, (short) 30, 'A', 100, 0.30f, 10000L, 0.00009, "Andrew");
            UserDataSet u1 = new UserDataSet("alexm", "qwerty", "Alex", 30, new AddressDataSet("Ленинский проспект, 40"), new PhoneDataSet("111-222-333"), new PhoneDataSet("333-444-555"));
            dbService.save(h1);
            dbService.save(u1);
            HumanDataSet humanFromdb = dbService.load(1, HumanDataSet.class);
            UserDataSet userFromDb = dbService.load("alexm", UserDataSet.class);
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
            dbService.shutdown();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void hibernateORM() {


        Configuration configuration = new Configuration()
                .configure("db/hibernate.cfg.xml")
                .addAnnotatedClass(UserDataSet.class)
                .addAnnotatedClass(AddressDataSet.class)
                .addAnnotatedClass(PhoneDataSet.class);
        DBService dbService = new DBServiceHibernateImpl(configuration);
        UserDataSet user = new UserDataSet("alexm", "qwerty", "Alex", 19, new AddressDataSet("Ленинский проспект, 40"), new PhoneDataSet("111-222-333"), new PhoneDataSet("333-444-555"));
        try {
            dbService.save(user);
            UserDataSet ud = dbService.load("alexm", UserDataSet.class);
            System.out.println(ud);
            dbService.shutdown();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration()
                .configure("db/hibernate.cfg.xml")
                .addAnnotatedClass(UserDataSet.class)
                .addAnnotatedClass(AddressDataSet.class)
                .addAnnotatedClass(PhoneDataSet.class);
        DBService dbService = new DBServiceHibernateImpl(configuration);
        UserDataSet user = new UserDataSet("alexm", "qwerty", "Alexey", 19, new AddressDataSet("Ленинский проспект, 40"), new PhoneDataSet("111-222-333"), new PhoneDataSet("333-444-555"));
        try {
            dbService.save(user);
            user = new UserDataSet("alexm2", "qwerty", "Alexander", 19, new AddressDataSet("Ленинский проспект, 40"), new PhoneDataSet("111-222-333"), new PhoneDataSet("333-444-555"));
            dbService.save(user);
        } catch (SQLException e) {
          e.printStackTrace();
        }
        JettyServer jettyServer = new JettyServer(80, new UserHttpService(dbService));
        jettyServer.start();
        dbService.shutdown();

    }
}
