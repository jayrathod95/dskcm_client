package com.deskcomm.core;

import java.sql.SQLException;

/**
 * Created by Jay Rathod on 07-02-2017.
 */
public interface Persistent {
    boolean insertToTable() throws SQLException, ClassNotFoundException;
    Object getUpdater();

    boolean fetchFromDb();
}
