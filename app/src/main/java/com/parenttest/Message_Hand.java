package com.parenttest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.ApplicationUser;
import com.example.vic_sun.fsc.MyApplication;
import com.example.vic_sun.fsc.R;
import com.server.HttpConnectUtil;
import com.server.WebServerHelp;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Message_Hand extends Activity {

    EditText et_title, et_content;
    TextView tv_hand;
    ImageView iv_pic;
    Handler handler;
    ApplicationUser user;
    MyApplication mApplication;
    Long message_id;


    static final String TAG = "FSC";
    static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 0x120;
    private static int RESULT_LOAD_IMAGE = 1;
    String picturePath;
    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagehand);

        mApplication = (MyApplication) getApplication();
        user = mApplication.getUser();

        iv_pic = (ImageView)findViewById(R.id.activity_handmessage_addpicture);
        et_content = (EditText)findViewById(R.id.activity_handmessage_content);
        et_title = (EditText)findViewById(R.id.activity_handmessage_title);
        tv_hand = (TextView)findViewById(R.id.activity_handmessage_hand);

        MyClickListener mc = new MyClickListener();
        tv_hand.setOnClickListener(mc);
        iv_pic.setOnClickListener(mc);

        handler = new Handler() {
            private Intent intent;

            public void handleMessage(Message msg) {
                if (msg.what == 0x123) {
                    JSONObject returnJson = (JSONObject)msg.obj;
                    try{
                        message_id = returnJson.getLong("message_id");
                        dateHand(message_id);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else if (msg.what == 0x120) {
                    Intent jump = new Intent(Message_Hand.this, release_message_main.class);
                    startActivity(jump);
                }

            }
        };


    }

    class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View V) {
            if (V.getId() == R.id.activity_handmessage_hand) {
                JSONObject handJson = new JSONObject();
                try {
                    handJson.put("user_id", user.getUser_id());
                    handJson.put("user_flag", user.getUser_flag());
                    handJson.put("user_pwd", user.getUser_pwd());
                    handJson.put("action", "handMessage");
                    handJson.put("message_title", et_title.getText().toString());
                    handJson.put("message_content", et_content.getText().toString());

                } catch (Exception e) {
                    e.printStackTrace();

                }
                final String content = String.valueOf(handJson);

                new Thread() {
                    public void run() {
                        Message message = handler.obtainMessage();
                        try {
                            URL url = new URL(WebServerHelp.getURL() + "HandMessage");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(5000);
                            conn.setDoOutput(true);// 设置允许输出
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); // 内容类型
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
                            JSONObject rJson = new JSONObject(returnString);

                            message.obj = rJson;
                            message.what = 0x123;

                            if (rJson != null && rJson.getInt("check_flag") == 1) {

                                handler.sendMessage(message);

                            } else {
                                handler.sendEmptyMessage(0x122);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(0x122);
                        }
                    }
                }.start();
            }
            else if (V.getId() == R.id.activity_handmessage_addpicture){
                i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");

                int permissionCheck = ContextCompat.checkSelfPermission(Message_Hand.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Message_Hand.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
                } else {
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
                break;

            default:
                break;

        }
    }

    public void dateHand (Long id){

        final String url = WebServerHelp.getURL() + "ImageUpload";
        //final String url = WebServerHelp.getURL() + "ImageUpload";
        final Map<String, Object> paramMap = new HashMap<String, Object>(); //文本数据全部添加到Map里
        paramMap.put("type", "message_img");
        paramMap.put("user_id", id);

        String path = picturePath; //此处写上要上传的文件在系统中的路径
        final File pictureFile = new File(path); //通过路径获取文件

        new Thread() {
            public void run() {
                try {
                    HttpConnectUtil.doPostPicture(url, paramMap, pictureFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x120);
            }
        }.start();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            super.onActivityResult(requestCode, resultCode, data);

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            iv_pic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }
}
