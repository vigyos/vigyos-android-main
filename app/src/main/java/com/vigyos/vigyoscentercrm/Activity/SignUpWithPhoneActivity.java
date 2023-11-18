package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vigyos.vigyoscentercrm.R;

@BuildCompat.PrereleaseSdkCheck
public class SignUpWithPhoneActivity extends AppCompatActivity {

    private LinearLayout numberLyt;
    private Animation animation;
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
        numberLyt = findViewById(R.id.numberLyt);
        phoneNumber = findViewById(R.id.phoneNumber);
        sendCodeButton = findViewById(R.id.sendCodeButton);
        signUpWithEmail = findViewById(R.id.signUpWithEmail);
        animation = AnimationUtils.loadAnimation(SignUpWithPhoneActivity.this, R.anim.shake_animation);
    }

    private void declaration() {
        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(SignUpWithPhoneActivity.this, R.anim.viewpush));
                if (phoneNumber.getText().toString().isEmpty()) {
                    numberLyt.startAnimation(animation);
                    phoneNumber.requestFocus();
                    Toast.makeText(SignUpWithPhoneActivity.this, "Enter your Number", Toast.LENGTH_SHORT).show();
                    return;
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
                v.startAnimation(AnimationUtils.loadAnimation(SignUpWithPhoneActivity.this,R.anim.viewpush));
                startActivity(new Intent(SignUpWithPhoneActivity.this, SignUpActivity.class));
            }
        });
    }
}