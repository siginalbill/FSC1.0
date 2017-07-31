package com.studenttest;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vic_sun.fsc.R;

public class Activity_Mark_Student_Page extends Activity {
	private TextView Chinese, English, Math, classrank, graderank, gradename;
	private Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mark_student_page);

		Bundle bundle = this.getIntent().getExtras();

		Chinese = (TextView) findViewById(R.id.Acticity_Mark_Student_Page_TextView_Chinese);
		English = (TextView) findViewById(R.id.Acticity_Mark_Student_Page_TextView_English);
		Math = (TextView) findViewById(R.id.Acticity_Mark_Student_Page_TextView_Maths);
		classrank = (TextView) findViewById(R.id.Acticity_Mark_Student_Page_TextView_classrank);
		graderank = (TextView) findViewById(R.id.Acticity_Mark_Student_Page_TextView_graderank);
		gradename = (TextView) findViewById(R.id.Activity_Mark_Student_Page_TextView_Gradename);

		Chinese.setText("语文:  " + bundle.getInt("grade_chinese"));
		English.setText("英语:  " + bundle.getInt("grade_english"));
		Math.setText("数学:  " + bundle.getInt("grade_math"));
		classrank.setText("班级排名:  " + bundle.getInt("grade_classrank"));
		graderank.setText("年级排名:  " + bundle.getInt("grade_graderank"));
		gradename.setText(bundle.getString("grade_name"));



	}
}
