package com.parenttest;

/**
 * Created by hxy on 2017/8/1.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.bean.ApplicationUser;
import com.example.vic_sun.fsc.MyApplication;
import com.example.vic_sun.fsc.R;
import com.server.GetImage;
import com.server.WebServerHelp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class release_message_main extends Activity {
    private MyApplication mApplication;
    ApplicationUser user;
    Handler handler;
    JSONArray messagelistJson;
    Bitmap[] images;
    TextView hand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.release_message_person);

        mApplication = (MyApplication) getApplication();
        user = mApplication.getUser();

        hand = (TextView)findViewById(R.id.activity_gerenliuyan_textview_huifu);
        MyClickListener mc = new MyClickListener();
        hand.setOnClickListener(mc);

        ImageButton ib_back = (ImageButton)findViewById(R.id.activity_gerenliuyan_imagebutton_back);
        ib_back.setOnClickListener(mc);

        final JSONObject requestJson = new JSONObject();
        try {

            requestJson.put("user_id", user.getUser_id());
            requestJson.put("user_flag", user.getUser_flag());
            requestJson.put("user_pwd", user.getUser_pwd());
            //action需要确认
            requestJson.put("action", "messageList");

        } catch (Exception e) {
            e.printStackTrace();

        }
        final String content = String.valueOf(requestJson);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x123) {
                    messagelistJson = (JSONArray) msg.obj;

                    //定义listview
                    ListView lv_safe = (ListView) findViewById(R.id.activity_gerenliuyan_listview_huifu);
                    GeneralAdapter generalAdapt = new GeneralAdapter();
                    lv_safe.setAdapter(generalAdapt);
                    lv_safe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            // TODO Auto-generated method stub
                            Intent intent0 = new Intent(release_message_main.this, release_message_detail.class);
                            Bundle bundle = new Bundle();
                            try{
                                bundle.putString("message_title", ((JSONObject)messagelistJson.get(position)).getString("message_title"));
                                bundle.putString("message_date", ((JSONObject)messagelistJson.get(position)).getString("message_date"));
                                bundle.putString("message_writer", ((JSONObject)messagelistJson.get(position)).getString("message_writer"));
                                bundle.putString("message_content", ((JSONObject)messagelistJson.get(position)).getString("message_content"));
                                bundle.putString("message_img", ((JSONObject)messagelistJson.get(position)).getString("message_img"));
                                bundle.putInt("writer_flag", ((JSONObject)messagelistJson.get(position)).getInt("message_writerflag"));
                                bundle.putString("parent_img", ((JSONObject)messagelistJson.get(position)).getString("parent_img"));
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            intent0.putExtras(bundle);
                            startActivity(intent0);
                        }
                    });

                }

            }
        };

        new Thread() {
            public void run() {
                Message message = handler.obtainMessage();
                try {
                    URL url = new URL(WebServerHelp.getURL() + "MessageList");
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

                    images = new Bitmap[rJson.length()];
                    for (int i = 0; i < rJson.length(); i++){
                        images[i] = GetImage.getInternetPicture(((JSONObject)rJson.get(i)).getString("parent_img"));
                    }

                    message.obj = rJson;
                    message.what = 0x123;

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
            if (messagelistJson == null) {
                return 0;
            } else {
                return messagelistJson.length();
            }
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public JSONObject getJsonItem(int position) {
// TODO Auto-generated method stub
            try {
                return (JSONObject) messagelistJson.get(position);
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


        //简单来说就是拿到单行的一个布局，然后根据不同的数值，填充主要的listView的每一个item
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //拿到ListViewItem的布局，转换为View类型的对象
            View layout = View.inflate(release_message_main.this, R.layout.release_message_voice, null);

            //

            ImageView ivhead = (ImageView) layout.findViewById(R.id.activity_liuyan_yuyin_imageview_touxiang);
            TextView tvname = (TextView) layout.findViewById(R.id.activity_liuyan_yuyin_textview_name);
            TextView tvtitle = (TextView) layout.findViewById(R.id.title) ;
            TextView tvcontent = (TextView) layout.findViewById(R.id.activity_liuyan_wenzi_textview_content);
            TextView tvdate = (TextView) layout.findViewById(R.id.activity_pingyu_yuyin_time);
            TextView tvstate = (TextView) layout.findViewById(R.id.activity_liuyan_yuyin_textview_nametail);
            try{
                String parent_img = ((JSONObject) getJsonItem(position)).getString("parent_image");
            }catch (Exception e){
                e.printStackTrace();
            }
            try {

                String message_title = ((JSONObject) getJsonItem(position)).getString("message_title");
                String message_writer = ((JSONObject) getJsonItem(position)).getString("message_writer");
                String message_date = ((JSONObject) getJsonItem(position)).getString("message_date");
                String message_content = ((JSONObject) getJsonItem(position)).getString("message_content");
                int message_writerflag = ((JSONObject) getJsonItem(position)).getInt("message_writerflag");
                String state = null;
                if (message_writerflag == 0) {
                    state = "家长";
                } else if (message_writerflag == 1) {
                    state = "学生";
                } else if (message_writerflag == 2) {
                    state = "老师";
                } else if (message_writerflag == 3) {
                    state = "领导";
                }

                tvname.setText(message_writer);
                if (message_content.length()>=40){
                    tvcontent.setText(message_content.substring(0,40));
                }
                else {
                    tvcontent.setText(message_content);
                }

                tvdate.setText(message_date);
                tvstate.setText(state);
                tvtitle.setText(message_title);
                if (images[position] != null){
                    ivhead.setImageBitmap(images[position]);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return layout;
        }


    }
    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View V) {
            if (V.getId() == R.id.activity_gerenliuyan_textview_huifu) {
                Intent intent = new Intent(release_message_main.this, Message_Hand.class);

                startActivity(intent);
            }
            else if (V.getId() == R.id.activity_gerenliuyan_imagebutton_back){
                finish();
            }

        }

    }
}
