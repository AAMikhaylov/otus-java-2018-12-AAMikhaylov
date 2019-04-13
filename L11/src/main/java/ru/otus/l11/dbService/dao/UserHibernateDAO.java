package ru.otus.l11.dbService.dao;

import org.hibernate.Session;
import ru.otus.l11.base.dataSets.DataSet;

import java.sql.SQLException;

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
    public <T extends DataSet> T load(long id, Class<T> cls) throws SQLException {
        return session.get(cls, id);
    }
}
