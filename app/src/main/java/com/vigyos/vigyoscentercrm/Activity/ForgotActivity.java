package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vigyos.vigyoscentercrm.R;

@BuildCompat.PrereleaseSdkCheck
public class ForgotActivity extends AppCompatActivity {

    private RelativeLayout sendCodeVerification;
    private RelativeLayout emailAddressLyt;
    private EditText emailAddress;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        initialization();
        declaration();
    }

    private void initialization() {
        sendCodeVerification = findViewById(R.id.sendCodeVerification);
        emailAddressLyt = findViewById(R.id.emailAddressLyt);
        emailAddress = findViewById(R.id.emailAddress);
    }

    private void declaration() {
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_animation);
        sendCodeVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(ForgotActivity.this, R.anim.viewpush));
                if (emailAddress.getText().toString().isEmpty()) {
                    emailAddress.requestFocus();
                    emailAddressLyt.startAnimation(animation);
                    Toast.makeText(ForgotActivity.this, "Enter your Email ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(ForgotActivity.this, VerifyAccountActivity.class);
                intent.putExtra("key", "ForgotActivity");
                intent.putExtra("value", emailAddress.getText().toString());
                startActivity(intent);
            }
        });
    }
}