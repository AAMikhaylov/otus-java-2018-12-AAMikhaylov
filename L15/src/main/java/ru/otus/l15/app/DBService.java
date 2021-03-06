package ru.otus.l15.app;

import ru.otus.l15.db.base.DataSet;
import ru.otus.l15.messageSystem.Addressee;

import java.sql.SQLException;
import java.util.List;

public interface DBService extends Addressee {
    void init();

    <T extends DataSet> void save(T user) throws SQLException;

    <T extends DataSet> void update(T user) throws SQLException;

    <T extends DataSet> T load(long id, Class<T> cls) throws SQLException;

    <T extends DataSet> T load(String login, Class<T> cls) throws SQLException;

    <T extends DataSet> List<T> load(Class<T> cls) throws SQLException;

    long count() throws SQLException;

    void shutdown();
}
