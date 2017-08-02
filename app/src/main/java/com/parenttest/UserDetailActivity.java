package com.parenttest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bean.ApplicationUser;
import com.example.vic_sun.fsc.MyApplication;
import com.example.vic_sun.fsc.R;

import com.server.GetImage;
import com.server.HttpConnectUtil;
import com.server.WebServerHelp;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UserDetailActivity extends Activity {
    private static int RESULT_LOAD_IMAGE = 1;
    String picturePath;
    ApplicationUser user;
    Handler handler;
    Intent i;
    TextView infor_UserName,infor_PhoneNum, infor_ChildrenName;
    ImageView iv_img;
    Bitmap img;

    static final String TAG = "FSC";
    static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 0x120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor);

        MyApplication mApplication = (MyApplication) getApplication();

        user = mApplication.getUser();

        MyClickListener mc = new MyClickListener();

        TextView tv_updateimg = (TextView) findViewById(R.id.textView_updataimg);
        tv_updateimg.setOnClickListener(mc);
        /***************************************************************************/
        infor_UserName = (TextView) findViewById(R.id.Activity_Detail_TextView_UserName);
        infor_PhoneNum = (TextView) findViewById(R.id.Activity_Detail_TextView_PhoneNum);
        infor_ChildrenName = (TextView) findViewById(R.id.Detail_student_name);
        iv_img = (ImageView)findViewById(R.id.imageView_Head);

        final JSONObject requestJson = new JSONObject();
        try {
            requestJson.put("user_id", user.user_id);
            requestJson.put("user_flag", user.user_flag);
            requestJson.put("user_pwd", user.user_pwd);
            requestJson.put("action", "getDetail");


        } catch (Exception e) {
            e.printStackTrace();

        }
        //
        final String content = String.valueOf(requestJson);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int flag=msg.what;
                if(flag==0x123)
                {
                    JSONObject parentJSON = (JSONObject)msg.obj;
                    try{
                        JSONObject studentJSON = (JSONObject)parentJSON.getJSONObject("student");
                        String img_url = parentJSON.getString("user_img");
                        infor_UserName.setText(parentJSON.getString("user_trueName"));
                        infor_PhoneNum.setText(parentJSON.getString("user_tel"));
                        infor_ChildrenName.setText(studentJSON.getString("student_name"));
                        if (img_url != null){
                            iv_img.setImageBitmap(img);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

            }
        };
        new Thread() {

            public void run() {
                Message message = handler.obtainMessage();

                try {
                    URL url = new URL(WebServerHelp.getURL() + "GetUserDetail");
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
                        handler.sendEmptyMessage(0x120);
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

                    img = GetImage.getInternetPicture(rJson.getString("user_img"));

                    message.obj = rJson;
                    message.what = 0x123;

                    if (rJson.getInt("login_flag") == 1) {

                        handler.sendMessage(message);

                    } else {
                        handler.sendEmptyMessage(0x120);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View V) {
            if (V.getId() == R.id.textView_updataimg) {

                i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");

                int permissionCheck = ContextCompat.checkSelfPermission(UserDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UserDetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
                } else {
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }


            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            super.onActivityResult(requestCode, resultCode, data);
            dateHand(requestCode, resultCode, data);



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

    public void dateHand (int requestCode, int resultCode, Intent data){
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        picturePath = cursor.getString(columnIndex);
        cursor.close();


        final String url = WebServerHelp.getURL() + "ImageUpload";
        //final String url = WebServerHelp.getURL() + "ImageUpload";
        final Map<String, Object> paramMap = new HashMap<String, Object>(); //文本数据全部添加到Map里
        paramMap.put("type", "parent_img");
        paramMap.put("user_id", user.getUser_id());

        String path = picturePath; //此处写上要上传的文件在系统中的路径
        final File pictureFile = new File(path); //通过路径获取文件

        new Thread() {
            public void run() {
                try {
                    HttpConnectUtil.doPostPicture(url, paramMap, pictureFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        ImageView imageView = (ImageView) findViewById(R.id.imageView_Head);
        imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    }

}
