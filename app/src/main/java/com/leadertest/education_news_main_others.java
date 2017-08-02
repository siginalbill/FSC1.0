package com.leadertest;

/**
 * Created by hxy on 2017/8/1.
 */


        import android.app.Activity;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.TextView;

        import com.bean.ApplicationUser;
        import com.example.vic_sun.fsc.MyApplication;
        import com.example.vic_sun.fsc.R;
        import com.server.GetImage;
        import com.server.WebServerHelp;

        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.io.ByteArrayOutputStream;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;

public class education_news_main_others extends Activity {
    private MyApplication mApplication;
    ApplicationUser user;
    Handler handler;
    JSONArray educationnewsJson;
    TextView tv_return;
    Bitmap[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educationnews_main);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x121) {
                    educationnewsJson = (JSONArray) msg.obj;
                    GeneralAdapter generalAdapt = new GeneralAdapter();
                    //定义listview
                    ListView lv_educationnews = (ListView) findViewById(R.id.Activity_EducationNews_Main_ListView);
                    lv_educationnews.setAdapter(generalAdapt);
                    lv_educationnews.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(education_news_main_others.this, education_news_detail.class);
                            Bundle bundle = new Bundle();
                            try{
                                bundle.putString("news_title", ((JSONObject)educationnewsJson.get(position)).getString("news_title"));
                                bundle.putString("news_date", ((JSONObject)educationnewsJson.get(position)).getString("news_date"));
                                bundle.putString("news_content", ((JSONObject)educationnewsJson.get(position)).getString("news_content"));
                                bundle.putString("news_img", ((JSONObject)educationnewsJson.get(position)).getString("news_img"));


                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });

                }

            }
        };

        new Thread() {
            public void run() {
                Message message = handler.obtainMessage();
                try {
                    URL url = new URL(WebServerHelp.getURL() + "News");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setDoOutput(true);// 设置允许输出
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); // 内容类型
                    OutputStream os = conn.getOutputStream();
                    os.close();

                    if (conn.getResponseCode() == 200) {

                    } else {
                        handler.sendEmptyMessage(0x122);
                    }
                    InputStream inStream = conn.getInputStream();
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    int len = 0;
                    byte[] data = new byte[5120];
                    while ((len = inStream.read(data)) != -1) {
                        outStream.write(data, 0, len);
                    }
                    inStream.close();
                    String returnString = new String(outStream.toByteArray());
                    JSONArray rJson = new JSONArray(returnString);

                    images = new Bitmap[rJson.length()];
                    for (int i = 0; i < rJson.length(); i++){
                        images[i] = GetImage.getInternetPicture(((JSONObject)rJson.get(i)).getString("news_img"));
                    }

                    message.obj = rJson;
                    message.what = 0x121;

                    if (rJson == null) {
                        handler.sendEmptyMessage(0x122);

                    } else {
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

        //得到listView中item的总数
        @Override
        public int getCount() {
// TODO Auto-generated method stub
            if (educationnewsJson == null) {
                return 0;
            } else {
                return educationnewsJson.length();
            }
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public JSONObject getJsonItem(int position) {
// TODO Auto-generated method stub
            try {
                return (JSONObject) educationnewsJson.get(position);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }


        //简单来说就是拿到单行的一个布局，然后根据不同的数值，填充主要的listView的每一个item
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //拿到ListViewItem的布局，转换为View类型的对象
            View layout = View.inflate(education_news_main_others.this, R.layout.activity_educationnews_main_listview, null);

            TextView tvtitle = (TextView) layout.findViewById(R.id.Activity_EducationNews_Main_ListView_Item_Title);
            TextView tvcontent = (TextView) layout.findViewById(R.id.Activity_EducationNews_Main_ListView_Item_Content);
            TextView tvdate = (TextView) layout.findViewById(R.id.Activity_EducationNews_Main_ListView_Item_date);
            ImageView iv = (ImageView) layout.findViewById(R.id.Activity_EducationNews_Main_ListView_Item_ImageView);

            try {

                String educationnews_title = ((JSONObject) getJsonItem(position)).getString("news_title");
                String educationnews_date = ((JSONObject) getJsonItem(position)).getString("news_date");
                String educationnews_content = ((JSONObject) getJsonItem(position)).getString("news_content");


                tvtitle.setText(educationnews_title);
                tvcontent.setText(educationnews_content.substring(0,40));
                tvdate.setText(educationnews_date);
                iv.setImageBitmap(images[position]);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return layout;
        }


    }


}
