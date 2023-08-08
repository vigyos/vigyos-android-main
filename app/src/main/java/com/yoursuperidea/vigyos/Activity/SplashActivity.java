package com.yoursuperidea.vigyos.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.yoursuperidea.vigyos.Constant.SharedPrefManager;
import com.yoursuperidea.vigyos.R;

@SuppressLint("CustomSplashScreen")
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
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 3000);
    }
}