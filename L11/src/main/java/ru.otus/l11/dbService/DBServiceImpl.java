package ru.otus.l11.dbService;

import ru.otus.l11.base.DBService;
import ru.otus.l11.dbcommon.DBHelper;
import ru.otus.l11.base.dataSets.DataSet;
import ru.otus.l11.dbService.dao.UserDAO;
import ru.otus.l11.dbService.dao.UserExecutorDAO;
import ru.otus.l11.dbcommon.DDLService;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBServiceImpl implements DBService {
    private final Connection connection;
    private final Class<? extends DataSet> dataSetClasses[];

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

    public DBServiceImpl(Connection connection, Class<? extends DataSet>... dataSetClasses) throws SQLException {
        this.connection = connection;
        this.dataSetClasses=dataSetClasses;
        DDLService ddlService=new DDLService(connection);
        for (Class<?> dataSetClass : dataSetClasses)
            ddlService.createTable(dataSetClass);

    }

    @Override
    public void shutdown() throws SQLException {
        DDLService ddlService=new DDLService(connection);
        for (Class<? extends DataSet> dataSetClass : dataSetClasses)
            ddlService.dropTable(dataSetClass);


    }
}




