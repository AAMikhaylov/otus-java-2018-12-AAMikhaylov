package ru.otus.l10.user;

import ru.otus.l10.dbcommon.DBHelper;
import ru.otus.l10.executors.Executor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserExecutorDAO implements UserDAO {
    private final Connection connection;

    public UserExecutorDAO(Connection connection) {
        this.connection = connection;
    }

    private <T> T createUser(ResultSet resultSet, Class<T> userClass) throws SQLException {
        if (resultSet.next())
            try {
                T user = userClass.getConstructor().newInstance();
                DBHelper.initObjectFields(user,userClass,resultSet);
                DBHelper.initObjectFields(user,userClass.getSuperclass(),resultSet);
                return user;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        return null;
    }


    @Override
    public <T extends DataSet> void save(T user) throws SQLException {
        StringBuilder SQLQuery = new StringBuilder("insert into " + DBHelper.getDbTableName(user.getClass()));
        SQLQuery.append("(" + DBHelper.getFields(user.getClass()) + ") values (");
        SQLQuery.append(DBHelper.getFieldValues(user));
        SQLQuery.append(");");
        Executor.update(connection, SQLQuery.toString());
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> cls) throws SQLException {
        String SQLQuery = "select * from " + DBHelper.getDbTableName(cls) + " where id=" + id;
        return Executor.query(connection, SQLQuery, ((resultSet) -> {
            return createUser(resultSet, cls);
        }));
    }

}
