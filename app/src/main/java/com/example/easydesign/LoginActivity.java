package com.example.easydesign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class LoginActivity extends Activity{
    Context context;
    private EditText et_username,et_password;
    private CheckBox cb;
    private Button bt_login,bt_register;
    private String username,password;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context=this;

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_register = (Button) findViewById(R.id.bt_register);
        cb = (CheckBox) findViewById(R.id.cb);

        sharedPreferences=getSharedPreferences("userinfo",MODE_WORLD_READABLE);

        /////////////login
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_username.getText().toString().trim();
                password = et_password.getText().toString().trim();
                if (username.equals("") || password.equals(""))
                    Toast.makeText(getApplicationContext(), "信息不全！", Toast.LENGTH_SHORT).show();
                else
                    gologin();
            }

        });

        ////////register
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    //////////////////////////////
    private void gologin(){
        findViewById(R.id.myprogressbar).setVisibility(View.VISIBLE);

        HttpUtils httputils=new HttpUtils();
        httputils.send(HttpMethod.GET, "http://42.159.246.0/artlu/app/loginresult.php?username="+username+"&password="+password, new RequestCallBack<Object>() {
            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {
                String result = (String) responseInfo.result;


                if (result.equals("succeed")) {
                    SharedPreferences.Editor editor=sharedPreferences.edit();

                    if (cb.isChecked()) {
                        editor.putString("username",username);
                        editor.putString("password",password);
                        editor.commit();
                    } else {
                        editor.putString("username",username);
                        editor.putString("password","DONTREMEMBER");
                        editor.commit();
                    }
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (result.equals("failed_wrongusername")) {
                    Toast.makeText(context, "用户名不存在", Toast.LENGTH_SHORT)
                            .show();
                    findViewById(R.id.myprogressbar).setVisibility(
                            View.INVISIBLE);
                } else if (result.equals("failed_wrongpassword")) {
                    Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT)
                            .show();
                    findViewById(R.id.myprogressbar).setVisibility(
                            View.INVISIBLE);
                } else {
                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                    findViewById(R.id.myprogressbar).setVisibility(
                            View.INVISIBLE);
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