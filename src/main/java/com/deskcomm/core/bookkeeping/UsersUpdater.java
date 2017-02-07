package com.deskcomm.core.bookkeeping;

import com.deskcomm.core.User;

/**
 * Created by Jay Rathod on 07-02-2017.
 */
public class UsersUpdater {


    public Adder getAdder() {
        return new Adder();
    }

    public class Adder {
        public boolean add(User user) {
            return user.save();
        }
    }

    public class Modifier {
    }

    public class Dropper {

    }
}
