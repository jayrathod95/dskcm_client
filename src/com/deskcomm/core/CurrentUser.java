package com.deskcomm.core;

import com.deskcomm.db.tables.UserPrefsTable;
import com.deskcomm.resources.Keys;

import java.sql.SQLException;

/**
 * Created by Jay Rathod on 17-01-2017.
 */
public class CurrentUser {
    public static boolean isLoggedIn() {
        try {
            UserPrefsTable table = new UserPrefsTable();
            if (table.fetchData())
                return table.getLoginStatus().equals(Keys.LOGGED_IN);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean logout() {
        try {
            UserPrefsTable table = new UserPrefsTable();
            return table.clearRecord();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean login(String email, String password) {

        return false;
    }
}
