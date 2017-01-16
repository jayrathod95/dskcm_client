package com.deskcomm.db;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 15-01-2017.
 */
public class DbConnection {
    final static private String JDBC_SQLITE_URL = "jdbc:sqlite:main.db";

    private DbConnection() {
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection(JDBC_SQLITE_URL);
    }
}
