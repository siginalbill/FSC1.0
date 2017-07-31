package com.parenttest;

/**
 * Created by hxy on 2017/7/31.
 */

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.vic_sun.fsc.R;

public class safe_school_detail extends Activity {
    TextView tv_flag ;
    TextView tv_date ;
    TextView tv_com ;
    TextView tv_left ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safe_school_detail);
        Bundle bundle = this.getIntent().getExtras();

        tv_flag = (TextView) findViewById(R.id.safe_school_detail_student_state);
        tv_date = (TextView) findViewById(R.id.safe_school_detail_student_date);
        tv_com = (TextView) findViewById(R.id.safe_school_detail_student_com);
        tv_left = (TextView) findViewById(R.id.safe_school_detail_student_left);

        String state = null;
        int attendence_flag = bundle.getInt("attendence_flag");
        if(attendence_flag == 0){
            state="正常";
        }else  if(attendence_flag == 1){
            state="缺勤";
        }else if(attendence_flag == 2){
            state="迟到";
        }else if(attendence_flag == 3){
            state="早退";
        }

        tv_flag.setText(state);
        tv_date.setText(bundle.getString("attendence_date"));
        tv_com.setText(bundle.getString("attendence_com"));
        tv_left.setText(bundle.getString("attendence_left"));



    }



}