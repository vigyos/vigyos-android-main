package com.yoursuperidea.vigyos.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.yoursuperidea.vigyos.Activity.SplashActivity;
import com.yoursuperidea.vigyos.R;

public class AccountFragment extends Fragment {

    private TextView userName;
    private TextView userPhone;
    private TextView userEmail;
    private TextView userAddress;


    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_account, container, false);
        userName = view.findViewById(R.id.userName);
        userPhone = view.findViewById(R.id.userPhone);
        userEmail = view.findViewById(R.id.emailTextView);
        userAddress = view.findViewById(R.id.userAddress);



        userName.setText(SplashActivity.prefManager.getFirstName() + " " + SplashActivity.prefManager.getLastName());
        userPhone.setText(SplashActivity.prefManager.getPhone());
        userEmail.setText(SplashActivity.prefManager.getEmail());

        return view;
    }
}