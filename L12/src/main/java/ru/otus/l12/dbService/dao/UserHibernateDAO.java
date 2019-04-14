package ru.otus.l12.dbService.dao;

import org.hibernate.Session;
import ru.otus.l12.base.DataSet;

import javax.persistence.Query;
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
        return  q.getResultList();
    }
}
