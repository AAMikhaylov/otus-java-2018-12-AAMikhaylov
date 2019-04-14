package ru.otus.l12.dbService;

import ru.otus.l12.base.DataSet;
import ru.otus.l12.dbService.dao.UserDAO;
import ru.otus.l12.dbService.dao.UserExecutorDAO;
import ru.otus.l12.dbcommon.DDLService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DBServiceImpl implements DBService {
    private final Connection connection;
    private DDLService ddlService;

    @Override
    public <T extends DataSet> void save(T user) throws SQLException {
        UserDAO dao = new UserExecutorDAO(connection);
        dao.save(user);
    }


    @Override
    public <T extends DataSet> T load(long id, Class<T> cls) throws SQLException {
        UserDAO dao = new UserExecutorDAO(connection);
        return dao.load(id, cls);

    }

    public DBServiceImpl(Connection connection, DDLService ddlService, Class<? extends DataSet>... dataSetClasses) throws SQLException {
        this.connection = connection;
        this.ddlService = ddlService;
        ddlService.createTables(dataSetClasses);
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
        throw new UnsupportedOperationException("Operation <load all> not suppported");
    }

    @Override
    public void shutdown() throws SQLException {
        ddlService.dropTables();
    }

}




