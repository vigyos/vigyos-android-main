package com.example.vigyoscentercrm;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.onesignal.OneSignal;

public class AppController extends Application {

//    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();

//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // OneSignal Initialization
        OneSignal.initWithContext(this, getString(R.string.ONESIGNAL_APP_ID));
    }
}
