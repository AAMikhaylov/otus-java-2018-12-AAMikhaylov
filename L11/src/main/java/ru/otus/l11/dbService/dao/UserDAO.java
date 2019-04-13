package ru.otus.l11.dbService.dao;

import ru.otus.l11.base.dataSets.DataSet;

import java.sql.SQLException;

public interface UserDAO {
    <T extends DataSet> void save(T user) throws SQLException;
    <T extends DataSet> T load(long id, Class<T> cls) throws SQLException;

}
