package ru.otus.l11.base;

import ru.otus.l11.base.dataSets.DataSet;

import java.sql.SQLException;

public interface DBService {
    public void dropTable(Class<? extends DataSet> dataSetClass) throws SQLException;
    public <T extends DataSet> void save(T user) throws SQLException;
    <T extends DataSet> T load(long id, Class<T> cls) throws SQLException;
}
