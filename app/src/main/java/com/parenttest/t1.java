package com.parenttest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.vic_sun.fsc.R;


/**
 * Created by vicsun on 2017/11/25.
 */

public class t1 extends Activity {
    ImageView p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.t1);
        p = (ImageView)findViewById(R.id.p1);

        new Handler().postDelayed(new Runnable(){
            public void run() {
                p.setImageResource(R.drawable.t1);
            }
        }, 1900);



    }
}
