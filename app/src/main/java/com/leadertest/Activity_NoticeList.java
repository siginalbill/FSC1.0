package com.leadertest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bean.ApplicationUser;
import com.example.vic_sun.fsc.MyApplication;
import com.example.vic_sun.fsc.R;
import com.server.WebServerHelp;

public class Activity_NoticeList extends Activity {
    Handler handler;

    JSONArray uncheck;
    MyApplication mApplication;
    ApplicationUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_teacher);

        mApplication = (MyApplication) getApplication();
        user = mApplication.getUser();
        final JSONObject requestJson = new JSONObject();
        try {
            requestJson.put("user_id", user.getUser_id());
            requestJson.put("user_flag", user.getUser_flag());
            requestJson.put("user_pwd", user.getUser_pwd());
            requestJson.put("action", "InformList");

        } catch (Exception e) {
            e.printStackTrace();

        }
        final String content = String.valueOf(requestJson);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x121) {
                    uncheck = (JSONArray)msg.obj;
                    GeneralAdapter generalAdapt = new GeneralAdapter();
                    ListView lv_name = (ListView) findViewById(R.id.Notice_ListView_WorkList);
                    lv_name.setAdapter(generalAdapt);
                    lv_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(Activity_NoticeList.this, Activity_Notice_Detail.class);
                            Bundle bundle = new Bundle();
                            try {
                                bundle.putString("inform_title",
                                        ((JSONObject) uncheck.get(position))
                                                .getString("inform_title"));
                                bundle.putString("inform_date",
                                        ((JSONObject) uncheck.get(position))
                                                .getString("inform_date"));
                                bundle.putString("inform_writer",
                                        ((JSONObject) uncheck.get(position))
                                                .getString("inform_writer"));
                                bundle.putString("inform_content",
                                        ((JSONObject) uncheck.get(position))
                                                .getString("inform_content"));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            intent.putExtras(bundle);
                            startActivity(intent);

                        }

                    });

                }

            }

        };
        new Thread() {
            public void run() {
                Message message = handler.obtainMessage();
                try {
                    URL url = new URL(WebServerHelp.getURL() + "InformList");
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
                    JSONArray jmp = new JSONArray(returnString);

                    message.what = 0x121;

                    if (jmp == null) {
                        message.what = 0x122;

                        handler.sendMessage(message);

                    } else {
                        message.obj = jmp;
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
            if (uncheck == null) {
                return 0;
            } else {
                return uncheck.length();
            }
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public JSONObject getJsonItem(int position) {
            // TODO Auto-generated method stub
            try {
                return (JSONObject) uncheck.get(position);
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

            View layout = View.inflate(Activity_NoticeList.this,
                    R.layout.notice_dataview, null);

            TextView title = (TextView) layout
                    .findViewById(R.id.Notice_TextView_title);
            TextView data = (TextView) layout
                    .findViewById(R.id.Notice_TextView_data);


            try {
                String inform_title  = ((JSONObject) getJsonItem(position))
                        .getString("inform_title");
                title.setText(inform_title);
                String inform_date = ((JSONObject) getJsonItem(position))
                        .getString("inform_date");
                data.setText(inform_date);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return layout;
        }

    }


}
