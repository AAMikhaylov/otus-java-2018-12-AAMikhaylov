package ru.otus.l10.dbcommon;

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

    public static String getFields(Class<?> cls) {
        StringBuilder ret = new StringBuilder();
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (!Modifier.isStatic(f.getModifiers()) && (f.getType().isPrimitive() || f.getType() == String.class))
                if (ret.length() == 0)
                    ret.append(f.getName());
                else
                    ret.append("," + f.getName());
        }
        return ret.toString();
    }

    private static String getDbValue(Object object) {
        if (object.getClass() == String.class || object.getClass() == Character.class)
            return "\"" + object.toString() + "\"";
        else
            return object.toString();

    }

    public static String getFieldValues(Object obj) {
        StringBuilder ret = new StringBuilder();
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                if (!Modifier.isStatic(f.getModifiers()) && (f.getType().isPrimitive() || f.getType() == String.class))
                    if (ret.length() == 0) {
                        ret.append(getDbValue(f.get(obj)));
                    } else
                        ret.append("," + getDbValue(f.get(obj)));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    public static void initObjectFields(Object initObject, Class<?> initClass, ResultSet resultSet) throws SQLException {
        Field[] fields = initClass.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (!Modifier.isStatic(f.getModifiers()) && (f.getType().isPrimitive() || f.getType() == String.class)) {
                try {
                    f.set(initObject, resultSet.getObject(f.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public static String getDbTableName(Class<?> cls) {
        String dbTableName = cls.getName().replace(cls.getPackageName() + ".", "");
        return dbTableName.replace("DataSet", "");
    }

    public static String getDbColumn(String fieldName, Class<?> fieldClass) {
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
