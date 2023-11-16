package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vigyos.vigyoscentercrm.R;

@BuildCompat.PrereleaseSdkCheck
public class SignUpWithPhoneActivity extends AppCompatActivity {
    
    private RelativeLayout sendCodeButton;
    private RelativeLayout signUpWithEmail;
    private EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_with_phone);
        initialization();
        declaration();
    }

    private void initialization() {
        phoneNumber = findViewById(R.id.phoneNumber);
        sendCodeButton = findViewById(R.id.sendCodeButton);
        signUpWithEmail = findViewById(R.id.signUpWithEmail);
    }

    private void declaration() {
        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(SignUpWithPhoneActivity.this, R.anim.viewpush));
                if (phoneNumber.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpWithPhoneActivity.this, "Enter your Number", Toast.LENGTH_SHORT).show();
                }


                Intent intent = new Intent(SignUpWithPhoneActivity.this, VerifyAccountActivity.class);
                intent.putExtra("key", "SignUpWithPhone");
                intent.putExtra("value", phoneNumber.getText().toString());
                startActivity(intent);
            }
        });
        signUpWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpWithPhoneActivity.this, SignUpActivity.class));
            }
        });
    }
}