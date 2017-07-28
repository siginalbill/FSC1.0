package com.parenttest;


import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends Activity {
    Button btn;
    EditText et_id, et_pwd;
    Toast tst;
    Intent intent;
    Handler handler;
    TextView tv_regist;
    MyApplication mApplication;
    ApplicationUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MyClickListener mc = new MyClickListener();

        btn = (Button) findViewById(R.id.Button_Login);
        btn.setOnClickListener(mc);
        tv_regist = (TextView) findViewById(R.id.Button_Login) ;
        btn.setOnClickListener(mc);

        mApplication = (MyApplication) getApplication();
        handler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                int flag = msg.what;
                if(flag==0)
                {
                    Intent intent=new Intent(LoginActivity.this, ParentMainPageActivity.class);
                    startActivity(intent);
                }
                else if(flag==1)
                {
                    Intent intent=new Intent(LoginActivity.this, ParentMainPageActivity.class);
                    startActivity(intent);
                }
                else if(flag==2)
                {
                    Intent intent=new Intent(LoginActivity.this, ParentMainPageActivity.class);
                    startActivity(intent);
                }
                else if(flag==3)
                {
                    Intent intent=new Intent(LoginActivity.this, Mainpage_leader.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(LoginActivity.this,
                            "登录失败", Toast.LENGTH_LONG)
                            .show();
                }
            }
        };
    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View V) {
            if (V.getId() == R.id.Button_Login) {
                et_id = (EditText) findViewById(R.id.activity_login_edittext_zhanghao);
                et_pwd = (EditText) findViewById(R.id.activity_login_edittext_mima);
                String u_id = (String) et_id.getText().toString();
                String u_pwd = (String) et_pwd.getText().toString();
                int u_flag = 3;
                String u_action = "login";
                if (true) {
                    final JSONObject loginJson = new JSONObject();
                    try {
                        loginJson.put("username", u_id);
                        loginJson.put("password", u_pwd);
                        loginJson.put("flag", u_flag);
                        loginJson.put("action", u_action);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    /*
                     * 验证
                     */
                    final String content = String.valueOf(loginJson);
                    new Thread() {
                        public void run() {
                            try {
                                URL url = new URL(WebServerHelp.getURL() + "Login");
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
                                    handler.sendEmptyMessage(4);
                                }
                                InputStream inStream = conn.getInputStream();
                                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                                int len = 0;
                                byte[] data = new byte[1024];
                                while ((len = inStream.read(data)) != -1) {
                                    outStream.write(data, 0, len);
                                }
                                inStream.close();
                                String returnString = new String(outStream.toByteArray());
                                JSONObject rJson = new JSONObject(returnString);
                                user = new ApplicationUser();
                                user.setUser_id(rJson.getLong("user_id"));
                                user.setUser_pwd(rJson.getString("user_pwd"));
                                user.setUser_flag(rJson.getInt("user_flag"));
                                user.setAction("login");
                                if (rJson.getInt("login_flag") == 1) {

                                    handler.sendEmptyMessage(user.getUser_flag());
                                    mApplication.setUserLogin(user);
                                } else {
                                    handler.sendEmptyMessage(4);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                } else {
                    intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }

            }
        }
    }
}
