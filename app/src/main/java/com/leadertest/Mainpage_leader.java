package com.leadertest;

/**
 * Created by hxy on 2017/7/28.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bean.ApplicationUser;
import com.example.vic_sun.fsc.MyApplication;
import com.example.vic_sun.fsc.R;
import com.server.WebServerHelp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Mainpage_leader extends Activity {
    Toast tst;
    Handler handler;
    ImageView iv_add,iv_bell,iv_people;
    TextView tx_newslead, tx_newscontent, tx_informlead1, tx_informcontent1;
    ApplicationUser user;
    private MyApplication mApplication;
    JSONObject rJson;
    ImageView inform, ednews;
    Intent intent;
    RelativeLayout informfunction,newsfunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_mainpage);
        mApplication = (MyApplication) getApplication();

        MyClickListener mc = new MyClickListener();

        iv_bell = (ImageView)findViewById(R.id.Activity_Leader_Mainpage_ImageView_Bell);
        iv_people= (ImageView)findViewById(R.id.Activity_Leader_Mainpage_ImageView_People);
        iv_add=(ImageView)findViewById(R.id.Activity_Leader_Mainpage_ImageView_More);
        inform = (ImageView) findViewById(R.id.Acticity_Leader_Mainpage_ImageView_Function1);
        ednews = (ImageView) findViewById(R.id.Acticity_Leader_Mainpage_ImageView_Function2);
        informfunction=(RelativeLayout) findViewById((R.id.Acticity_Leader_Mainpage_RelativeLayout_LatestNews));
        newsfunction=(RelativeLayout) findViewById((R.id.Acticity_Leader_Mainpage_RelativeLayout_EducationNews));
        tx_newslead = (TextView) findViewById(R.id.Acticity_Leader_Mainpage_TextView_EducationNewsLeader);
        tx_newscontent = (TextView) findViewById(R.id.Acticity_Leader_Mainpage_TextView_EducationNewsDetails);
        tx_informlead1 = (TextView) findViewById(R.id.Acticity_Leader_Mainpage_TextView_LatestNewsLeader1);
        tx_informcontent1 = (TextView) findViewById(R.id.Acticity_Leader_Mainpage_TextView_EducationNewsLeader);

        iv_add.setOnClickListener(mc);
        iv_bell.setOnClickListener(mc);
        iv_people.setOnClickListener(mc);
        tx_newslead.setOnClickListener(mc);
        tx_newscontent.setOnClickListener(mc);
        tx_informlead1.setOnClickListener(mc);
        tx_informcontent1.setOnClickListener(mc);
        inform.setOnClickListener(mc);
        ednews.setOnClickListener(mc);
        informfunction.setOnClickListener(mc);
        newsfunction.setOnClickListener(mc);

        user = mApplication.getUser();

        final JSONObject requestJson = new JSONObject();
        try {
            requestJson.put("user_id", user.user_id);
            requestJson.put("user_flag", user.user_flag);
            requestJson.put("user_pwd", user.user_pwd);
            requestJson.put("action", "getMessage");


        } catch (Exception e) {
            e.printStackTrace();

        }
        //

        final String content = String.valueOf(requestJson);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        JSONObject rJsons = (JSONObject) msg.obj;
                        JSONObject news_json = null;
                        try {
                            news_json = rJsons.getJSONObject("news");
                            tx_newscontent.setText(news_json.getString("news_content"));
                            tx_newslead.setText(news_json.getString("news_title"));
                            JSONObject inform_json = rJsons.getJSONObject("inform");
                            tx_informcontent1.setText(inform_json.getString("inform_content"));
                            tx_informlead1.setText(inform_json.getString("inform_title"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                }
            }
        };
        new Thread() {


            public void run() {
                Message message = handler.obtainMessage();

                try {
                    URL url = new URL(WebServerHelp.getURL() + "MainPage");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setDoOutput(true);// 设置允许输出
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); // 内容类型
                    OutputStream os = conn.getOutputStream();
                    os.write(content.getBytes());
                    os.close();


                    if (conn.getResponseCode() == 200) {


                    } else {
                        handler.sendEmptyMessage(0x122);
                    }
                    InputStream inStream = conn.getInputStream();
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    int len = 0;
                    byte[] data = new byte[5120];
                    while ((len = inStream.read(data)) != -1) {
                        outStream.write(data, 0, len);

                    }
                    inStream.close();
                    String returnString = new String(outStream.toByteArray());
                    rJson = new JSONObject(returnString);//json
                    //                    JSONObject news_json = rJson.getJSONObject("news");
                    //                    tx_newscontent.setText(news_json.getString("news_content"));
                    //                    tx_newslead.setText(news_json.getString("news_title"));
                    //                    JSONObject inform_json = rJson.getJSONObject("inform");
                    //                    tx_informcontent1.setText(inform_json.getString("inform_content"));
                    //                    tx_informlead1.setText(inform_json.getString("inform_title"));


                    message.obj = rJson;
                    message.what = 1;

                    if (rJson == null) {
                        handler.sendMessage(message);
                        tst = Toast.makeText(Mainpage_leader.this, "那就失败了", Toast.LENGTH_SHORT);
                        tst.show();

                    } else {
                        handler.sendMessage(message);
                        tst = Toast.makeText(Mainpage_leader.this, "服务异常", Toast.LENGTH_SHORT);
                        tst.show();
                    }

                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View V) {
            if (V.getId() == R.id.Acticity_Leader_Mainpage_ImageView_Function1) {


            } else if(V.getId() == R.id.Acticity_Leader_Mainpage_ImageView_Function2){

            }else if(V.getId() == R.id.Acticity_Leader_Mainpage_RelativeLayout_EducationNews){

            }
            else if(V.getId() == R.id.Acticity_Leader_Mainpage_RelativeLayout_LatestNewsDetails){

            }
            else if(V.getId() == R.id.Activity_Leader_Mainpage_ImageView_People){

            }

            else if(V.getId() == R.id.Activity_Leader_Mainpage_ImageView_More){

            }

            else if(V.getId() == R.id.Activity_Leader_Mainpage_ImageView_Bell){

            }


        }
    }





}