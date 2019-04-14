package ru.otus.l12.dbcommon;

import ru.otus.l12.base.DataSet;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DDLService {
    private Connection connection;
    List<Class<? extends DataSet>> dataSetClasses = new ArrayList<>();

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


    public void createTables(Class<? extends DataSet>... dataSetClasses) throws SQLException {
        try (final Statement statement = connection.createStatement()) {
            for (Class dataSetClass : dataSetClasses) {
                statement.execute(dbCreateQuery(dataSetClass));
                this.dataSetClasses.add(dataSetClass);
            }
        }
    }

    public void dropTables() throws SQLException {
        try (final Statement statement = connection.createStatement()) {
            Iterator<Class<? extends DataSet>> iterDataSet = dataSetClasses.iterator();
            while (iterDataSet.hasNext()) {
                statement.execute(dbDropQuery(iterDataSet.next()));
                iterDataSet.remove();
            }
        }
    }
}
