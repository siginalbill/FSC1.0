package com.parenttest;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vic_sun.fsc.R;

public class ParentTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_listview);
        // 创建ListView对象
        ListView listview = (ListView) findViewById(R.id.listView);
        // 定义动态数组
        ArrayList<HashMap<String, Object>> arryList = new ArrayList<HashMap<String, Object>>();
        // 往数组放入值
        for (int k = 1; k < 8; k++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("Img", R.drawable.parent);
            map.put("Username", "家长" + k);
            map.put("button", "点击审核");
            arryList.add(map);
        }
        // 定义适配器
        SimpleAdapter itemAdapter = new SimpleAdapter(this, arryList,
                R.layout.activity_check_listview_item, new String[] { "Img", "Username",
                "button" }, new int[]{R.id.img,R.id.username,R.id.button});
        //添加适配器
        listview.setAdapter(itemAdapter);
        listview.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                TextView tv=(TextView)arg1.findViewById(R.id.username);
                tv.setText("修改用户名");
                Toast.makeText(ParentTestActivity.this, "id="+arg2, Toast.LENGTH_LONG).show();
            }

        });

    }
    //长按菜单响应函数
    public boolean onContextItemSelected(MenuItem item){
        setTitle("点击了长按菜单里面的第"+item.getItemId()+"个项目");
        return super.onContextItemSelected(item);
    }

}
