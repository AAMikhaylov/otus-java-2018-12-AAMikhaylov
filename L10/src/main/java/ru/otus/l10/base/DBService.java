package ru.otus.l10.base;

import java.sql.SQLException;

public interface DBService {
    public void dropTable(Class<?> dataSetClass) throws SQLException;
}
