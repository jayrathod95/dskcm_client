package com.deskcomm.support;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jay Rathod on 05-02-2017.
 */
public class PathDecoder {
    private PathDecoder() {
    }


    public static Map<Integer, String> getPathParams(String string) {
        char[] chars = string.toCharArray();
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        Integer j = 0;
        for (int i = 0; i < chars.length; ) {
            StringBuilder buffer = new StringBuilder();
            while (i < chars.length && chars[i++] != '/') {
                buffer.append(chars[i - 1]);
            }
            map.put(j++, buffer.toString());
        }
        return map;
    }
}
