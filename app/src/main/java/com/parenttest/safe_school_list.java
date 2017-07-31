package com.parenttest;

/**
 * Created by hxy on 2017/7/31.
 */

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

public class safe_school_list extends Activity {
    private MyApplication mApplication;
    ApplicationUser user;
    Handler handler;
    JSONArray safelistJson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.safe_school_listview);

        mApplication = (MyApplication) getApplication();
        user = mApplication.getUser();

        TextView tvtoday = (TextView)findViewById(R.id.activity_safe_school_showTexttodaydate);
        Date time = new java.sql.Date(new java.util.Date().getTime());
        String sDate = time.toString();
        tvtoday.setText(sDate);

        final JSONObject requestJson = new JSONObject();
        try {
            requestJson.put("user_id", user.getUser_id());
            requestJson.put("user_flag", user.getUser_flag());
            requestJson.put("user_pwd", user.getUser_pwd());
            //action需要确认
            requestJson.put("action", "safeschool");

        } catch (Exception e) {
            e.printStackTrace();

        }
        final String content = String.valueOf(requestJson);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0x121){
                    safelistJson = (JSONArray) msg.obj;

                    GeneralAdapter generalAdapt = new GeneralAdapter();
                    //定义listview
                    ListView lv_safe = (ListView) findViewById(R.id.activity_safe_school_listView);
                    lv_safe.setAdapter(generalAdapt);
                    lv_safe.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(safe_school_list.this, safe_school_detail.class);
                            Bundle bundle = new Bundle();
                            try{
                                bundle.putString("attendence_date", ((JSONObject)safelistJson.get(position)).getString("attendence_date"));
                                bundle.putInt("attendence_flag", ((JSONObject)safelistJson.get(position)).getInt("attendence_flag"));
                                bundle.putString("attendence_com", ((JSONObject)safelistJson.get(position)).getString("attendence_com"));
                                bundle.putString("attendence_left", ((JSONObject)safelistJson.get(position)).getString("attendence_left"));
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
                else{

                }
            }
        };

        new Thread() {
            public void run() {
                Message message = handler.obtainMessage();
                try {
                    URL url = new URL(WebServerHelp.getURL() + "SafeSchool");
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

        //得到listView中item的总数
        @Override
        public int getCount() {
// TODO Auto-generated method stub
            if (safelistJson == null){
                return 0;
            }else{
                return safelistJson.length();
            }
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public JSONObject getJsonItem(int position) {
// TODO Auto-generated method stub
            try {
                return (JSONObject) safelistJson.get(position);
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }


        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }


        //简单来说就是拿到单行的一个布局，然后根据不同的数值，填充主要的listView的每一个item
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //拿到ListViewItem的布局，转换为View类型的对象
            View layout = View.inflate(safe_school_list.this, R.layout.safe_school_listview_item, null);

            //

            TextView tvState = (TextView) layout.findViewById(R.id.activity_safe_school_state);
            TextView tvdate = (TextView) layout.findViewById(R.id.activity_safe_school_weekday);

            try{
                String attendence_date = ((JSONObject)getJsonItem(position)).getString("attendence_date");
                int attendence_flag = ((JSONObject)getJsonItem(position)).getInt("attendence_flag");
                String state=null;
                if(attendence_flag == 0){
                     state="正常";
                }else  if(attendence_flag == 1){
                    state="缺勤";
                }else if(attendence_flag == 2){
                    state="迟到";
                }else if(attendence_flag == 3){
                    state="早退";
                }

                tvdate.setText(attendence_date);
                tvState.setText(state);

            }catch (Exception e){
                e.printStackTrace();
            }


            return layout;
        }



    }

}