package com.deskcomm.core.bookkeeping;

import java.util.List;

/**
 * Created by Jay Rathod on 07-02-2017.
 */
public class BookKeeper {
    private static List<Object> userUpdater;

    public static UsersUpdater getUsersUpdater() {
        return new UsersUpdater();
    }


}
