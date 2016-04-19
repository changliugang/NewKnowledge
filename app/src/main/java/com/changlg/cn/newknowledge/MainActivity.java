package com.changlg.cn.newknowledge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.changlg.cn.newknowledge.crashloghandler.CrashHandler;
import com.changlg.cn.newknowledge.gson.GsonActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);

        startActivity(new Intent(MainActivity.this, GsonActivity.class));


    }
}
