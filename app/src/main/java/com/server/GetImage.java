package com.server;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by vic-sun on 2017/7/31.
 */

public class GetImage {
    protected Bitmap GetImage(String url) {

        Bitmap tmpBitmap = null;
        try {
            InputStream is = new java.net.URL(WebServerHelp.getIP() + "/image/" + url).openStream();
            tmpBitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpBitmap;

    }
}
