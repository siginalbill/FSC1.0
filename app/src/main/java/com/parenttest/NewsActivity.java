package com.parenttest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.bean.News_bean;
import com.example.vic_sun.fsc.R;
import com.server.GetAndParseJson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class NewsActivity extends Activity {
    private List<News_bean>listNews;
    private ListView list;
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case GetAndParseJson.PARSESUCCWSS:
                    listNews=(List<News_bean>) msg.obj;
                    initData();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educationnews_main);
        // 创建ListView对象
        ListView listview = (ListView) findViewById(R.id.Activity_EducationNews_Main_ListView);
        // 定义动态数组
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        // 往数组放入值
        HashMap<String, Object> map = new HashMap<String, Object>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educationnews_main);
        list=(ListView)findViewById(R.id.Activity_EducationNews_Main_ListView);
        GetAndParseJson getAndParseJson=new GetAndParseJson(mHandler);
        getAndParseJson.getJsonFromInternet();


    }
    /**
     * 将解析后的xml填充到ListView
     */

    protected void initData() {
        // TODO Auto-generated method stub
        List<Map<String, Object>>items=new ArrayList<Map<String,Object>>();
        for (News_bean news:listNews) {
            Map<String, Object>item=new HashMap<String, Object>();
            item.put("id", news.getNews_id());
            item.put("title", news.getNews_title());
            item.put("date", news.getNews_date());
            item.put("content", news.getNews_content());
            items.add(item);
        }
        SimpleAdapter adapter=new SimpleAdapter(this, items, R.layout.activity_educationnews_main_listview, new String[]
                {"id","title","date","content"}, new int[]{R.id.Activity_EducationNews_Main_ListView_Item_Id,R.id.Activity_EducationNews_Main_ListView_Item_Title,R.id.Activity_EducationNews_Main_ListView_Item_date,R.id.Activity_EducationNews_Main_ListView_Item_Content});
        list.setAdapter(adapter);
    }
    private String convertDate(Date publishDate) {
        // TODO Auto-generated method stub
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        return sdf.format(publishDate);
    }



}

