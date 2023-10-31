package com.vigyos.vigyoscentercrm;

import android.app.Application;

import com.onesignal.OneSignal;

public class AppController extends Application {

//    private FirebaseAnalytics mFirebaseAnalytics;
    public static boolean backCheck = true;

    @Override
    public void onCreate() {
        super.onCreate();
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

         //OneSignal Initialization
        OneSignal.initWithContext(this, getString(R.string.ONESIGNAL_APP_ID));
    }
}