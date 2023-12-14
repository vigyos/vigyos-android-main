package com.vigyos.vigyoscentercrm;

import android.app.Application;

import com.onesignal.OneSignal;

public class AppController extends Application {

    public static boolean backCheck = true;

    @Override
    public void onCreate() {
        super.onCreate();
         //OneSignal Initialization
        OneSignal.initWithContext(this, getString(R.string.ONESIGNAL_APP_ID));
    }
}