package com.deskcomm.core;

import org.junit.Test;

/**
 * Created by Jay Rathod on 08-02-2017.
 */
public class GroupTest {
    @Test
    public void insertToTable() throws Exception {
        Group group = new Group("xxxxxxx", "aaaaaaaa", "uuuuuuuuuuu", new User("121212"), "aaaaaaaa", new User("1111111"), new User("1111111"));
        System.out.println(group.getUpdater().updateIconUrl("this is new name for group"));
    }

}