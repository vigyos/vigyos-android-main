package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.vigyos.vigyoscentercrm.Constant.SharedPrefManager;
import com.vigyos.vigyoscentercrm.R;

@SuppressLint("CustomSplashScreen")
@BuildCompat.PrereleaseSdkCheck
public class SplashActivity extends AppCompatActivity {

    public static SharedPrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefManager = new SharedPrefManager(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (prefManager.getLogin()){
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 3000);
    }
}