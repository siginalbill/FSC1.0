package com.parenttest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.vic_sun.fsc.R;
import com.leadertest.education_news_main_others;
import com.studenttest.Mainpage_student;
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

        RelativeLayout ly = (RelativeLayout)findViewById(R.id.Activity_Parent_Mainpage_RelativeLayout_EducationNews) ;
        LinearLayout ly_news = (LinearLayout)findViewById(R.id.Activity_Parent_Mainpage_LinearLayout_Function7) ;

        MyClickListener mc = new MyClickListener();

        iv_addcheck.setOnClickListener(mc);
        iv_video.setOnClickListener(mc);
        iv_others.setOnClickListener(mc);
        ly.setOnClickListener(mc);
        ly_news.setOnClickListener(mc);

    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View V) {
            if (V.getId() == R.id.Activity_Parent_Mainpage_ImageView_More) {
                Intent intent = new Intent(Mainpage_litter_parent.this, Activity_HandText.class);
                startActivity(intent);

            }else if (V.getId() == R.id.Activity_Parent_Mainpage_LinearLayout_Function7) {
                Intent intent = new Intent(Mainpage_litter_parent.this, education_news_main_others.class);
                startActivity(intent);
            }

            else {
                Intent intent = new Intent(Mainpage_litter_parent.this, Activity_HandText.class);
                startActivity(intent);
            }
        }
    }
}
