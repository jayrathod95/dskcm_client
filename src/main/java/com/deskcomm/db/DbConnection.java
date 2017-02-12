package com.deskcomm.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Jay Rathod on 15-01-2017.
 */
public class DbConnection {
    final static private String JDBC_SQLITE_URL = "jdbc:sqlite:" + UUID.randomUUID().toString() + ".db";

    private DbConnection() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return DriverManager.getConnection(JDBC_SQLITE_URL);
    }
}
