package com.leadertest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.vic_sun.fsc.R;

public class Activity_Notice_Detail extends Activity {
    private TextView title, time, writer, content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_detile);
        Bundle bundle = this.getIntent().getExtras();

        title = (TextView)findViewById(R.id.notice_detile_tv_title);
        time = (TextView)findViewById(R.id.notice_detile_tv_time);
        writer = (TextView)findViewById(R.id.notice_detile_tv_name);
        content = (TextView)findViewById(R.id.notice_detile_tv_content);

        title.setText(bundle.getString("inform_title"));
        time.setText(bundle.getString("inform_date"));
        writer.setText(bundle.getString("inform_writer"));
        content.setText(bundle.getString("inform_content"));


    }
}
