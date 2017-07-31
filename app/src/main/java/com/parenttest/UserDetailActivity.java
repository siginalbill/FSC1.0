package com.parenttest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import com.server.HttpConnectUtil;
import com.server.WebServerHelp;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UserDetailActivity extends Activity {
    private static int RESULT_LOAD_IMAGE = 1;
    String picturePath;
    ApplicationUser user;
    Intent i;

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
