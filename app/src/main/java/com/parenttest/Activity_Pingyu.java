package com.parenttest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.vic_sun.fsc.R;

public class Activity_Pingyu extends Activity {
	private ListView lv;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> list;
	private int[] images = { R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher };
	private String[] names = { "����", "����", "����" };
	private HashMap<String, Object> map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pingyu);
		lv = (ListView) findViewById(R.id.activity_pingyu_listview);
		list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < names.length; i++) {
			map = new HashMap<String, Object>();
			map.put("img", images[i]);
			map.put("name", names[i]);
			map.put("desc", "2017/7/" + i);
			map.put("content", names[i]);
			list.add(map);

		}
		String[] from = { "img", "name", "desc", "content" };
		int[] to = { R.id.activity_pingyu_adapter_pic,
				R.id.activity_pingyu_adapter_name,
				R.id.activity_pingyu_adapter_time,
				R.id.activity_pingyu_adapter_content };
		adapter = new SimpleAdapter(this, list,
				R.layout.activity_pingyu_adapter_item, from, to);
		lv.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
