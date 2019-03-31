package ru.otus.l10.user;

import java.sql.SQLException;

public interface UserDAO {
    <T extends DataSet> void save(T user) throws SQLException;

    <T extends DataSet> T load(long id, Class<T> cls) throws SQLException;

}
