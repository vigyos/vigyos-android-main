package com.example.vigyoscentercrm.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.vigyoscentercrm.R;

public class PancardCreateFragment extends Fragment {

    private View view;
    private Activity activity;

    public PancardCreateFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pan_card_create, container, false);



        return view;
    }
}