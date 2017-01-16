package com.deskcomm.db.tables;

import com.deskcomm.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 16-01-2017.
 */
public class UserPrefsTable {
    private String sessionId = null;
    private String sessionId1 = null;
    private String loginStatus = null;
    private String _uuid = null;
    private String userName = null;
    private String firstName = null;
    private String lastName = null;
    private String email = null;
    private String mobile = null;
    private String created = null;


    public UserPrefsTable() throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS user_preferences(\n" +
                "  session_id TEXT NOT NULL,\n" +
                "  session_id1 TEXT NOT NULL ,\n" +
                "  login_status TEXT NOT NULL ,\n" +
                "  _uuid TEXT NOT NULL ,\n" +
                "  fname TEXT NOT NULL ,\n" +
                "  lname TEXT NOT NULL ,\n" +
                "  email TEXT NOT NULL ,\n" +
                "  mobile TEXT NOT NULL,\n" +
                "  img_url TEXT NOT NULL ,\n" +
                "  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL \n" +
                ")");
        preparedStatement.executeUpdate();
    }

    private boolean fetchData() {

    }


    public String getSessionId() {

        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId1() {
        return sessionId1;
    }

    public void setSessionId1(String sessionId1) {
        this.sessionId1 = sessionId1;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String get_uuid() {
        return _uuid;
    }

    public void set_uuid(String _uuid) {
        this._uuid = _uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
