package com.studenttest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.vic_sun.fsc.R;

public class StudentMainPageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_mainpage);
    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View V) {
            if (V.getId() == R.id.Rigister_Button_Rigister) {

            }
        }
    }
}
