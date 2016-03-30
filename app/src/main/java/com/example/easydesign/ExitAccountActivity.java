package com.example.easydesign;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class ExitAccountActivity extends Activity {

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_account);

        sharedPreferences=getSharedPreferences("userinfo",MODE_WORLD_READABLE);

    }
    public void bt_exitaccount(View v){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("password","DONTHAVE");
        editor.commit();

        Intent intent = new Intent(ExitAccountActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
