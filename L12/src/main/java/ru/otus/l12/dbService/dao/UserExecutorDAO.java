package ru.otus.l12.dbService.dao;

import ru.otus.l12.dbcommon.DBHelper;
import ru.otus.l12.executors.Executor;
import ru.otus.l12.base.DataSet;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserExecutorDAO implements UserDAO {
    private final Connection connection;

    public UserExecutorDAO(Connection connection) {
        this.connection = connection;
    }

    private <T> T createUser(ResultSet resultSet, Class<T> userClass) throws SQLException {
        if (resultSet.next())
            try {
                T user = userClass.getConstructor().newInstance();
                DBHelper.initJavaObjectFields(user, userClass, resultSet);
                DBHelper.initJavaObjectFields(user, userClass.getSuperclass(), resultSet);
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


    private <T extends DataSet> boolean isExists(T user) throws SQLException {
        String queryCount = DBHelper.dbSelectCountQuery(user.getClass(), "id=" + user.getId());
        Integer cnt = Executor.query(connection, queryCount, ((resultSet) -> {
            resultSet.next();
            return resultSet.getInt("cnt");
        }));
        if (cnt == 0)
            return false;
        else
            return true;
    }

    @Override
    public <T extends DataSet> void save(T user) throws SQLException {
        String querySave = "";
        if (user.getId() == 0 || !isExists(user))
            querySave = DBHelper.dbInsertQuery(user);
        else
            querySave = DBHelper.dbUpdateQuery(user, user.getId());
        Executor.update(connection, querySave);
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> userClass) throws SQLException {
        String querySelect = DBHelper.dbSelectQuery(userClass, "id=" + id);
        return Executor.query(connection, querySelect, ((resultSet) -> {
            return createUser(resultSet, userClass);
        }));
    }

    @Override
    public <T extends DataSet> T load(String login, Class<T> cls) throws SQLException {
        throw new UnsupportedOperationException("Operation <load by login> not suppported");
    }


    @Override
    public <T extends DataSet> void update(T user) throws SQLException {
        throw new UnsupportedOperationException("Operation <update> not suppported");
    }

    @Override
    public <T extends DataSet> List<T> load(Class<T> cls) throws SQLException {
        throw new UnsupportedOperationException("Operation <execute> not suppported");
    }

    @Override
    public long count() throws SQLException {
        throw new UnsupportedOperationException("Operation <count> not suppported");
    }
}
