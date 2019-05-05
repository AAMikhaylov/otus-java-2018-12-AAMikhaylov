package ru.otus.l14.db.dbService.dao;

import org.hibernate.Session;
import ru.otus.l14.db.base.DataSet;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.SQLException;
import java.util.List;

public class UserHibernateDAO implements UserDAO {
    private final Session session;

    public UserHibernateDAO(Session session) {
        this.session = session;
    }

    @Override
    public <T extends DataSet> void save(T user) throws SQLException {
        session.save(user);
    }

    @Override
    public <T extends DataSet> void update(T user) throws SQLException {
        session.update(user);

    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> cls) throws SQLException {
        return session.get(cls, id);
    }

    @Override
    public <T extends DataSet> T load(String login, Class<T> cls) throws SQLException {
        return session.byNaturalId(cls)
                .using("login", login)
                .load();
    }


    @Override
    public <T extends DataSet> List<T> load(Class<T> cls) throws SQLException {
        Query q = session.createQuery("from user", cls);
        return q.getResultList();
    }

    @Override
    public long count() throws SQLException {
        TypedQuery<Long> q = session.createQuery("select count (*) from user",Long.class);
        return  q.getSingleResult();
    }
}
