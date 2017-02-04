package com.deskcomm.db.tables;

import java.sql.Timestamp;

/**
 * Created by Jay Rathod on 22-01-2017.
 */
public class UsersTable {
    private static Timestamp lastUpdate;
    private String uuid;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;

    public UsersTable(String uuid, String firstName, String lastName, String email, String mobile) {

        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
    }


    public static Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public static void setLastUpdate(Timestamp lastUpdate) {
        UsersTable.lastUpdate = lastUpdate;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
}
