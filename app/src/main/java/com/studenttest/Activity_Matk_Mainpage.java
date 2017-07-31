package com.studenttest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bean.ApplicationUser;
import com.example.vic_sun.fsc.MyApplication;
import com.example.vic_sun.fsc.R;
import com.server.WebServerHelp;

public class Activity_Matk_Mainpage extends Activity {
	private MyApplication mApplication;
	ApplicationUser user;
	Handler handler;
	JSONArray uncheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mark_mainpage);

		mApplication = (MyApplication) getApplication();
		user = mApplication.getUser();


		final JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("user_id", user.getUser_id());
			requestJson.put("user_flag", user.getUser_flag());
			requestJson.put("user_pwd", user.getUser_pwd());
			requestJson.put("action", "GradeforParent");

		} catch (Exception e) {
			e.printStackTrace();

		}
		final String content = String.valueOf(requestJson);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0x123) {
					uncheck = (JSONArray) msg.obj;

					GeneralAdapter generalAdapt = new GeneralAdapter();
					ListView lv_name = (ListView) findViewById(R.id.Activity_Mark_Mainpage_ListView);
					lv_name.setAdapter(generalAdapt);
					lv_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						public void onItemClick(AdapterView<?> parent,
												View view, int position, long id) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(
									Activity_Matk_Mainpage.this,
									Activity_Mark_Student_Page.class);
							Bundle bundle = new Bundle();
							try {
								bundle.putString("grade_name",
										((JSONObject) uncheck.get(position))
												.getString("grade_name"));
								bundle.putInt("grade_chinese",
										((JSONObject) uncheck.get(position))
												.getInt("grade_chinese"));
								bundle.putInt("grade_english",
										((JSONObject) uncheck.get(position))
												.getInt("grade_english"));
								bundle.putInt("grade_math",
										((JSONObject) uncheck.get(position))
												.getInt("grade_math"));
								bundle.putInt("grade_classrank",
										((JSONObject) uncheck.get(position))
												.getInt("grade_classrank"));
								bundle.putInt("grade_graderank",
										((JSONObject) uncheck.get(position))
												.getInt("grade_graderank"));
							} catch (Exception e) {
								e.printStackTrace();
							}

							intent.putExtras(bundle);
							startActivity(intent);
						}
					});
				} else {

				}
			}
		};
		new Thread() {
			public void run() {

				try {
					URL url = new URL(WebServerHelp.getURL() + "GradeforParent");
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(5000);
					conn.setDoOutput(true);// 设置允许输出
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Type",
							"application/json; charset=UTF-8"); // 内容类型
					OutputStream os = conn.getOutputStream();
					os.write(content.getBytes());
					os.close();

					if (conn.getResponseCode() == 200) {

					} else {
						handler.sendEmptyMessage(0x122);
					}
					InputStream inStream = conn.getInputStream();
					ByteArrayOutputStream outStream = new ByteArrayOutputStream();
					int len = 0;
					byte[] data = new byte[1024];
					while ((len = inStream.read(data)) != -1) {
						outStream.write(data, 0, len);
					}
					inStream.close();
					String returnString = new String(outStream.toByteArray());
					JSONArray grades = new JSONArray(returnString);

					Message message = handler.obtainMessage();
					if (grades == null) {
						handler.sendEmptyMessage(0x122);

					} else {
						message.what = 0x123;
						message.obj = grades;
						handler.sendMessage(message);
					}

				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(0x122);
				}
			}
		}.start();

	}
	class GeneralAdapter extends BaseAdapter {


		@Override
		public int getCount() {
// TODO Auto-generated method stub
			if (uncheck == null){
				return 0;
			}else{
				return uncheck.length();
			}
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public JSONObject getJsonItem(int position) {
// TODO Auto-generated method stub
			try {
				return (JSONObject) uncheck.get(position);
			}catch (Exception e){
				e.printStackTrace();
				return null;
			}
		}


		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View layout = View.inflate(Activity_Matk_Mainpage.this, R.layout.mark_item, null);


			TextView tvName = (TextView) layout.findViewById(R.id.mark_item_gradename);
			try{
				JSONObject i = (JSONObject)(getJsonItem(position));
				String tv_gradename = i.getString("grade_name");
				tvName.setText(tv_gradename);
			}catch (Exception e){
				e.printStackTrace();
			}


			return layout;
		}

	}

}
