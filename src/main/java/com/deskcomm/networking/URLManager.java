package com.deskcomm.networking;

/**
 * Created by Jay Rathod on 18-01-2017.
 */
public class URLManager {
    public static String baseUrl = "localhost:8080";
    public static String TARGET_URL1 = "http://httpbin.org";
    public static String TARGET_URL2 = "http://" + baseUrl + "/dskcm_rest/webapi";
    public static String TARGET_URL = "http://" + baseUrl + "/dskcm_rest/webapi";
    public static String TARGET_URL_WS = "ws://" + baseUrl + "/dskcm_rest/ws";
}
