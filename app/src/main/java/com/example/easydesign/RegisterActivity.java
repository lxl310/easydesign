package com.example.easydesign;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class RegisterActivity extends Activity {
    private EditText register_username;
    private EditText register_password;
    private EditText reregister_password;
    private HttpUtils httputils;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context=this;

        register_username=(EditText) findViewById(R.id.register_username);
        register_password=(EditText) findViewById(R.id.register_password);
        reregister_password=(EditText) findViewById(R.id.reregister_password);
    }

    ///
    public void click_register(View v){
        String username=register_username.getText().toString();
        String password=register_password.getText().toString();
        String repassword=reregister_password.getText().toString();

        if (username.equals("")||password.equals("")||!password.equals(repassword))
            Toast.makeText(getApplicationContext(), "注册信息有误！", Toast.LENGTH_SHORT).show();
        else
        {
            httputils=new HttpUtils();
            findViewById(R.id.myprogressbar).setVisibility(View.VISIBLE);

            httputils.send(HttpMethod.GET, "http://42.159.246.0/artlu/app/register.php?username="+username+"&password="+password, new RequestCallBack<Object>() {
                @Override
                public void onSuccess(ResponseInfo<Object> responseInfo) {
                    String result = (String) responseInfo.result;

                    if(result.equals("alreadyregistered")){
                        Toast.makeText(getApplicationContext(), "用户已存在！", Toast.LENGTH_SHORT).show();
                        register_username.setText("");
                        findViewById(R.id.myprogressbar).setVisibility(View.INVISIBLE);
                    }
                    else if(result.equals("succeed")){
                        Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "网络连接错误！", Toast.LENGTH_SHORT).show();
                        findViewById(R.id.myprogressbar).setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(context, "网络连接出错", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.myprogressbar).setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}

