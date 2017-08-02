package com.teachertest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.bean.ApplicationUser;
import com.example.vic_sun.fsc.MyApplication;
import com.example.vic_sun.fsc.R;
import com.server.WebServerHelp;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class homework_edit extends Activity {
    private Spinner sp_choice;
    private Button btn;
    private EditText fabu_homework_content;
    private Handler handler;
    int u_flag = 0;
    ApplicationUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fabu_homework);
        MyApplication mApplication = (MyApplication)getApplication();
        user = mApplication.getUser();

        fabu_homework_content = (EditText)findViewById(R.id.fabu_homework_content);
        btn = (Button) findViewById(R.id.fabu_homework_button);
        MyClickListener mc = new MyClickListener();
        btn.setOnClickListener(mc);

        sp_choice = (Spinner) findViewById(R.id.fabu_homework_spinner);
        sp_choice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                u_flag = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fabu_homework_content = (EditText) findViewById(R.id.fabu_homework_content);

        handler = new Handler() {
            private Intent intent;
            private Toast tst;

            public void handleMessage(Message msg) {
                if (msg.what == 0x123) {
                    Toast.makeText(homework_edit.this, "提交成功",
                            Toast.LENGTH_LONG).show();
                    intent = new Intent(homework_edit.this, HomeworkMainActivity.class);
                    startActivity(intent);
                } else if (msg.what == 0x122) {
                    tst = Toast.makeText(homework_edit.this, "更新成功",
                            Toast.LENGTH_SHORT);
                    tst.show();
                    intent = new Intent(homework_edit.this,
                            HomeworkMainActivity.class);
                    startActivity(intent);
                }

            }
        };


    }

    class MyClickListener implements View.OnClickListener {

        @Override

        public void onClick(View V) {
            if (V.getId() == R.id.fabu_homework_button) {

                String content = fabu_homework_content.getText().toString();


                JSONObject handJson = new JSONObject();
                try {
                    handJson.put("user_id", user.getUser_id());
                    handJson.put("user_flag", user.getUser_flag());
                    handJson.put("user_pwd", user.getUser_pwd());
                    handJson.put("action", "handHomework");
                    handJson.put("major",u_flag );
                    handJson.put("content", content);

                } catch (Exception e) {
                    // TODO: handle exception
                }
                final String h_content = String.valueOf(handJson);

                new Thread() {
                    public void run() {
                        try {
                            URL url = new URL(WebServerHelp.getURL() + "HandHomework");
                            HttpURLConnection conn = (HttpURLConnection) url
                                    .openConnection();
                            conn.setConnectTimeout(5000);
                            conn.setDoOutput(true);
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type",
                                    "application/json; charset=UTF-8");
                            OutputStream os = conn.getOutputStream();
                            os.write(h_content.getBytes());
                            os.close();
                            if (conn.getResponseCode() == 200) {

                            } else {
                                handler.sendEmptyMessage(0x121);
                            }
                            InputStream inStream = conn.getInputStream();
                            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                            int len = 0;
                            byte[] data = new byte[512];
                            while ((len = inStream.read(data)) != -1) {
                                outStream.write(data, 0, len);
                            }
                            inStream.close();
                            String returnString = new String(
                                    outStream.toByteArray());
                            JSONObject rJson = new JSONObject(returnString);
                            if (rJson.getInt("check_flag") == 1) {
                                if (rJson.getInt("reason") == 1) {

                                    handler.sendEmptyMessage(0x122);
                                }else{
                                    handler.sendEmptyMessage(0x123);
                                }

                            } else {
                                handler.sendEmptyMessage(0x121);

                            }

                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    };
                }.start();

            }

        }

    }

}