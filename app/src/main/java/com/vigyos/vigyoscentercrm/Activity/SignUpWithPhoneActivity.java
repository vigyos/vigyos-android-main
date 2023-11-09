package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.vigyos.vigyoscentercrm.R;

@BuildCompat.PrereleaseSdkCheck
public class SignUpWithPhoneActivity extends AppCompatActivity {
    
    private RelativeLayout sendCodeButton;
    private RelativeLayout signUpWithEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_with_phone);
        initialization();
        declaration();
    }

    private void initialization() {
        sendCodeButton = findViewById(R.id.sendCodeButton);
        signUpWithEmail = findViewById(R.id.signUpWithEmail);
    }

    private void declaration() {
        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpWithPhoneActivity.this, VerifyAccountActivity.class));
            }
        });
        signUpWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpWithPhoneActivity.this, CreateAccountActivity.class));
            }
        });
    }
}