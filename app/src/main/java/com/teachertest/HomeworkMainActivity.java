package com.teachertest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bean.ApplicationUser;
import com.example.vic_sun.fsc.MyApplication;
import com.example.vic_sun.fsc.R;
import com.server.WebServerHelp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeworkMainActivity extends Activity {
    private MyApplication mApplication;
    ApplicationUser user;
    Handler handler;
    JSONArray homeworkJson;
    TextView tv_hand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_main);

        mApplication = (MyApplication) getApplication();
        user = mApplication.getUser();
        MyClickListener mc = new MyClickListener();

        ImageView iv_back = (ImageView) findViewById(R.id.homework_back);
        iv_back.setOnClickListener(mc);

        tv_hand = (TextView)findViewById(R.id.activity_handhomework_hand);
        tv_hand.setOnClickListener(mc);

        final JSONObject requestJson = new JSONObject();
        try {

            requestJson.put("user_id", user.getUser_id());
            requestJson.put("user_flag", user.getUser_flag());
            requestJson.put("user_pwd", user.getUser_pwd());

            requestJson.put("action", "getHomework");

        } catch (Exception e) {
            e.printStackTrace();

        }
        final String content = String.valueOf(requestJson);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x121) {
                    homeworkJson = (JSONArray) msg.obj;
                    GeneralAdapter generalAdapt = new GeneralAdapter();

                    ListView lv_homework = (ListView) findViewById(R.id.homework_listView);
                    lv_homework.setAdapter(generalAdapt);
                    lv_homework.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(HomeworkMainActivity.this, homework_detail.class);
                            Bundle bundle = new Bundle();
                            try{
                                bundle.putString("homework_id", ((JSONObject)homeworkJson.get(position)).getString("homework_id"));
                                bundle.putString("homework_date", ((JSONObject)homeworkJson.get(position)).getString("homework_date"));
                                bundle.putString("chinese", ((JSONObject)homeworkJson.get(position)).getString("chinese"));
                                bundle.putString("english", ((JSONObject)homeworkJson.get(position)).getString("english"));
                                bundle.putString("math", ((JSONObject)homeworkJson.get(position)).getString("math"));


                            }catch (Exception e){
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
                    URL url = new URL(WebServerHelp.getURL() + "Homework");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
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
                    JSONArray rJson = new JSONArray(returnString);

                    message.obj = rJson;
                    message.what = 0x121;

                    if (rJson == null) {
                        handler.sendEmptyMessage(0x122);

                    } else {
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
            if (homeworkJson == null) {
                return 0;
            } else {
                return homeworkJson.length();
            }
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public JSONObject getJsonItem(int position) {
// TODO Auto-generated method stub
            try {
                return (JSONObject) homeworkJson.get(position);
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

            View layout = View.inflate(HomeworkMainActivity.this, R.layout.homework_listview, null);

            TextView homework_date = (TextView) layout.findViewById(R.id.homework_firstdate);


            try {

                String date = ((JSONObject) getJsonItem(position)).getString("homework_date");
                homework_date.setText(date);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return layout;
        }


    }
    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View V) {
            if (V.getId() == R.id.activity_handhomework_hand) {
                Intent intent = new Intent(HomeworkMainActivity.this, homework_edit.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivity(intent);
            }
            else if (V.getId() == R.id.homework_back){
                finish();
            }

        }

    }
}
