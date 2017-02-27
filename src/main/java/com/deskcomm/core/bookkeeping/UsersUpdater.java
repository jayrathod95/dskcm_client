package com.deskcomm.core.bookkeeping;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.core.User;
import com.deskcomm.db.DbConnection;
import javafx.concurrent.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 07-02-2017.
 */
public class UsersUpdater {

    private static UsersUpdater usersUpdater = null;

    public static UsersUpdater getInstance() {
        if (usersUpdater == null) usersUpdater = new UsersUpdater();
        return usersUpdater;
    }

    public boolean updateAllUsers(JSONArray jsonArrayUsers) {
        boolean b = false;
        try {
            deleteAllUsers();
            for (int i = 0; i < jsonArrayUsers.length(); i++) {
                JSONObject jsonObjectUser = jsonArrayUsers.getJSONObject(i);

                User user = new User(jsonObjectUser);
                if (user.getUuid().equals(CurrentUser.getInstance().getUuid())) continue;
                b = user.insertToTable();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    public boolean updateAllUsersAsync(JSONArray jsonArrayUsers) {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    deleteAllUsers();
                    for (int i = 0; i < jsonArrayUsers.length(); i++) {
                        JSONObject jsonObjectUser = jsonArrayUsers.getJSONObject(i);
                        User user = new User(jsonObjectUser);
                        if (user.getUuid().equals(CurrentUser.getInstance().getUuid())) continue;
                        boolean b = user.insertToTable();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread(task).start();
        boolean b = false;

        return b;
    }


    private boolean deleteAllUsers() {
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM users");
            statement.executeUpdate();
            int updateCount = statement.getUpdateCount();
            statement.close();
            connection.close();
            return updateCount > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
