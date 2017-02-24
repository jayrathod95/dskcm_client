package com.deskcomm.support;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jay_rathod on 19-02-2017.
 */
public class TimeStampParser {
    public static Date parse(String string) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
            return dateFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
