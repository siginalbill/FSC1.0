package com.teachertest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vic_sun.fsc.R;


public class homework_detail extends Activity {
    TextView homework_dataview_date ;
    TextView homework_dataview_chinese ;
    TextView homework_dataview_chinese_date ;
    TextView homework_dataview_math ;
    TextView homework_dataview_math_date ;
    TextView homework_dataview_english ;
    TextView homework_dataview_english_date ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homework_dataview);
        Bundle bundle = this.getIntent().getExtras();

        homework_dataview_date = (TextView) findViewById(R.id.homework_dataview_date);
        homework_dataview_chinese = (TextView) findViewById(R.id.homework_dataview_chinese);

        homework_dataview_math = (TextView) findViewById(R.id.homework_dataview_math);

        homework_dataview_english = (TextView) findViewById(R.id.homework_dataview_english);


        homework_dataview_date.setText(bundle.getString("homework_date"));
        try{
            homework_dataview_chinese.setText(bundle.getString("chinese"));
            homework_dataview_math.setText(bundle.getString("math"));
            homework_dataview_english.setText(bundle.getString("english"));
        }catch (Exception e){

        }


        ImageView iv_back = (ImageView)findViewById(R.id.Activity_Mark_Student_Page_ImageView_Left);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }



}