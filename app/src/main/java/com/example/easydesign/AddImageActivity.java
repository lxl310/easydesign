package com.example.easydesign;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlertDialog.Builder;
import android.widget.Toast;

import com.example.easydesign.tools.CircularProgress;
import com.example.easydesign.tools.SmallImage;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AddImageActivity extends Activity {
    private ImageView img_add;
    private TextView tv_shareplan,tv_tip;
    private CheckBox cb_agree;
    private Button bt_cancel,bt_add;
    private CircularProgress progress_add;
    Context context;
    AlertDialog mydialog;
    String mPhotoPath;
    File mPhotoFile;
    boolean haveimage=false;
    File smallimagefile;
    private String username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        init();

        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_WORLD_READABLE);
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","DONTHAVE");

        ////////
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotodialog();
            }
        });

        //////
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ////
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goaddimage();
            }
        });
    }

    private void init() {
        context=this;
        img_add = (ImageView) findViewById(R.id.img_add);
        tv_shareplan = (TextView) findViewById(R.id.tv_shareplan);
        cb_agree = (CheckBox) findViewById(R.id.cb_agree);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        bt_add = (Button) findViewById(R.id.bt_add);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        progress_add = (CircularProgress) findViewById(R.id.progress_add);
    }

    //////////////////////////////////////////addimage
    public void goaddimage(){
        String share = "1";
        if(!cb_agree.isChecked()) {
            share = "0";
        }
        if (haveimage == true) {
            // ------------------------------
            progress_add.setVisibility(View.VISIBLE);

            RequestParams params = new RequestParams();
            params.addBodyParameter("img", smallimagefile);
            params.addBodyParameter("imagename", new Date().getTime() + "");
            params.addBodyParameter("username", username);
            params.addBodyParameter("share", share);
            HttpUtils http = new HttpUtils();
            http.send(HttpMethod.POST, "http://42.159.246.0/artlu/app/addimage.php",
                    params, new RequestCallBack<String>() {

                        @Override
                        public void onStart() {
                            tv_tip.setText("连接中...");
                        }

                        @Override
                        public void onLoading(long total, long current,
                                              boolean isUploading) {
                            if (isUploading) {
                                tv_tip.setText("已完成: " + (int) (current * 100 / total) + "/%");
                            }
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            Toast.makeText(context,"已添加至我的图库",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            tv_tip.setText(error.getExceptionCode() + ":" + msg);
                        }
                    });
            // --------------------------------
        } else {
            tv_tip.setText("请先拍照或选择图片");
            hidetext();
        }
    }
    // /////////////////////////////////////////////////////////////自定义的Dialog
    public void showPhotodialog() {
        Builder builder = new Builder(AddImageActivity.this);
        mydialog = builder.create();
        View view = LayoutInflater.from(AddImageActivity.this).inflate(
                R.layout.dialog_picimage, null);
        mydialog.setCanceledOnTouchOutside(true);
        mydialog.show();
        mydialog.setContentView(view);


        // dialog内部的点击事件
        TextView tv1 = (TextView) view.findViewById(R.id.tv_paizhao);
        TextView tv2 = (TextView) view.findViewById(R.id.tv_xiangce);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });

    }

    //
    public void takePicture() {
        try {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            mPhotoPath = "/storage/sdcard0/DCIM/Camera/" + getPhotoFileName();
            mPhotoFile = new File(mPhotoPath);
           /* if (!mPhotoFile.exists()) {
                mPhotoFile.createNewFile();
            }*/
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
            startActivityForResult(intent, 1);
        } catch (Exception e) {
        }
    }

    // onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            SmallImage smallimage = new SmallImage(mPhotoPath);
            Bitmap bmp = smallimage.bitmap;
            smallimagefile = new File(smallimage.smallimagepath);
            img_add.setImageBitmap(bmp);
            haveimage = true;
            mydialog.dismiss();
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String path = getPathFromUri(uri);
            SmallImage smallimage = new SmallImage(path);
            Bitmap bm1 = smallimage.bitmap;
            smallimagefile = new File(smallimage.smallimagepath);
            img_add.setImageBitmap(bm1);
            haveimage = true;
            mydialog.dismiss();
        }

    }

    // getPathFromUri
    public String getPathFromUri(Uri uri) {
        ContentResolver cr = this.getContentResolver();
        Cursor cursor = cr.query(uri, null, null, null, null);
        while (cursor.moveToNext()) {
            return cursor.getString(cursor.getColumnIndex("_data"));
        }
        return "";
    }

    // getPhotoFileName
    public String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    // hidetext
    public void hidetext() {
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        tv_tip.setText("");
                        timer.cancel();
                    }
                });
            }
        };
        timer.schedule(task, 2000);
    }
}
