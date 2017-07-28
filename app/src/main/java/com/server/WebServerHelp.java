package com.server;

/**
 * Created by vic-sun on 2017/7/28.
 */

public class WebServerHelp {
    protected static String IP = "http://192.168.1.101:8080";
    protected static String URL = IP + "/FSCServer/servlet/";

    public static String getURL(){
        return URL;
    }

    public static String getIP(){
        return IP;
    }
}
