package com.teachertest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bean.ApplicationUser;
import com.example.vic_sun.fsc.MyApplication;
import com.example.vic_sun.fsc.R;
import com.server.WebServerHelp;

import org.json.JSONArray;
import org.json.JSONObject;

public class Activity_Remark extends Activity {
    Handler handler;
    MyApplication mApplication;
    ApplicationUser user;
    JSONArray list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pingyu);
        mApplication = (MyApplication) getApplication();
        user = mApplication.getUser();

        final JSONObject requestJson = new JSONObject();
        try {
            requestJson.put("user_id", user.getUser_id());
            requestJson.put("user_flag", user.getUser_flag());
            requestJson.put("user_pwd", user.getUser_pwd());
            requestJson.put("action", "RemarkList");

        } catch (Exception e) {
            e.printStackTrace();

        }
        final String content = String.valueOf(requestJson);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x121) {
                    list = (JSONArray)msg.obj;
                    GeneralAdapter generalAdapt = new GeneralAdapter();
                    ListView lv_name = (ListView) findViewById(R.id.activity_pingyu_listview);
                    lv_name.setAdapter(generalAdapt);

                }

            }

        };

        new Thread() {
            public void run() {
                Message message = handler.obtainMessage();
                try {
                    URL url = new URL(WebServerHelp.getURL() + "RemarkList");
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setDoOutput(true);// 设置允许输出
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type",
                            "application/json; charset=UTF-8"); // 内容类型
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
                    byte[] data = new byte[1024];
                    while ((len = inStream.read(data)) != -1) {
                        outStream.write(data, 0, len);
                    }
                    inStream.close();
                    String returnString = new String(outStream.toByteArray());
                    JSONArray uncheck = new JSONArray(returnString);

                    message.what = 0x121;

                    if (uncheck == null) {
                        handler.sendEmptyMessage(0x122);

                    } else {
                        message.obj = uncheck;
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0x122);
                }
            }
        }.start();

    }

    class GeneralAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (list == null) {
                return 0;
            } else {
                return list.length();
            }
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public JSONObject getJsonItem(int position) {
            // TODO Auto-generated method stub
            try {
                return (JSONObject) list.get(position);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View layout = View.inflate(Activity_Remark.this,
                    R.layout.activity_pingyu_adapter_item, null);

            TextView name = (TextView) layout
                    .findViewById(R.id.activity_pingyu_adapter_name);
            TextView time = (TextView) layout
                    .findViewById(R.id.activity_pingyu_adapter_time);
            TextView biaoti = (TextView) layout.findViewById(R.id.biaoti);
            TextView content = (TextView) layout
                    .findViewById(R.id.activity_pingyu_adapter_content);

            try {
                String remark_writer = ((JSONObject) getJsonItem(position))
                        .getString("remark_writer");
                name.setText(remark_writer);
                String remark_date = ((JSONObject) getJsonItem(position))
                        .getString("remark_date");
                time.setText(remark_date);
                String remark_title = ((JSONObject) getJsonItem(position))
                        .getString("remark_title");
                biaoti.setText(remark_title);
                String remark_content = ((JSONObject) getJsonItem(position))
                        .getString("remark_content");
                content.setText(remark_content);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return layout;
        }

    }

}
