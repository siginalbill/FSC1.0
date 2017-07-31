package com.server;

/**
 * Created by vic-sun on 2017/7/28.
 */

public class WebServerHelp {
    //protected static String IP = "http://192.168.1.101:8080";
    //protected static String IP = "http://127.0.0.1:8080";
    //protected static String IP = "http://172.18.165.2:8080";
    protected static String IP = "http://192.168.1.166:8080";
    //protected static String IP = "http://192.168.48.38:8080";

    protected static String URL = IP + "/FSCServer/servlet/";

    public static String getURL(){
        return URL;
    }

    public static String getIP(){
        return IP;
    }
}
