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

import com.example.vic_sun.fsc.R;
import com.server.WebServerHelp;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends Activity {
    Button btn_regist,btn_check;
    EditText et_phone, et_password1, et_password2, et_check;
    TextView tv_login;
    Toast tst;
    Intent intent;
    Handler handler;
    String check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rigister);

        MyClickListener mc = new MyClickListener();

        btn_regist = (Button) findViewById(R.id.Rigister_Button_Rigister);
        btn_check = (Button) findViewById(R.id.Rigister_Button_getPIN);
        btn_regist.setOnClickListener(mc);
        btn_check.setOnClickListener(mc);
        tv_login = (TextView) findViewById(R.id.Rigister_TextView_Return);
        tv_login.setOnClickListener(mc);

        handler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                if (msg.what == 0x123)
                {
                    Toast.makeText(RegisterActivity.this,
                            "注册成功", Toast.LENGTH_LONG)
                            .show();
                    intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else if (msg.what == 0x122)
                {
                    tst = Toast.makeText(RegisterActivity.this, "该手机号已被注册", Toast.LENGTH_SHORT);
                    tst.show();
                }
                else if (msg.what == 0x121)
                {
                    tst = Toast.makeText(RegisterActivity.this, "服务异常", Toast.LENGTH_SHORT);
                    tst.show();
                }
                else if (msg.what == 0x120) {
                    tst = Toast.makeText(RegisterActivity.this, "密码不一致", Toast.LENGTH_SHORT);
                    tst.show();
                }
                else if (msg.what == 0x199) {
                    tst = Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT);
                    tst.show();
                }
                else{
                    tst = Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT);
                    tst.show();
                }
            }
        };
    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View V) {
            if (V.getId() == R.id.Rigister_Button_Rigister) {
                et_phone = (EditText) findViewById(R.id.Rigister_EditText_Phonenum);
                et_password1 = (EditText) findViewById(R.id.Rigister_EditText_EnterPassword);
                et_password2 = (EditText) findViewById(R.id.Rigister_EditText_ConfirmPassword);
                et_check = (EditText) findViewById(R.id.Rigister_EditText_PIN);

                String phonenum = (String) et_phone.getText().toString();
                String pass1 = (String) et_password1.getText().toString();
                String pass2 = (String) et_password2.getText().toString();
                String s_check = (String) et_check.getText().toString();
                if (pass1.equals(pass2) && check.equals(s_check)) {
                    final JSONObject registJson = new JSONObject();
                    try {
                        registJson.put("parent_tel", phonenum);
                        registJson.put("parent_pwd", pass1);
                        registJson.put("flag", 0);
                        registJson.put("action", "regist");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    /*
                     * 验证
                     */
                    final String content = String.valueOf(registJson);
                    new Thread() {
                        public void run() {
                            try {
                                URL url = new URL(WebServerHelp.getURL() + "Regist");
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
                                String returnString = new String(outStream.toByteArray());
                                JSONObject rJson = new JSONObject(returnString);
                                if(rJson.getInt("regist_flag") == 1){
                                    handler.sendEmptyMessage(0x123);
                                }
                                else{
                                    if (rJson.getInt("reason") == 1){

                                        handler.sendEmptyMessage(0x122);
                                    }
                                    else{

                                        handler.sendEmptyMessage(0x121);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                }
                else if (!pass1.equals(pass2)){
                    handler.sendEmptyMessage(0x120);
                }
                else {
                    handler.sendEmptyMessage(0x199);
                }

            } else if (V.getId() == R.id.Rigister_Button_getPIN) {
                check = String.valueOf((int)((Math.random()*9+1)*100000));
                btn_check.setText(check);
            }
            else if (V.getId() == R.id.Rigister_TextView_Return) {
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }
}
