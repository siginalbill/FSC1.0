package com.studenttest;

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
import com.leadertest.Activity_NoticeList;
import com.leadertest.education_news_main_others;
import com.parenttest.MediaPlayerDemo_Video;
import com.parenttest.release_message_main;
import com.parenttest.safe_school_list;
import com.publicpage.Activity_ErrorPage;
import com.publicpage.Activity_NewInform;
import com.server.WebServerHelp;
import com.teachertest.Activity_Remark;
import com.teachertest.HomeworkMainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Mainpage_student extends Activity {
    Toast tst;
    Handler handler;
    TextView tx_newslead, tx_newscontent, tx_informlead1, tx_informcontent1, tx_informlead2, tx_informcontent2, tx_messagelead1, tx_messagelead2, tx_messagecontent2, tx_messagecontent1;
    ImageView iv_add, iv_bell, iv_people;
    ApplicationUser user;
    private MyApplication mApplication;
    JSONObject rJson;
    ImageView homework, moniter, message, safeschool, grade, remark, ednews, more;
    Intent intent;
    RelativeLayout informfunction, newsfunction, messagefunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_mainpage);
        mApplication = (MyApplication) getApplication();

        MyClickListener mc = new MyClickListener();

        iv_add = (ImageView) findViewById(R.id.Activity_Student_Mainpage_ImageView_More) ;
        iv_bell = (ImageView) findViewById(R.id.Activity_Student_Mainpage_ImageView_Bell) ;
        iv_people = (ImageView) findViewById(R.id.Activity_Student_Mainpage_ImageView_People) ;

        homework = (ImageView) findViewById(R.id.Activity_Student_Mainpage_ImageView_Function1);
        moniter = (ImageView) findViewById(R.id.Activity_Student_Mainpage_ImageView_Function2);
        message = (ImageView) findViewById(R.id.Activity_Student_Mainpage_ImageView_Function3);
        safeschool = (ImageView) findViewById(R.id.Activity_Student_Mainpage_ImageView_Function4);
        grade = (ImageView) findViewById(R.id.Activity_Student_Mainpage_ImageView_Function5);
        remark = (ImageView) findViewById(R.id.Activity_Student_Mainpage_ImageView_Function6);
        ednews = (ImageView) findViewById(R.id.Activity_Student_Mainpage_ImageView_Function7);
        more = (ImageView) findViewById(R.id.Activity_Student_Mainpage_ImageView_Function8);

        newsfunction = (RelativeLayout) findViewById((R.id.Activity_Student_Mainpage_RelativeLayout_EducationNews));
        informfunction = (RelativeLayout) findViewById((R.id.message));
        messagefunction = (RelativeLayout) findViewById((R.id.Activity_Student_Mainpage_RelativeLayout_LatestNewsDetails));

        tx_newslead = (TextView) findViewById(R.id.Activity_Student_Mainpage_TextView_EducationNewsLeader);
        tx_newscontent = (TextView) findViewById(R.id.Activity_Student_Mainpage_TextView_EducationNewsDetails);
        tx_informlead1 = (TextView) findViewById(R.id.Activity_Student_Mainpage_TextView_LatestNewsLeader1);
        tx_informcontent1 = (TextView) findViewById(R.id.Activity_Student_Mainpage_TextView_LatestNewsDetails1);

        tx_messagelead1 = (TextView) findViewById(R.id.Activity_Student_Mainpage_TextView_ClassNoteLeader1);
        tx_messagecontent1 = (TextView) findViewById(R.id.Activity_Student_Mainpage_TextView_ClassNoteDetails1);
        tx_messagelead2 = (TextView) findViewById(R.id.Activity_Student_Mainpage_TextView_ClassNoteLeader2);
        tx_messagecontent2 = (TextView) findViewById(R.id.Activity_Student_Mainpage_TextView_ClassNoteDetails2);

        iv_add.setOnClickListener(mc);
        iv_bell.setOnClickListener(mc);
        iv_people.setOnClickListener(mc);
        homework.setOnClickListener(mc);
        moniter.setOnClickListener(mc);
        message.setOnClickListener(mc);
        safeschool.setOnClickListener(mc);
        grade.setOnClickListener(mc);
        remark.setOnClickListener(mc);
        ednews.setOnClickListener(mc);
        more.setOnClickListener(mc);


        ednews.setOnClickListener(mc);

        newsfunction.setOnClickListener(mc);

        informfunction.setOnClickListener(mc);

        messagefunction.setOnClickListener(mc);

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
                            JSONObject inform1_json = rJsons.getJSONObject("i1");
                            tx_informcontent1.setText(inform1_json.getString("inform_content"));
                            tx_informlead1.setText(inform1_json.getString("inform_title"));

                            JSONObject m1_json = rJsons.getJSONObject("m1");
                            tx_messagelead1.setText(m1_json.getString("message_title"));
                            tx_messagecontent1.setText(m1_json.getString("message_content"));
                            JSONObject m2_json = rJsons.getJSONObject("m2");
                            tx_messagelead2.setText(m2_json.getString("message_title"));
                            tx_messagecontent2.setText(m2_json.getString("message_content"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;


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
                        handler.sendEmptyMessage(2);


                    } else {
                        handler.sendMessage(message);

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
            if (V.getId() == R.id.Activity_Student_Mainpage_ImageView_Function1) {
                Intent intent = new Intent(Mainpage_student.this, HomeworkMainActivity.class);
                startActivity(intent);

            } else if (V.getId() == R.id.Activity_Student_Mainpage_ImageView_Function2) {
                final String MEDIA = "media";
                int STREAM_VIDEO = 5;
                Intent intent = new Intent(Mainpage_student.this, MediaPlayerDemo_Video.class);
                intent.putExtra(MEDIA, STREAM_VIDEO);
                startActivity(intent);
            } else if (V.getId() == R.id.Activity_Student_Mainpage_ImageView_Function3) {
                Intent intent = new Intent(Mainpage_student.this, release_message_main.class);
                startActivity(intent);
            } else if (V.getId() == R.id.Activity_Student_Mainpage_ImageView_Function4) {
                Intent intent = new Intent(Mainpage_student.this, safe_school_list.class);
                startActivity(intent);
            } else if (V.getId() == R.id.Activity_Student_Mainpage_ImageView_Function5) {
                Intent intent = new Intent(Mainpage_student.this, Activity_Matk_Mainpage.class);
                startActivity(intent);
            } else if (V.getId() == R.id.Activity_Student_Mainpage_ImageView_Function6) {
                Intent intent = new Intent(Mainpage_student.this, Activity_Remark.class);
                startActivity(intent);
            } else if (V.getId() == R.id.Activity_Student_Mainpage_ImageView_Function7) {
                Intent intent = new Intent(Mainpage_student.this, education_news_main_others.class);
                startActivity(intent);
            } else if (V.getId() == R.id.Activity_Student_Mainpage_ImageView_Function8) {
                Intent intent = new Intent(Mainpage_student.this, activity_allfunctions.class);
                startActivity(intent);
            } else if (V.getId() == R.id.Activity_Student_Mainpage_ImageView_People) {
                Intent intent = new Intent(Mainpage_student.this, Activity_ErrorPage.class);
                startActivity(intent);
            } else if (V.getId() == R.id.Activity_Student_Mainpage_ImageView_More) {
                Intent intent = new Intent(Mainpage_student.this, Activity_ErrorPage.class);
                startActivity(intent);
            } else if (V.getId() == R.id.Activity_Student_Mainpage_ImageView_Bell) {
                Intent intent = new Intent(Mainpage_student.this, Activity_NewInform.class);
                startActivity(intent);

            } else if (V.getId() == R.id.Activity_Student_Mainpage_RelativeLayout_LatestNewsDetails) {
                Intent intent = new Intent(Mainpage_student.this, Activity_NoticeList.class);
                startActivity(intent);

            } else if (V.getId() == R.id.Activity_Student_Mainpage_RelativeLayout_EducationNews) {
                Intent intent = new Intent(Mainpage_student.this, education_news_main_others.class);
                startActivity(intent);
            } else if (V.getId() == R.id.message) {
                Intent intent = new Intent(Mainpage_student.this, release_message_main.class);
                startActivity(intent);
            }
        }


    }
}