package ru.otus.l11.dbcommon;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHelper {

    public static Connection getMySQLConnection() throws SQLException {
        final String mySQLConnString = "jdbc:mysql://  " +
                "db4free.net:" +
                "3306/otuslessions?" +
                "user=otusstudent&" +
                "password=Fktrctq161077&" +
                "useSSL=false";
        return DriverManager.getConnection(mySQLConnString);
    }

    public static String dbUpdateQuery(Object javaObject, long id) {
        StringBuilder result = new StringBuilder("update  " + dbTableName(javaObject.getClass()) + " set ");
        Field[] fields = javaObject.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++)
            try {
                fields[i].setAccessible(true);
                if (isForDbField(fields[i]))
                    if (i == 0)
                        result.append(fields[i].getName() + "=" + dbValue(fields[i].get(javaObject)));
                    else
                        result.append(", " + fields[i].getName() + "=" + dbValue(fields[i].get(javaObject)));
            } catch (
                    IllegalAccessException e) {
                e.printStackTrace();
            }
        result.append(" where id=" + id + ";");
        return result.toString();
    }

    public static String dbSelectQuery(Class<?> javaClass, String filter) {
        String result = "select * from  " + dbTableName(javaClass);
        if (filter.isEmpty())
            return result + ";";
        else
            return result + " where " + filter + ";";
    }

    public static String dbSelectCountQuery(Class<?> javaClass, String filter) {
        String result = "select count(*) cnt from  " + dbTableName(javaClass);
        if (filter.isEmpty())
            return result + ";";
        else
            return result + " where " + filter + ";";
    }


    public static String dbDropQuery(Class<?> javaClass) {
        return "drop table if exists " + DBHelper.dbTableName(javaClass) + ";";
    }

    public static String dbCreateQuery(Class<?> javaClass) {
        StringBuilder result = new StringBuilder("create table if not exists " + dbTableName(javaClass) + " ");
        result.append("(id  bigint(20) not null primary key auto_increment");
        Field[] fields = javaClass.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (isForDbField(f))
                result.append(",  " + DBHelper.dbCreateColumnClause(f.getName(), f.getType()));
        }
        result.append(");");
        return result.toString();
    }

    public static String dbInsertQuery(Object javaObject) {
        return "insert into " + dbTableName(javaObject.getClass()) + " " +
                dbInsertFieldList(javaObject.getClass()) + " " +
                "values " + dbInsertValueList(javaObject) + ";";
    }

    private static String dbInsertValueList(Object javaObject) {
        StringBuilder result = new StringBuilder("(");
        Field[] fields = javaObject.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                if (isForDbField(fields[i]))
                    if (i == 0) {
                        result.append(dbValue(fields[i].get(javaObject)));
                    } else
                        result.append(", " + dbValue(fields[i].get(javaObject)));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        result.append(")");
        return result.toString();
    }

    private static String dbInsertFieldList(Class<?> javaClass) {
        StringBuilder result = new StringBuilder("(");
        Field[] fields = javaClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            if (isForDbField(fields[i]))
                if (i == 0)
                    result.append(fields[i].getName());
                else
                    result.append(", " + fields[i].getName());
        }
        result.append(")");
        return result.toString();
    }

    private static boolean isForDbField(Field field) {
        if (!Modifier.isStatic(field.getModifiers()) && (field.getType().isPrimitive() || field.getType() == String.class))
            return true;
        return false;
    }

    private static String dbValue(Object object) {
        if (object == null)
            return "null";
        if (object.getClass() == String.class || object.getClass() == Character.class)
            return "\"" + object.toString() + "\"";
        else
            return object.toString();

    }


    private static String dbTableName(Class<?> cls) {
        String dbTableName = cls.getName().replace(cls.getPackageName() + ".", "");
        return dbTableName.replace("DataSet", "");
    }


    public static void initJavaObjectFields(Object initObject, Class<?> initClass, ResultSet resultSet) throws SQLException {
        Field[] fields = initClass.getDeclaredFields();
        for (Field f : fields)
            try {
                f.setAccessible(true);
                if (isForDbField(f))
                    if (f.getType() == byte.class)
                        f.set(initObject, resultSet.getByte(f.getName()));
                    else if (f.getType() == short.class)
                        f.set(initObject, resultSet.getShort(f.getName()));
                    else if (f.getType() == char.class)
                        f.set(initObject, resultSet.getString(f.getName()).charAt(0));
                    else
                        f.set(initObject, resultSet.getObject(f.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
    }

    private static String dbCreateColumnClause(String fieldName, Class<?> fieldClass) {
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

}
