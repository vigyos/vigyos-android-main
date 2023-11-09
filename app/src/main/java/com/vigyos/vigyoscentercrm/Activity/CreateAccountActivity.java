package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vigyos.vigyoscentercrm.R;

@BuildCompat.PrereleaseSdkCheck
public class CreateAccountActivity extends AppCompatActivity {

    private RelativeLayout signUpButton;
    private RelativeLayout signUpWithNumber;
    private TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initialization();
        declaration();
    }

    private void initialization() {
        signUpButton = findViewById(R.id.signUpButton);
        signUpWithNumber = findViewById(R.id.signUpWithNumber);
        signUp = findViewById(R.id.signUp);
    }

    private void declaration() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateAccountActivity.this, AccountCreatedActivity.class));
            }
        });
        signUpWithNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateAccountActivity.this, SignUpWithPhoneActivity.class));
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
            }
        });
    }
}