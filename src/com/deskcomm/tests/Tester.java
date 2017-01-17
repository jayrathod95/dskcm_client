package com.deskcomm.tests;

import com.deskcomm.db.tables.UserPrefsTable;
import com.deskcomm.support.Logger;

import java.sql.SQLException;

/**
 * Created by Jay Rathod on 16-01-2017.
 */
public class Tester {
    public void test() {
        try {
      //      UserPrefsTable table = new UserPrefsTable("sessionId", "sessionId", true, "asdasdasd", "Jay", "Rathod", "jay_rathod@live.com", null, null);
      //      Logger.print(table.insert());
            UserPrefsTable table=new UserPrefsTable();
            Logger.print(table.updateMobile("98822323343"));

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
