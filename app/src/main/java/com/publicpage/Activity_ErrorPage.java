package com.publicpage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bean.ApplicationUser;
import com.example.vic_sun.fsc.MyApplication;
import com.example.vic_sun.fsc.R;

public class Activity_ErrorPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_page);

        MyApplication myApplication = (MyApplication) getApplication();
        ApplicationUser user = myApplication.getUser();

        TextView tv = (TextView)findViewById(R.id.Activity_Parent_Mainpage_TextView_EducationNewsLeader);

        if (user.getUser_flag() == 0){
            tv.setText("家长用户");
        }
        else if (user.getUser_flag() == 1){
            tv.setText("学生用户");
        }
        else if (user.getUser_flag() == 2){
            tv.setText("老师用户");
        }
        else if (user.getUser_flag() == 3){
            tv.setText("领导用户");
        }

        ImageView btn = (ImageView)findViewById(R.id.Activity_newinformImageView_Left);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
