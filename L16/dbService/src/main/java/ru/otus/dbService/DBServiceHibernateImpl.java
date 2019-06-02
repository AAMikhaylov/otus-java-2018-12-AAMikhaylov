package ru.otus.dbService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.otus.dbService.base.AddressDataSet;
import ru.otus.dbService.base.DataSet;
import ru.otus.dbService.base.PhoneDataSet;
import ru.otus.dbService.base.UserDataSet;
import ru.otus.dbService.dao.UserDAO;
import ru.otus.dbService.dao.UserHibernateDAO;
import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.MessageSystem;
import ru.otus.messageSystem.MessageSystemContext;

import java.sql.SQLException;
import java.util.List;

public class DBServiceHibernateImpl implements DBService {
    private final MessageSystemContext context;
    private final Address address;
    private final SessionFactory sessionFactory;
    private final int serverPort;

    public static void main(String[] args) {
        try {
            if (args.length == 0)
                throw new Exception("Argument count exception.");
            DBService db = new DBServiceHibernateImpl("db/hibernate_oracle.cfg.xml", Integer.parseInt(args[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMS() {
        return context.getMessageSystem();
    }

    @Override
    public void init() {
        context.getMessageSystem().addAddressee(this);
        try {
            UserDataSet user = new UserDataSet("alexm", "qwerty", "Alexey", 19, new AddressDataSet("Ленинский проспект, 40"), new PhoneDataSet("111-222-333"), new PhoneDataSet("333-444-555"));
            save(user);
            user = new UserDataSet("alexm2", "qwerty", "Alexander", 19, new AddressDataSet("Ленинский проспект, 40"), new PhoneDataSet("111-222-333"), new PhoneDataSet("333-444-555"));
            save(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public DBServiceHibernateImpl(String hibernateCfgFile, int serverPort) {
        System.out.println("DB Service started for connection to " + serverPort + " port.");
        this.serverPort = serverPort;
        this.context = null;
        this.address = null;
        Configuration configuration = new Configuration()
                .configure(hibernateCfgFile)
                .addAnnotatedClass(UserDataSet.class)
                .addAnnotatedClass(AddressDataSet.class)
                .addAnnotatedClass(PhoneDataSet.class);
        sessionFactory = configuration.buildSessionFactory();

    }

    public DBServiceHibernateImpl(MessageSystemContext context, Address address, String hibernateCfgFile) {
        serverPort = 0;
        this.context = context;
        this.address = address;
        Configuration configuration = new Configuration()
                .configure(hibernateCfgFile)
                .addAnnotatedClass(UserDataSet.class)
                .addAnnotatedClass(AddressDataSet.class)
                .addAnnotatedClass(PhoneDataSet.class);
        sessionFactory = configuration.buildSessionFactory();

    }

    private <T extends DataSet> T execute(InTransExecutable<T> executor) throws SQLException {
        Transaction transaction = null;
        try (final Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            T result = executor.executeInTrans(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        }
    }

    @Override
    public <T extends DataSet> void update(T user) throws SQLException {
        execute((session) -> {
            UserDAO dao = new UserHibernateDAO(session);
            dao.update(user);
            return user;
        });

    }

    @Override
    public <T extends DataSet> void save(T user) throws SQLException {
        execute((session) -> {
            UserDAO dao = new UserHibernateDAO(session);
            dao.save(user);
            return user;
        });
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> cls) throws SQLException {
        return execute((session) -> {
            UserDAO dao = new UserHibernateDAO(session);
            return dao.load(id, cls);
        });
    }

    @Override
    public <T extends DataSet> T load(String login, Class<T> cls) throws SQLException {
        return execute((session) -> {
            UserDAO dao = new UserHibernateDAO(session);
            return dao.load(login, cls);
        });
    }


    @Override
    public <T extends DataSet> List<T> load(Class<T> cls) throws SQLException {
        try (final Session session = sessionFactory.openSession()) {
            UserDAO dao = new UserHibernateDAO(session);
            return dao.load(cls);
        }

    }

    @Override
    public long count() throws SQLException {
        try (final Session session = sessionFactory.openSession()) {
            UserDAO dao = new UserHibernateDAO(session);
            return dao.count();
        }

    }

    @Override
    public void shutdown() {
        sessionFactory.close();
    }
}
