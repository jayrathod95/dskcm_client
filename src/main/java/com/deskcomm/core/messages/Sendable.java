package com.deskcomm.core.messages;

import java.sql.SQLException;

/**
 * Created by Jay Rathod on 10-02-2017.
 */
public interface Sendable {
    void send() throws SQLException, ClassNotFoundException;
}
