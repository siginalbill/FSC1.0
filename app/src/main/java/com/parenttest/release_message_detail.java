package com.parenttest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vic_sun.fsc.R;
import com.server.GetImage;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by hxy on 2017/8/1.
 */

public class release_message_detail extends Activity{
    TextView tv_name, tv_type, tv_date, tv_title, tv_content;
    ImageView iv_head, iv_image;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        Bundle bundle = this.getIntent().getExtras();

        final String message_title = bundle.getString("message_title");
        final String message_date = bundle.getString("message_date");
        final String message_writer = bundle.getString("message_writer");
        final String message_content = bundle.getString("message_content");
        final String message_img = bundle.getString("message_img");
        final String parent_img = bundle.getString("parent_img");
        final int message_writerflag = bundle.getInt("message_writerflag");

        tv_name = (TextView)findViewById(R.id.activity_message_textview_name);
        tv_type = (TextView)findViewById(R.id.activity_message_textview_nametail);
        tv_date = (TextView)findViewById(R.id.activity_message_time);
        tv_title = (TextView)findViewById(R.id.activity_message_title);
        tv_content = (TextView)findViewById(R.id.activity_message_content);

        iv_head = (ImageView)findViewById(R.id.activity_message_head);
        iv_image = (ImageView)findViewById(R.id.activity_message_img);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x123) {
                    tv_name.setText(message_writer);
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
                    tv_type.setText(state);
                    tv_content.setText(message_content);
                    tv_title.setText(message_title);
                    tv_date.setText(message_date);


                }

            }
        };

        new Thread() {
            public void run() {
                try {
                    iv_head.setImageBitmap(GetImage.getInternetPicture(parent_img));
                    iv_image.setImageBitmap(GetImage.getInternetPicture(message_img));
                } catch (Exception e) {

                }
                handler.sendEmptyMessage(0x123);

            }
        }.start();
    }
}
