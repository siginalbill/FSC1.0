package com.parenttest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vic_sun.fsc.R;
import com.server.WebServerHelp;

public class Activity_HandText extends Activity {
	private EditText parentname;
	private EditText studentname;
	private EditText studentclass;
	private EditText studentnum;
	private EditText parentphone;
	private EditText comment;
	private EditText school;
	private Button btn;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_detail);

		parentname = (EditText) findViewById(R.id.shenhe_edittext_jiazhangxingming);
		studentname = (EditText) findViewById(R.id.shenhe_edittext_xueshengxingming);
		studentclass = (EditText) findViewById(R.id.shenhe_edittext_xueshengbanji);
		studentnum = (EditText) findViewById(R.id.shehe_xueshengxuehao);
		parentphone = (EditText) findViewById(R.id.shenhe_edittext_phonenum);
		comment = (EditText) findViewById(R.id.shenhe_edittext_beizhu);
		school = (EditText) findViewById(R.id.shenhe_edittext_xuexiao);
		btn = (Button) findViewById(R.id.shenhe_button_hand);
		handler = new Handler() {
			private Intent intent;
			private Toast tst;

			public void handleMessage(Message msg) {
				if (msg.what == 0x123) {
					Toast.makeText(Activity_HandText.this, "提交成功，请等待老师审核",
							Toast.LENGTH_LONG).show();
					intent = new Intent(Activity_HandText.this,
							Mainpage_litter_parent.class);
					startActivity(intent);
				} else if (msg.what == 0x122) {
					tst = Toast.makeText(Activity_HandText.this, "更新成功，请等待老师审核",
							Toast.LENGTH_SHORT);
					tst.show();
					intent = new Intent(Activity_HandText.this,
							Mainpage_litter_parent.class);
					startActivity(intent);
				} else if (msg.what == 0x121) {
					tst = Toast.makeText(Activity_HandText.this,
							"已审核或信息填写错误请联系老师或管理员", Toast.LENGTH_SHORT);
					tst.show();
				}

			}
		};

		btn.setOnClickListener(new MyClickListener());

	}

	class MyClickListener implements View.OnClickListener {

		@Override
		public void onClick(View V) {
			if (V.getId() == R.id.shenhe_button_hand) {
				String pn = parentname.getText().toString();
				String sn = studentname.getText().toString();
				String sc = studentclass.getText().toString();
				String snum = studentnum.getText().toString();
				String pp = parentphone.getText().toString();
				String c = comment.getText().toString();
				String s = school.getText().toString();

				final JSONObject handJson = new JSONObject();
				try {
					handJson.put("parent_name", pn);
					handJson.put("student_name", sn);
					handJson.put("class_name", sc);
					handJson.put("student_num", snum);
					handJson.put("uncheck_comment", c);
					handJson.put("school_name", s);
					handJson.put("parent_tel", pp);

				} catch (Exception e) {
					// TODO: handle exception
				}
				final String content = String.valueOf(handJson);

				new Thread() {
					public void run() {
						try {
							URL url = new URL(WebServerHelp.getURL() + "ReceiveCheck");
							HttpURLConnection conn = (HttpURLConnection) url
									.openConnection();
							conn.setConnectTimeout(5000);
							conn.setDoOutput(true);
							conn.setRequestMethod("POST");
							conn.setRequestProperty("Content-Type",
									"application/json; charset=UTF-8");
							OutputStream os = conn.getOutputStream();
							os.write(content.getBytes());
							os.close();
							if (conn.getResponseCode() == 200) {

							} else {
								handler.sendEmptyMessage(0x121);
							}
							InputStream inStream = conn.getInputStream();
							ByteArrayOutputStream outStream = new ByteArrayOutputStream();
							int len = 0;
							byte[] data = new byte[1024];
							while ((len = inStream.read(data)) != -1) {
								outStream.write(data, 0, len);
							}
							inStream.close();
							String returnString = new String(
									outStream.toByteArray());
							JSONObject rJson = new JSONObject(returnString);
							if (rJson.getInt("check_flag") == 1) {
								if (rJson.getInt("reason") == 1) { //正常

									handler.sendEmptyMessage(0x122); //更新成功
								}else{
									handler.sendEmptyMessage(0x123); //提交成功
								}

							} else {
								handler.sendEmptyMessage(0x121); //错误

							}

						} catch (Exception e) {
							// TODO: handle exception
						}
					};
				}.start();

			}

		}

	}

}
