package ru.otus.l11.dbService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.otus.l11.base.DBService;
import ru.otus.l11.base.dataSets.DataSet;
import ru.otus.l11.dbService.dao.UserDAO;
import ru.otus.l11.dbService.dao.UserHibernateDAO;

import java.sql.SQLException;

public class DBServiceHibernateImpl implements DBService {
    private SessionFactory sessionFactory;

    public DBServiceHibernateImpl(Configuration configuration) {
        sessionFactory = configuration.configure().buildSessionFactory();
    }

    @Override
    public <T extends DataSet> void save(T user) throws SQLException {
        Transaction transaction = null;
        try (final Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            UserDAO dao = new UserHibernateDAO(session);
            dao.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        }
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> cls) throws SQLException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            UserDAO dao = new UserHibernateDAO(session);
            transaction.commit();
            return dao.load(id, cls);
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        }
    }

    @Override
    public void shutdown() throws SQLException {
        sessionFactory.close();
    }
}
