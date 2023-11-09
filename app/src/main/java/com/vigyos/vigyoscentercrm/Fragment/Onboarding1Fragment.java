package com.vigyos.vigyoscentercrm.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.vigyos.vigyoscentercrm.R;

@BuildCompat.PrereleaseSdkCheck
public class Onboarding1Fragment extends Fragment {

    public Onboarding1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_onboarding1, container, false);
        RelativeLayout nextButton = view.findViewById(R.id.nextButton);
        TextView skipThis = view.findViewById(R.id.skipThis);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewpush));
                loadFragment(new Onboarding2Fragment());
            }
        });
        skipThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewpush));
                loadFragment(new Onboarding3Fragment());
            }
        });
        return view;
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        if (!fragmentManager.isStateSaved()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_container1, fragment);
            transaction.commit();
        }
    }
}