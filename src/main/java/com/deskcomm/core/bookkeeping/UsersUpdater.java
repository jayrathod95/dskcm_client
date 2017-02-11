package com.deskcomm.core.bookkeeping;

import com.deskcomm.core.User;
import com.deskcomm.db.DbConnection;
import com.deskcomm.support.Keys;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 07-02-2017.
 */
public class UsersUpdater {

    public boolean updateUsers(JSONArray jsonArrayUsers) {
        boolean b = false;
        try {
            deleteAllUsers();
            for (int i = 0; i < jsonArrayUsers.length(); i++) {
                JSONObject jsonObjectUser = jsonArrayUsers.getJSONObject(i);
                User user = new User(jsonObjectUser);
                b = user.insertToTable();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }


    private boolean deleteAllUsers() throws SQLException, ClassNotFoundException {
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM users");
            statement.executeUpdate();
            int updateCount = statement.getUpdateCount();
            statement.close();
            connection.close();
            return updateCount > 0;
        } catch (SQLException e) {
            if (e.getMessage().contains(Keys.NO_SUCH_TABLE)) {
                createTable();
                return deleteAllUsers();
            } else throw e;
        }
    }

    public void createTable() {
        Connection connection = null;
        PreparedStatement statement;
        try {
            connection = DbConnection.getConnection();
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS users(uuid TEXT CONSTRAINT users_pk PRIMARY KEY ,fname TEXT NOT NULL ,lname TEXT NOT NULL ,email TEXT NOT NULL ,mobile TEXT,img_url TEXT,gender TEXT NOT NULL, created TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
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


}
