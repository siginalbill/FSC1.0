package com.teachertest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;



import org.json.JSONObject;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bean.ApplicationUser;
import com.example.vic_sun.fsc.MyApplication;
import com.example.vic_sun.fsc.R;
import com.server.WebServerHelp;

public class Activity_EditRemark extends Activity {
    private EditText xuehao, biaoti, zhengwen;
    private Button btn;
    private MyApplication mApplication;
    ApplicationUser user;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pingyu_bianjipingyu);

        xuehao = (EditText) findViewById(R.id.activity_bianjipingyu_edittext_shuruxuehao);
        biaoti = (EditText) findViewById(R.id.activity_bianjipingyu_edittext_biaoti);
        zhengwen = (EditText) findViewById(R.id.activity_bianjipingyu_edittext_zhengwen);
        btn = (Button) findViewById(R.id.activity_bianjipingyu_button_fabu);
        mApplication = (MyApplication) getApplication();
        user = mApplication.getUser();
        MyClickListener mc = new MyClickListener();
        btn.setOnClickListener(mc);

        handler = new Handler() {
            private Intent intent;
            private Toast tst;

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x123) {
                    Toast.makeText(Activity_EditRemark.this, "发布成功",
                            Toast.LENGTH_LONG).show();
                    intent = new Intent(Activity_EditRemark.this,
                            Activity_EditRemark.class);
                    startActivity(intent);
                } else if (msg.what == 0x122) {
                    tst = Toast.makeText(Activity_EditRemark.this,
                            "学生姓名或学号错误", Toast.LENGTH_SHORT);
                    tst.show();
                } else if (msg.what == 0x121) {
                    tst = Toast.makeText(Activity_EditRemark.this, "其他",
                            Toast.LENGTH_SHORT);
                    tst.show();
                }

            }

        };

    }

    class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View V) {
            if (V.getId() == R.id.activity_bianjipingyu_button_fabu) {
                String xh = xuehao.getText().toString();
                String bt = biaoti.getText().toString();
                String zw = zhengwen.getText().toString();

                final JSONObject handJson = new JSONObject();
                try {
                    handJson.put("student_num", xh);
                    handJson.put("remark_title", bt);
                    handJson.put("remark_content", zw);
                    handJson.put("user_id", user.getUser_id());
                    handJson.put("user_flag", user.getUser_flag());
                    handJson.put("user_pwd", user.getUser_pwd());
                    handJson.put("action", "HandRRemark");
                } catch (Exception e) {
                    // TODO: handle exception
                }
                final String content = String.valueOf(handJson);

                new Thread() {

                    public void run() {

                        try {

                            URL url = new URL(WebServerHelp.getURL() + "HandRemark");
                            HttpURLConnection conn = (HttpURLConnection) url
                                    .openConnection();
                            conn.setConnectTimeout(5000);
                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type",
                                    "application/json; charset=UTF-8");
                            OutputStream os = conn.getOutputStream();
                            os.write(content.getBytes());
                            os.close();

                            if (conn.getResponseCode() == 200) {

                            } else {
                                handler.sendEmptyMessage(0x121);
                            }
                            InputStream inStream = conn.getInputStream();
                            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                            int len = 0;
                            byte[] data = new byte[1024];
                            while ((len = inStream.read(data)) != -1) {
                                outStream.write(data, 0, len);
                            }
                            inStream.close();
                            String returnString = new String(
                                    outStream.toByteArray());
                            JSONObject rJson = new JSONObject(returnString);

                            if (rJson.getInt("check_flag") == 1) {
                                handler.sendEmptyMessage(0x123);
                            } else {
                                if (rJson.getInt("reason") == 1) {
                                    handler.sendEmptyMessage(0x122);
                                } else {

                                    handler.sendEmptyMessage(0x121);
                                }
                            }

                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                    }
                }.start();
            }

        }
    }

}
