package com.server;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by vic-sun on 2017/7/31.
 */

public class GetImage {

    public static Bitmap getInternetPicture(String UrlPath) {
        Bitmap bm = null;

        String urlpath = WebServerHelp.getIP() + "/FSCServer/image/"  + UrlPath;

        try {
            URL uri = new URL(urlpath);


            HttpURLConnection connection = (HttpURLConnection) uri.openConnection();

            connection.setRequestMethod("GET");

            connection.setReadTimeout(5000);
            // 设置连接超时
            connection.setConnectTimeout(5000);
            connection.connect();


            if (connection.getResponseCode() == 200) {

                InputStream is = connection.getInputStream();

                bm = BitmapFactory.decodeStream(is);


            } else {

                bm = null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;

    }

    public static Bitmap GetImageBitmap(String url) {

        Bitmap tmpBitmap = null;
        try {
            InputStream is = new java.net.URL(WebServerHelp.getIP() + "/FSCServer/image/" + url).openStream();
            tmpBitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpBitmap;

    }
}
