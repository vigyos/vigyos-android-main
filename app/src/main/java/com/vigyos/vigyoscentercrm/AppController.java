package com.vigyos.vigyoscentercrm;

import android.app.Application;
import android.view.View;

import com.vigyos.vigyoscentercrm.R;
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