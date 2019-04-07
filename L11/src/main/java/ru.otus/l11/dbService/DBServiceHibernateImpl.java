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

    public DBServiceHibernateImpl(Configuration configuration, Class<? extends DataSet>... classes) {
        if (classes != null)
            for (Class<?> cls : classes)
                configuration.addAnnotatedClass(cls);
        sessionFactory = configuration.configure().buildSessionFactory();
    }

    @Override
    public <T extends DataSet> void save(T user) throws SQLException {
        try (final Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.getTransaction();
            transaction.begin();
            UserDAO dao = new UserHibernateDAO(session);
            dao.save(user);
            transaction.commit();
        }
    }
    @Override
    public <T extends DataSet> T load(long id, Class<T> cls) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.getTransaction();
            transaction.begin();
            UserDAO dao = new UserHibernateDAO(session);
            transaction.commit();
            return dao.load(id, cls);

        }
    }
    
}
