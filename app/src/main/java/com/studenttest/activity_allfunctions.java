package com.studenttest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bean.ApplicationUser;
import com.example.vic_sun.fsc.MyApplication;
import com.example.vic_sun.fsc.R;
import com.leadertest.Activity_Notice;
import com.leadertest.Activity_NoticeList;
import com.server.WebServerHelp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class activity_allfunctions extends Activity{
	
	ImageView iv_Left,iv_Bell,iv_People,iv_More;
	ImageView homework,monitor,message,safeschool,grade,remark,
	ed_news,sc_notice,pa_review,code_sharing,self_message;

	
	  @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_allfunctions);
	        
	        iv_Left=(ImageView)findViewById(R.id.Activity_LatestImageView_Left);
	        iv_Bell=(ImageView)findViewById(R.id.Activity_LatestImageView_Bell);
	        iv_People=(ImageView)findViewById(R.id.Activity_LatestNews_ImageView_People);
	        iv_More=(ImageView)findViewById(R.id.Activity_LatestNews_ImageView_More);
	        
	        homework=(ImageView)findViewById(R.id.Activity_AllFunctions_ImageView_Function1);
	        monitor=(ImageView)findViewById(R.id.Activity_AllFunctions_ImageView_Function2);
	        message=(ImageView)findViewById(R.id.Activity_AllFunctions_ImageView_Function3);
	        safeschool=(ImageView)findViewById(R.id.Activity_AllFunctions_ImageView_Function4);
	        grade=(ImageView)findViewById(R.id.Activity_AllFunctions_ImageView_Function5);
	        remark=(ImageView)findViewById(R.id.Activity_AllFunctions_ImageView_Function6);
	    	ed_news=(ImageView)findViewById(R.id.Activity_AllFunctions_ImageView_Function7);
	    	sc_notice=(ImageView)findViewById(R.id.Activity_AllFunctions_ImageView_Function8);
	    	pa_review=(ImageView)findViewById(R.id.Activity_AllFunctions_ImageView_Function9);
	    	code_sharing=(ImageView)findViewById(R.id.Activity_AllFunctions_ImageView_Function10);
	    	self_message=(ImageView)findViewById(R.id.Activity_AllFunctions_ImageView_Function11);
	      
	    	MyClickListener mc = new MyClickListener();
	    	
	    	iv_Left.setOnClickListener(mc);
	    	iv_Bell.setOnClickListener(mc);
	    	iv_People.setOnClickListener(mc);
	    	iv_More.setOnClickListener(mc);
	    	
	    	
	    	homework.setOnClickListener(mc);
	    	monitor.setOnClickListener(mc);
	    	message.setOnClickListener(mc);
	    	safeschool.setOnClickListener(mc);
	    	grade.setOnClickListener(mc);
	    	remark.setOnClickListener(mc);
	    	ed_news.setOnClickListener(mc);
	    	sc_notice.setOnClickListener(mc);
	    	pa_review.setOnClickListener(mc);
	    	code_sharing.setOnClickListener(mc);
	    	self_message.setOnClickListener(mc);
	    	

	    		
	    		
	  }
	class MyClickListener implements View.OnClickListener{
		@Override
		public void onClick(View V){
			if (V.getId() == R.id.Activity_LatestImageView_Left) {


			}else if(V.getId() == R.id.Activity_LatestImageView_Bell) {


			}else if(V.getId() == R.id.Activity_LatestNews_ImageView_People) {


			}else if(V.getId() == R.id.Activity_LatestNews_ImageView_More) {


			}else if(V.getId() == R.id.Activity_AllFunctions_ImageView_Function1) {


			}else if(V.getId() == R.id.Activity_AllFunctions_ImageView_Function2) {


			}else if(V.getId() == R.id.Activity_AllFunctions_ImageView_Function3) {


			}else if(V.getId() == R.id.Activity_AllFunctions_ImageView_Function4) {


			}else if(V.getId() == R.id.Activity_AllFunctions_ImageView_Function5) {


			}else if(V.getId() == R.id.Activity_AllFunctions_ImageView_Function6) {


			}else if(V.getId() == R.id.Activity_AllFunctions_ImageView_Function7) {


			}else if(V.getId() == R.id.Activity_AllFunctions_ImageView_Function8) {
				Intent intent = new Intent(activity_allfunctions.this, Activity_NoticeList.class);
				startActivity(intent);

			}else if(V.getId() == R.id.Activity_AllFunctions_ImageView_Function9) {


			}else if(V.getId() == R.id.Activity_AllFunctions_ImageView_Function10) {


			}else if(V.getId() == R.id.Activity_AllFunctions_ImageView_Function11) {


			}


		}

	}
}









