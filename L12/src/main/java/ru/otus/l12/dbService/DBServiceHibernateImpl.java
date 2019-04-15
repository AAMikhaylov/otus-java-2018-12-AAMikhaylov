package ru.otus.l12.dbService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.otus.l12.base.DataSet;
import ru.otus.l12.dbService.dao.UserDAO;
import ru.otus.l12.dbService.dao.UserHibernateDAO;

import java.sql.SQLException;
import java.util.List;

public class DBServiceHibernateImpl implements DBService {
    private SessionFactory sessionFactory;

    public DBServiceHibernateImpl(Configuration configuration) {
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
    public void shutdown() throws SQLException {
        sessionFactory.close();
    }
}
