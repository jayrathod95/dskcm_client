package com.deskcomm.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 16-01-2017.
 */
public interface Table {
    static void createMessagesPersonalTable() {
        System.out.println("Creating messages_personal Table");
        Connection connection = null;
        PreparedStatement statement;
        try {
            connection = DbConnection.getConnection();
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS messages_personal(" +
                    "_uuid TEXT CONSTRAINT users_pk PRIMARY KEY ," +
                    "data TEXT NOT NULL," +
                    " _to TEXT NOT NULL ," +
                    "_from TEXT NOT NULL ," +
                    "read INTEGER NOT NULL DEFAULT 0," +
                    "server_timestamp TIMESTAMP NOT NULL ," +
                    "created TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            statement.execute();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

    }

    static void createUsersTable() {
        Connection connection = null;
        PreparedStatement statement;
        try {
            connection = DbConnection.getConnection();
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS users(uuid TEXT CONSTRAINT users_pk PRIMARY KEY ,fname TEXT NOT NULL ,lname TEXT NOT NULL ,email TEXT NOT NULL ,mobile TEXT,img_url TEXT,gender TEXT NOT NULL, created TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            statement.execute();
            statement.close();
            connection.close();
            System.out.println("Created users Table");
        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    @SuppressWarnings("Duplicates")
    static void createUserPreferencesTable() throws SQLException {
        Connection connection = DbConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS user_preferences(\n" +
                "  session_id TEXT NOT NULL,\n" +
                "  session_id1 TEXT NOT NULL ,\n" +
                "  login_status TEXT NOT NULL DEFAULT 'NOT_LOGGED_IN',\n" +
                "  _uuid TEXT NOT NULL ,\n" +
                "  fname TEXT NOT NULL ,\n" +
                "  lname TEXT NOT NULL ,\n" +
                "  email TEXT NOT NULL ,\n" +
                "  mobile TEXT,\n" +
                "  gender TEXT,\n" +
                "  img_url TEXT ,\n" +
                "  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL \n" +
                ")");
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    boolean selectData();

    boolean updateProperty();

    boolean deleteData();
}
