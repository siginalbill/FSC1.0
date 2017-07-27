package com.server;

/**
 * Created by hxy on 2017/7/27.
 */
import android.os.Handler;
import android.os.Message;

import com.bean.News_bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class GetAndParseJson {
    private String url="http://192.168.48.22:8080/FSCServer/servlet/News";
    public static final int PARSESUCCWSS=0x2001;
    private Handler handler;
    public GetAndParseJson(Handler handler) {
        // TODO Auto-generated constructor stub
        this.handler=handler;
    }
    /**
     * 获取网络上的XML
     */
    public void getJsonFromInternet () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    HttpURLConnection conn=(HttpURLConnection) new URL(url).openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode()==200) {
                        InputStream inputStream=conn.getInputStream();
                        List<News_bean>listNews=parseJson(inputStream);
                        if (listNews.size()>0) {
                            Message msg=new Message();
                            msg.what=PARSESUCCWSS;//通知UI线程Json解析完成
                            msg.obj=listNews;//将解析出的数据传递给UI线程
                            handler.sendMessage(msg);
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();

    }
    /**
     * 解析json格式的输入流转换成List
     * @param inputStream
     * @return List
     */
    protected List<News_bean> parseJson(InputStream inputStream) {
        // TODO Auto-generated method stub
        List<News_bean>listNews=new ArrayList<News_bean>();
        byte[]jsonBytes=convertIsToByteArray(inputStream);
        String json=new String(jsonBytes);
        try {
            JSONArray jsonArray=new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jObject=jsonArray.getJSONObject(i);
                long id=jObject.getLong("news_id");
                String title=jObject.getString("news_title");
                String content=jObject.getString("news_content");
                String date=jObject.getString("news_date");
                News_bean news=new News_bean(id, title, content, date);
                listNews.add(news);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return listNews;
    }
    /**
     * 将输入流转化成ByteArray
     * @param inputStream
     * @return ByteArray
     */
    private byte[] convertIsToByteArray(InputStream inputStream) {
        // TODO Auto-generated method stub
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        byte buffer[]=new byte[1024];
        int length=0;
        try {
            while ((length=inputStream.read(buffer))!=-1) {
                baos.write(buffer, 0, length);
            }
            inputStream.close();
            baos.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
}