package com.example.easydesign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easydesign.adapter.Adapter_hlv;
import com.example.easydesign.tools.DragScaleView;
import com.example.easydesign.tools.HorizontalListView;
import com.example.easydesign.tools.localinfoutil;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;

import net.simonvt.menudrawer.MenuDrawer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    private Context context;
    private Button bt_bh,bt_dh,bt_pdh,bt_aqj,bt_qdh,bntTakePic;
    private TextView tv_username;
    private View view_option;
    private DragScaleView drag_scale_view;
    private SurfaceView surfaceView;
    private RelativeLayout fraShadeBottom, buttonLayout,rl_login;
    private LinearLayout ll_uploaddesign,ll_mydesign,ll_guanyu,ll_suggest,ll_tuichu;
    private Camera camera;
    private Camera.Parameters parameters = null;
    private WindowManager mWindowManager;
    private int windowWidth;//获取手机屏幕宽度
    private int windowHight;//获取手机屏幕高度
    private String savePath = "/finger/";
    private Bundle bundle = null;// 声明一个Bundle对象，用来存储数据
    private int IS_TOOK = 0;//是否已经拍照 ,0为否
    private MenuDrawer menudraw;

    public String type="bh",url="http://42.159.246.0/artlu/app/easydesign.php?type=koutu/";
    public String[] imagenames;
    public HorizontalListView hlv;
    public BitmapUtils bitmap;
    public HttpUtils httputil;
    public int havetakedpic=0;
    public SharedPreferences sharedPreferences;
    private String username,password;
    public int ISLOGIN=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        bitmap = new BitmapUtils(context);
        httputil = new HttpUtils();

        /////////////////////////////////////////
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowWidth = mWindowManager.getDefaultDisplay().getWidth();

        menudraw = MenuDrawer.attach(this);
        menudraw.setContentView(R.layout.activity_main);
        menudraw.setMenuView(R.layout.menudraw);
        menudraw.setMenuSize(windowWidth * 9 / 10);

        init();
        //////////////////////////////////////////
        sharedPreferences=getSharedPreferences("userinfo", MODE_WORLD_READABLE);
        username=sharedPreferences.getString("username", "DONTHAVE");
        password=sharedPreferences.getString("password", "DONTHAVE");
        if(password.equals("DONTREMEMBER")){
            tv_username.setText(username);
            ISLOGIN=1;
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("password","DONTHAVE");
            editor.commit();
        }
        else if(password.equals("DONTHAVE")){
            ;
        }
        else{
            tv_username.setText(username);
            ISLOGIN=1;
        }


        hlvrefresh();
        hlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                setbitmap(imagenames[arg2]);
            }
        });
    }

    //按钮点击事件
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.bnt_takepicture:
                fraShadeBottom.setVisibility(View.INVISIBLE);
                camera.takePicture(mShutter, null, mJpeg);// 拍照
                havetakedpic=1;
                break;
            case R.id.bt_bh:
                type ="bh";
                initcolor();
                bt_bh.setTextColor(getResources().getColor(R.color.orange));
                bt_bh.setBackground(getResources().getDrawable(R.drawable.orangebar));
                hlvrefresh();
                break;
            case R.id.bt_dh:
                type ="dh";
                initcolor();
                bt_dh.setTextColor(getResources().getColor(R.color.orange));
                bt_dh.setBackground(getResources().getDrawable(R.drawable.orangebar));
                hlvrefresh();
                break;
            case R.id.bt_pdh:
                type ="pdh";
                initcolor();
                bt_pdh.setTextColor(getResources().getColor(R.color.orange));
                bt_pdh.setBackground(getResources().getDrawable(R.drawable.orangebar));
                hlvrefresh();
                break;
            case R.id.bt_aqj:
                type ="aqj";
                initcolor();
                bt_aqj.setTextColor(getResources().getColor(R.color.orange));
                bt_aqj.setBackground(getResources().getDrawable(R.drawable.orangebar));
                hlvrefresh();
                break;
            case R.id.bt_qdh:
                type ="qdh";
                initcolor();
                bt_qdh.setTextColor(getResources().getColor(R.color.orange));
                bt_qdh.setBackground(getResources().getDrawable(R.drawable.orangebar));
                hlvrefresh();
                break;
            case R.id.view_option:
                menudraw.openMenu();
                break;
            case R.id.rl_login:
                if(ISLOGIN==1) {
                    Intent intent = new Intent(context, ExitAccountActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.ll_uploaddesign:
                String loginusername=tv_username.getText().toString();
                if(loginusername.equals("点击登录/注册")) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(context, AddImageActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_mydesign:
                break;
        }
    }
    //////////////////////////////////////////////////////////////////////
   /* public void hlvrefresh(){
        httputil.send(HttpMethod.GET, url + type, new RequestCallBack<Object>() {
            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {

                String result = (String) responseInfo.result;
                imagenames = result.split("##", 30);

               try {
                    Adapter_hlv adapter = new Adapter_hlv(context,type, imagenames, bitmap);
                    hlv.setAdapter(adapter);
                } catch (Exception e) {
                    Toast.makeText(context, "网络不给力", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(context, "网络连接出错", Toast.LENGTH_SHORT).show();
            }
        });

    }*/
    public void hlvrefresh(){
        final String localinfo = localinfoutil.getinfo(type);
        if(!localinfo.equals("dontexit")){
            imagenames = localinfo.split("##", 30);
            Adapter_hlv adapter = new Adapter_hlv(context,type, imagenames, bitmap);
            hlv.setAdapter(adapter);
        }
        new Thread(){
            @Override
            public void run() {
                try {
                    URL myurl=new URL(url+type);
                    HttpURLConnection conn=(HttpURLConnection) myurl.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");

                    final String info =istostr(conn.getInputStream());
                    if(localinfo.equals("dontexit")){
                        localinfoutil.saveinfo(type, info);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imagenames = info.split("##", 30);
                                Adapter_hlv adapter = new Adapter_hlv(context,type, imagenames, bitmap);
                                hlv.setAdapter(adapter);
                            }
                        });
                    }
                    else if(!info.equals(localinfo)) {
                        localinfoutil.saveinfo(type, info);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imagenames = info.split("##", 30);
                                Adapter_hlv adapter = new Adapter_hlv(context,type, imagenames, bitmap);
                                hlv.setAdapter(adapter);
                            }
                        });
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    ///////////////////////////////////////////////////////////////////////////init()
    @SuppressWarnings("deprecation")
    private void init() {
       /*
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowWidth = mWindowManager.getDefaultDisplay().getWidth();
        windowHight = mWindowManager.getDefaultDisplay().getHeight();
        fraShadeBottom = (RelativeLayout) findViewById(R.id.fra_shade_bottom);


        RelativeLayout.LayoutParams bottomParams = (RelativeLayout.LayoutParams) fraShadeBottom.getLayoutParams();
        bottomParams.width = windowWidth;
        bottomParams.height = (windowHight - windowWidth) / 2;
        fraShadeBottom.setLayoutParams(bottomParams);
        fraShadeBottom.getBackground().setAlpha(200);
        */

        //按钮
        bntTakePic = (Button) findViewById(R.id.bnt_takepicture);
        fraShadeBottom = (RelativeLayout) findViewById(R.id.fra_shade_bottom);
        rl_login = (RelativeLayout) findViewById(R.id.rl_login);
        ll_uploaddesign = (LinearLayout) findViewById(R.id.ll_uploaddesign);
        ll_mydesign = (LinearLayout) findViewById(R.id.ll_mydesign);
        ll_guanyu = (LinearLayout) findViewById(R.id.ll_guanyu);
        ll_suggest = (LinearLayout) findViewById(R.id.ll_suggest);
        ll_tuichu = (LinearLayout) findViewById(R.id.ll_tuichu);

        bt_bh = (Button) findViewById(R.id.bt_bh);
        bt_dh = (Button) findViewById(R.id.bt_dh);
        bt_pdh = (Button) findViewById(R.id.bt_pdh);
        bt_aqj = (Button) findViewById(R.id.bt_aqj);
        bt_qdh = (Button) findViewById(R.id.bt_qdh);
        tv_username = (TextView) findViewById(R.id.tv_username);

        hlv = (HorizontalListView) findViewById(R.id.hlv);
        drag_scale_view = (DragScaleView) findViewById(R.id.drag_scale_view);
        buttonLayout = (RelativeLayout) findViewById(R.id.buttonLayout);
        view_option = (View) findViewById(R.id.view_option);

        bntTakePic.setOnClickListener(this);
        bt_bh.setOnClickListener(this);
        bt_dh.setOnClickListener(this);
        bt_pdh.setOnClickListener(this);
        bt_aqj.setOnClickListener(this);
        bt_qdh.setOnClickListener(this);
        view_option.setOnClickListener(this);
        rl_login.setOnClickListener(this);
        ll_uploaddesign.setOnClickListener(this);
        ll_mydesign.setOnClickListener(this);
        ll_guanyu.setOnClickListener(this);
        ll_suggest.setOnClickListener(this);
        ll_tuichu.setOnClickListener(this);

        //照相机预览的空间
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setFixedSize(150, 150); // 设置Surface分辨率
        surfaceView.getHolder().setKeepScreenOn(true);// 屏幕常亮
        surfaceView.getHolder().addCallback(new SurfaceCallback());// 为SurfaceView的句柄添加一个回调函数
    }

    //initcolor
    public void initcolor(){
        bt_bh.setTextColor(getResources().getColor(R.color.whited));
        bt_dh.setTextColor(getResources().getColor(R.color.whited));
        bt_pdh.setTextColor(getResources().getColor(R.color.whited));
        bt_aqj.setTextColor(getResources().getColor(R.color.whited));
        bt_qdh.setTextColor(getResources().getColor(R.color.whited));
        bt_bh.setBackground(null);
        bt_dh.setBackground(null);
        bt_pdh.setBackground(null);
        bt_aqj.setBackground(null);
        bt_qdh.setBackground(null);
    }
    //重构相机照相回调类/@author pc/
    private final class SurfaceCallback implements SurfaceHolder.Callback {

        @SuppressWarnings("deprecation")
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            // TODO Auto-generated method stub
            parameters = camera.getParameters(); // 获取各项参数
            parameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
            Camera.Size bestSize=null;

            List<Camera.Size> sizeList = camera.getParameters().getSupportedPreviewSizes();
            bestSize = sizeList.get(0);

            for (int i = 1; i < sizeList.size(); i++) {
                if ((sizeList.get(i).width * sizeList.get(i).height) >
                        (bestSize.width * bestSize.height)) {
                    bestSize = sizeList.get(i);
                }
            }

            parameters.setPreviewSize(bestSize.width, bestSize.height); // 设置预览大小
            parameters.setJpegQuality(80); // 设置照片质量
            camera.setParameters(parameters);
            camera.startPreview(); // 开始预览
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            try {
                camera = Camera.open(); // 打开摄像头
                camera.setPreviewDisplay(holder); // 设置用于显示拍照影像的SurfaceHolder对象
                camera.setDisplayOrientation(getPreviewDegree(MainActivity.this));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            if (camera != null) {
                camera.stopPreview();
                camera.release(); // 释放照相机
                camera = null;
            }
        }

    }
    // 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
    public static int getPreviewDegree(Activity activity) {
        // 获得手机的方向
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degree = 0;
        // 根据手机的方向计算相机预览画面应该选择的角度
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }

    ///////////
    /* 图像数据处理还未完成时的回调函数 */
    private Camera.ShutterCallback mShutter = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            // 一般显示进度条
        }
    };

    /* 图像数据处理完成后的回调函数 */
    private Camera.PictureCallback mJpeg = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // 保存图片
           /* mFileName = UUID.randomUUID().toString() + ".jpg";
            Log.i("filename", mFileName);
            FileOutputStream out = null;
            try {
                out = openFileOutput(mFileName, Context.MODE_PRIVATE);
                byte[] newData = null;
                out.write(data);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null)
                        out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            */
        }
    };

    /**
     * 获取和保存当前屏幕的截图
     */
    private void GetandSaveCurrentImage(Activity activity) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();

        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        Display display = activity.getWindowManager().getDefaultDisplay();

        // 获取屏幕宽和高
        int widths = display.getWidth();
        int heights = display.getHeight();

        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);

        // 去掉状态栏
        Bitmap Bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
                statusBarHeights, widths, heights - statusBarHeights);

        // 销毁缓存信息
        view.destroyDrawingCache();

        String currenttime = System.currentTimeMillis() + "";
        String SavePath = getSDCardPath() + "/ScreenImage/";
        // 3.保存Bitmap
        try {
            File path = new File(SavePath);
            if (!path.exists())
                path.mkdirs();
            File file = new File(SavePath + currenttime + "Screen.png");
            Toast.makeText(MainActivity.this, SavePath + currenttime + "Screen.png", Toast.LENGTH_SHORT).show();
            FileOutputStream fos = new FileOutputStream(file);
            Bmp.compress(Bitmap.CompressFormat.PNG, 60, fos);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "ooo", Toast.LENGTH_SHORT).show();
        }
    }

////////////////////////////////////////////

    /**
     * 获取SDCard的目录路径功能
     */
    private String getSDCardPath() {
        File sdcardDir = null;
        // 判断SDCard是否存在
        boolean sdcardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdcardExist) {
            sdcardDir = Environment.getExternalStorageDirectory();
        }
        return sdcardDir.toString();
    }

    //////////////////////////////
    public void setbitmap(final String imgname) {
            new Thread(){
                @Override
                public void run() {
                    try{
                        URL url = new URL("http://42.159.246.0/artlu/app/koutu/" + type +"/"+ imgname);
                        HttpURLConnection conn = (HttpURLConnection) url
                                .openConnection();
                        conn.setDoInput(true);
                        InputStream is = conn.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(is);
                        is.close();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                drag_scale_view.setBitmap(bitmap);
                            }
                        });
                    }
                    catch (Exception e){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "图片已去火星", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }.start();
    }



    //////////
    public static String istostr(InputStream in)
    {

        String str = "";
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuffer sb = new StringBuffer();

            while ((str = reader.readLine()) != null)
            {
                sb.append(str).append("\n");
            }
            return sb.toString();
        }
        catch (UnsupportedEncodingException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return str;
    }

    ///////////////
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(havetakedpic==1){
                camera.startPreview();
                havetakedpic=0;
                fraShadeBottom.setVisibility(View.VISIBLE);
                return true;
            }
            else
                return super.onKeyDown(keyCode, event);
        }
        else
        return super.onKeyDown(keyCode, event);
    }
}
