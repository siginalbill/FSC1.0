package com.parenttest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.vic_sun.fsc.R;
import com.teachertest.CheckList;


/**
 * Created by hxy on 2017/7/29.
 */

public class Mainpage_litter_parent extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_litter_parent_mainpage);

        ImageView iv_addcheck = (ImageView) findViewById(R.id.Activity_Parent_Mainpage_ImageView_More);
        ImageView iv_video = (ImageView) findViewById(R.id.Activity_Parent_Mainpage_ImageView_Function2) ;
        ImageView iv_others = (ImageView) findViewById(R.id.Activity_Parent_Mainpage_ImageView_Function8) ;

        MyClickListener mc = new MyClickListener();

        iv_addcheck.setOnClickListener(mc);
        iv_video.setOnClickListener(mc);
        iv_others.setOnClickListener(mc);

    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View V) {
            if (V.getId() == R.id.Activity_Parent_Mainpage_ImageView_More) {
                Intent intent = new Intent(Mainpage_litter_parent.this, Activity_HandText.class);
                startActivity(intent);

            }
            else if (V.getId() == R.id.Activity_Parent_Mainpage_LinearLayout_Function2){

            }
            else {

            }
        }
    }
}
