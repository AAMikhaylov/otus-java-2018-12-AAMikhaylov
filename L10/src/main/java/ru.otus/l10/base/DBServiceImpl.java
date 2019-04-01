package ru.otus.l10.base;

import ru.otus.l10.dbcommon.DBHelper;
import ru.otus.l10.user.DataSet;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBServiceImpl implements DBService {
    private final Connection connection;


    private void createTable(Class<?> dataSetClass) throws SQLException {
        try (final Statement statement = connection.createStatement()) {
            statement.execute(DBHelper.dbCreateQuery(dataSetClass));
        }
    }

    public void dropTable(Class<?> dataSetClass) throws SQLException {
        try (final Statement statement = connection.createStatement()) {
            statement.execute(DBHelper.dbDropQuery(dataSetClass));
        }
    }

    public DBServiceImpl(Connection connection, Class<? extends DataSet>... dataSetClasses) throws SQLException {
        this.connection = connection;
        for (Class<?> dataSetClass : dataSetClasses)
            createTable(dataSetClass);
    }
}




