package ru.otus.l11.executors;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Executor {
    public static void update(Connection connection, String query) throws SQLException {
        try (final Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
    }

    public static <T> T query(Connection connection, String query, TResultHandler<T> handler) throws SQLException {
        try (final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(query);
            return handler.handle(resultSet);
        }
    }
}
