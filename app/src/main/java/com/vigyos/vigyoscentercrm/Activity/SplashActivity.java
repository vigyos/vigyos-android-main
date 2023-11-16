package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

import com.vigyos.vigyoscentercrm.Constant.SharedPrefManager;
import com.vigyos.vigyoscentercrm.R;

@SuppressLint("CustomSplashScreen")
@BuildCompat.PrereleaseSdkCheck
public class SplashActivity extends AppCompatActivity {

    public static SharedPrefManager prefManager;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefManager = new SharedPrefManager(this);

//        videoView =  findViewById(R.id.videoView);
//        String path = "android.resource://com.vigyos.vigyoscentercrm/"+R.raw.splash;
//        Uri uri = Uri.parse(path);
//        videoView.setVideoURI(uri);
//        videoView.requestFocus();
//        videoView.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (prefManager.getLogin()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else if(prefManager.getRegister()) {
                    startActivity(new Intent(SplashActivity.this, RegisterHomeActivity.class));
                    finish();
                }  else {
                    startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
                    finish();
                }


//                if (prefManager.getOnboarding()) {
//                    if (prefManager.getLogin()){
//                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                        finish();
//                    } else if (prefManager.getRegister()) {
//                        startActivity(new Intent(SplashActivity.this, RegisterHomeActivity.class));
//                        finish();
//                    } else {
//                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                        finish();
//                    }
//                } else {
//
//                }
            }
        }, 3000);
    }
}