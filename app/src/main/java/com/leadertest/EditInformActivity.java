package com.leadertest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.vic_sun.fsc.R;

public class EditInformActivity extends Activity {
    EditText et_title,et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_edit);
    }
}
