package ru.otus.l14.db.dbService.dao;

import ru.otus.l14.db.base.DataSet;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    <T extends DataSet> void save(T user) throws SQLException;

    <T extends DataSet> void update(T user) throws SQLException;

    <T extends DataSet> T load(long id, Class<T> cls) throws SQLException;

    <T extends DataSet> T load(String login, Class<T> cls) throws SQLException;

    <T extends DataSet> List<T> load(Class<T> cls) throws SQLException;

    long count() throws SQLException;


}
