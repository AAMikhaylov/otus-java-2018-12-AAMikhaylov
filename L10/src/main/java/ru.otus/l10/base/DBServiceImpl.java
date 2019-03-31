package ru.otus.l10.base;

import ru.otus.l10.dbcommon.DBHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBServiceImpl implements DBService {
    private final Connection connection;
    private Class<?> dataSetClass;


    private void createTable() throws SQLException {
        String dbTableName = DBHelper.getDbTableName(dataSetClass);
        StringBuilder SQLQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        SQLQuery.append(dbTableName);
        SQLQuery.append("\r\n(id  BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT");
        Field[] fields = dataSetClass.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (!Modifier.isStatic(f.getModifiers()) && (f.getType().isPrimitive() || f.getType() == String.class))
                SQLQuery.append(",\r\n" + DBHelper.getDbColumn(f.getName(), f.getType()));
        }
        SQLQuery.append(");");
        try (final Statement statement = connection.createStatement()) {
            statement.execute(SQLQuery.toString());
        }
    }

    public void dropTable() throws SQLException {
        String SQLQuery = "DROP TABLE IF EXISTS " + DBHelper.getDbTableName(dataSetClass) + ";";
        try (final Statement statement = connection.createStatement()) {
            statement.execute(SQLQuery.toString());
        }

    }

    public DBServiceImpl(Connection connection, Class<?> dataSetClass) throws SQLException {
        this.connection = connection;
        this.dataSetClass = dataSetClass;
        createTable();
    }
}




