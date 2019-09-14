package de.langomatisch.skyblock.core.database;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public class MySQLDatabase implements Database<ResultSet> {

    private Connection connection;
    private final String host, database, username, password;
    private final int port;

    @Override
    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager
                    .getConnection(String.format("jdbc:mysql://%s:%s/%s?"
                            + "user=%s&password=%s", host, port, database, username, password));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            if (connection.isClosed()) return;
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultSet query(String query, String... replacements) {
        try {
            return connection.prepareStatement(query, replacements).executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(String query, String... replacements) {
        try {
            connection.prepareStatement(query, replacements).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnected() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
