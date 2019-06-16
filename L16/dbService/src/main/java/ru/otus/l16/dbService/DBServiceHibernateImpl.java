package ru.otus.l16.dbService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.otus.l16.dbService.base.AddressDataSet;
import ru.otus.l16.dbService.base.DataSet;
import ru.otus.l16.dbService.base.PhoneDataSet;
import ru.otus.l16.dbService.base.UserDataSet;
import ru.otus.l16.dbService.dao.UserDAO;
import ru.otus.l16.dbService.dao.UserHibernateDAO;


import java.sql.SQLException;
import java.util.List;

public class DBServiceHibernateImpl implements DBService {
    private final SessionFactory sessionFactory;

    @Override
    public void init(boolean addData) {
        if (addData)
            try {
                UserDataSet user = new UserDataSet("alexm", "qwerty", "Alexey", 19, new AddressDataSet("Ленинский проспект, 40"), new PhoneDataSet("111-222-333"), new PhoneDataSet("333-444-555"));
                save(user);
                user = new UserDataSet("alexm2", "qwerty", "Alexander", 19, new AddressDataSet("Ленинский проспект, 40"), new PhoneDataSet("111-222-333"), new PhoneDataSet("333-444-555"));
                save(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public DBServiceHibernateImpl(String hibernateCfgFile, boolean createSchema) {
        Configuration configuration = new Configuration()
                .configure(hibernateCfgFile)
                .addAnnotatedClass(UserDataSet.class)
                .addAnnotatedClass(AddressDataSet.class)
                .addAnnotatedClass(PhoneDataSet.class);
        if (!createSchema)
            configuration.setProperty("hibernate.hbm2ddl.auto", "validate");
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
