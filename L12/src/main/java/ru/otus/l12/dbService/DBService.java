package ru.otus.l12.dbService;

import ru.otus.l12.base.DataSet;

import java.sql.SQLException;
import java.util.List;

public interface DBService {
    <T extends DataSet> void save(T user) throws SQLException;

    <T extends DataSet> void update(T user) throws SQLException;

    <T extends DataSet> T load(long id, Class<T> cls) throws SQLException;

    <T extends DataSet> T load(String login, Class<T> cls) throws SQLException;

    <T extends DataSet> List<T> load(Class<T> cls) throws SQLException;

    void shutdown() throws SQLException;


}