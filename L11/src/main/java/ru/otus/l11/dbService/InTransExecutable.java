package ru.otus.l11.dbService;

import org.hibernate.Session;
import ru.otus.l11.base.dataSets.DataSet;

import java.sql.SQLException;

public interface InTransExecutable<T> {
    T executeInTrans(Session session) throws SQLException;
}
