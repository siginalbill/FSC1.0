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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.bean.ApplicationUser;
import com.example.vic_sun.fsc.MyApplication;
import com.example.vic_sun.fsc.R;
import com.parenttest.Mainpage_leader;
import com.server.WebServerHelp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckList extends Activity {
    private MyApplication mApplication;
    ApplicationUser user;

    Handler handler;

    JSONArray uncheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_listview);

        mApplication = (MyApplication) getApplication();
        user = mApplication.getUser();



        final JSONObject requestJson = new JSONObject();
        try {
            requestJson.put("user_id", user.getUser_id());
            requestJson.put("user_flag", user.getUser_flag());
            requestJson.put("user_pwd", user.getUser_pwd());
            requestJson.put("action", "checklist");

        } catch (Exception e) {
            e.printStackTrace();

        }
        final String content = String.valueOf(requestJson);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0x121){
                    JSONObject listJson = (JSONObject) msg.obj;

                    JSONArray checked;
                    try{
                        uncheck = (JSONArray)listJson.getJSONArray("uncheck");

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    GeneralAdapter generalAdapt = new GeneralAdapter();
                    ListView lv_name = (ListView) findViewById(R.id.listView);
                    lv_name.setAdapter(generalAdapt);
                    lv_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(CheckList.this, CheckDetail.class);
                            Bundle bundle = new Bundle();
                            try{
                                bundle.putString("parent_name", ((JSONObject)uncheck.get(position)).getString("parent_name"));
                                bundle.putString("parent_tel", ((JSONObject)uncheck.get(position)).getString("parent_tel"));
                                bundle.putString("student_name", ((JSONObject)uncheck.get(position)).getString("student_name"));

                                bundle.putString("student_num", ((JSONObject)uncheck.get(position)).getString("student_num"));
                                bundle.putString("comment", ((JSONObject)uncheck.get(position)).getString("comment"));
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
                    URL url = new URL(WebServerHelp.getURL() + "CheckList");
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
                    JSONObject rJson = new JSONObject(returnString);

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
            if (uncheck == null){
                return 0;
            }else{
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
            View layout = View.inflate(CheckList.this, R.layout.activity_check_listview_item, null);

            //显示名字
            TextView tvName = (TextView) layout.findViewById(R.id.username);
            try{
                String parent_name = ((JSONObject)getJsonItem(position)).getString("parent_name");
                tvName.setText(parent_name);
            }catch (Exception e){
                e.printStackTrace();
            }


            return layout;
        }

    }

}
