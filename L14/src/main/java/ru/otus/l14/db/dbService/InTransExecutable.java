package ru.otus.l14.db.dbService;

import org.hibernate.Session;

import java.sql.SQLException;

public interface InTransExecutable<T> {
    T executeInTrans(Session session) throws SQLException;
}
