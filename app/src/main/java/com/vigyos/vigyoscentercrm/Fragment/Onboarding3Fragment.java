package com.vigyos.vigyoscentercrm.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.vigyos.vigyoscentercrm.Activity.SignUpActivity;
import com.vigyos.vigyoscentercrm.Activity.LoginActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.R;

@BuildCompat.PrereleaseSdkCheck
public class Onboarding3Fragment extends Fragment {

    public Onboarding3Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_onboarding3, container, false);
        RelativeLayout createAccount = view.findViewById(R.id.createButton);
        RelativeLayout loginButton = view.findViewById(R.id.loginButton);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewpush));
                startActivity(new Intent(getActivity(), SignUpActivity.class));
                SplashActivity.prefManager.setOnboarding(true);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewpush));
                startActivity(new Intent(getActivity(), LoginActivity.class));
                SplashActivity.prefManager.setOnboarding(true);
            }
        });
        return view;
    }
}