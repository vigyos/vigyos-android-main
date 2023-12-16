package com.vigyos.vigyoscentercrm.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.vigyos.vigyoscentercrm.Fragment.Onboarding.Onboarding1Fragment;
import com.vigyos.vigyoscentercrm.Fragment.Onboarding.Onboarding3Fragment;
import com.vigyos.vigyoscentercrm.R;

@BuildCompat.PrereleaseSdkCheck
public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        if (SplashActivity.prefManager.getOnboarding()) {
            loadFragment(new Onboarding3Fragment());
        } else {
            loadFragment(new Onboarding1Fragment());
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (!fragmentManager.isStateSaved()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_container1, fragment);
            transaction.commit();
        }
    }
}