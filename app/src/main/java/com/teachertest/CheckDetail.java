package com.teachertest;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.ApplicationUser;
import com.example.vic_sun.fsc.MyApplication;
import com.example.vic_sun.fsc.R;
import com.parenttest.LoginActivity;
import com.server.WebServerHelp;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckDetail extends Activity {
    TextView tv_parentname ;
    TextView tv_parenttel ;
    TextView tv_stuname ;
    TextView tv_stuclass ;
    TextView tv_stunum ;
    EditText et_comment ;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_test);

        Bundle bundle = this.getIntent().getExtras();

        tv_parentname = (TextView) findViewById(R.id.textView_parent_name);
        tv_parenttel = (TextView) findViewById(R.id.textView_parenttel);
        tv_stuname = (TextView) findViewById(R.id.textView_stuname);
        tv_stuclass = (TextView) findViewById(R.id.textView_stuclass);
        tv_stunum = (TextView) findViewById(R.id.textView_stunum);
        et_comment = (EditText) findViewById(R.id.textView_comment) ;


        Button bt_hand = (Button) findViewById(R.id.button_hand);

        tv_parentname.setText(bundle.getString("parent_name"));
        tv_parenttel.setText(bundle.getString("parenttel"));
        tv_stuname.setText(bundle.getString("stuname"));
        tv_stuclass.setText(bundle.getString("stuclass"));
        tv_stunum.setText(bundle.getString("stunum"));
        et_comment.setText(bundle.getString("comment"));


        MyClickListener mc = new MyClickListener();
        bt_hand.setOnClickListener(mc);

        handler = new Handler() {
            private Intent intent;

            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x123:
                        Toast.makeText(CheckDetail.this,
                                "提交成功", Toast.LENGTH_LONG)
                                .show();
                        Intent intent = new Intent(CheckDetail.this, CheckList.class);
                        startActivity(intent);
                    case 0x121:
                        Toast.makeText(CheckDetail.this,
                                "提交失败", Toast.LENGTH_LONG)
                                .show();
                }
            }
        };

    }

    class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View V) {
            if (V.getId() == R.id.button_hand && ((RadioButton)findViewById(R.id.radioButton1)).isChecked()) {
                ApplicationUser user;
                MyApplication mApplication;

                mApplication = (MyApplication) getApplication();
                user = mApplication.getUser();

                Long userid = user.getUser_id();
                String password = user.getUser_pwd();
                int flag = user.getUser_flag();
                String action = "checked";
                String student_num = tv_stunum.getText().toString();
                String parent_tel = tv_parenttel.getText().toString();
                String comment = et_comment.getText().toString();

                final JSONObject handJson = new JSONObject();
                try {
                    handJson.put("userid", userid);
                    handJson.put("password", password);
                    handJson.put("flag", flag);
                    handJson.put("action", action);
                    handJson.put("student_num", student_num);
                    handJson.put("parent_tel", parent_tel);
                    handJson.put("comment", comment);

                } catch (Exception e) {
                    // TODO: handle exception
                }
                final String content = String.valueOf(handJson);

                new Thread() {
                    public void run() {
                        try {
                            URL url = new URL(WebServerHelp.getURL() + "Checked");
                            HttpURLConnection conn = (HttpURLConnection) url
                                    .openConnection();
                            conn.setConnectTimeout(5000);
                            conn.setDoOutput(true);
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

                                handler.sendEmptyMessage(0x123); //提交成功


                            } else {
                                handler.sendEmptyMessage(0x121); //错误

                            }

                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    };
                }.start();

            }
            else if (((RadioButton)findViewById(R.id.radioButton2)).isChecked()){
                Intent intent = new Intent(CheckDetail.this, CheckList.class);
                startActivity(intent);
            }

        }

    }
}
