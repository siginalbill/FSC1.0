package com.leadertest;

/**
 * Created by hxy on 2017/8/1.
 */
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vic_sun.fsc.R;
import com.server.GetImage;

public class education_news_detail extends Activity {
    TextView tv_title ;
    TextView tv_date ;
    TextView tv_content ;
    ImageView iv;
    Handler handler;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educationnews_details);
        final Bundle bundle = this.getIntent().getExtras();


        tv_title = (TextView) findViewById(R.id.Activity_EducationNews_Detailsils_Item_Title);
        tv_date = (TextView) findViewById(R.id.Activity_EducationNews_Details_TextView_date);
        tv_content = (TextView) findViewById(R.id.Activity_EducationNews_Details_TextView_Content);
        iv = (ImageView) findViewById(R.id.Activity_EducationNews_Detailsils_Item_ImageView) ;


        tv_title.setText(bundle.getString("news_title"));
        tv_date.setText(bundle.getString("news_date"));
        tv_content.setText(bundle.getString("news_content"));

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x123) {
                    iv.setImageBitmap(bmp);


                }

            }
        };
        new Thread() {
            public void run() {
                try {
                    bmp = GetImage.getInternetPicture(bundle.getString("news_img"));
                } catch (Exception e) {

                }
                handler.sendEmptyMessage(0x123);

            }
        }.start();
    }



}