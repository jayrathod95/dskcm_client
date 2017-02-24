package com.deskcomm.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 15-01-2017.
 */
public class DbConnection {
    public static final String DATABASE_NAME = "test2.db";
    final static public String JDBC_SQLITE_URL = "jdbc:sqlite:" + DATABASE_NAME;

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
