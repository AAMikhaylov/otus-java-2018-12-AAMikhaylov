package ru.otus.l11.dbcommon;

import ru.otus.l11.base.dataSets.DataSet;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DDLService {
    private Connection connection;

    public DDLService(Connection connection) {
        this.connection = connection;
    }

    private String dbCreateColumnClause(String fieldName, Class<?> fieldClass) {
        if (fieldClass == byte.class)
            return fieldName + "  TINYINT";
        if (fieldClass == boolean.class)
            return fieldName + "  BOOL";
        if (fieldClass == short.class)
            return fieldName + "  SMALLINT";
        if (fieldClass == char.class)
            return fieldName + "  CHAR";
        if (fieldClass == int.class)
            return fieldName + "  INTEGER";
        if (fieldClass == float.class)
            return fieldName + "  FLOAT";
        if (fieldClass == long.class)
            return fieldName + "  BIGINT";
        if (fieldClass == double.class)
            return fieldName + "  DOUBLE";
        if (fieldClass == String.class)
            return fieldName + "  VARCHAR(255)";
        return null;
    }


    private String dbDropQuery(Class<?> javaClass) {
        return "drop table if exists " + DBHelper.dbTableName(javaClass) + ";";
    }

    private String dbCreateQuery(Class<?> javaClass) {
        StringBuilder result = new StringBuilder("create table if not exists " + DBHelper.dbTableName(javaClass) + " ");
        result.append("(id  bigint(20) not null primary key auto_increment");
        Field[] fields = javaClass.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (DBHelper.isForDbField(f))
                result.append(",  " + dbCreateColumnClause(f.getName(), f.getType()));
        }
        result.append(");");
        return result.toString();
    }


    public void createTable(Class<?> dataSetClass) throws SQLException {
        try (final Statement statement = connection.createStatement()) {
            statement.execute(dbCreateQuery(dataSetClass));
        }
    }

    public void dropTable(Class<? extends DataSet> dataSetClass) throws SQLException {
        try (final Statement statement = connection.createStatement()) {
            statement.execute(dbDropQuery(dataSetClass));
        }
    }
}
