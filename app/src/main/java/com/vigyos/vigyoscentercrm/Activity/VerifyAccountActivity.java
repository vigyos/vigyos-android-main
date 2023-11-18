package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.vigyos.vigyoscentercrm.R;

@BuildCompat.PrereleaseSdkCheck
public class VerifyAccountActivity extends AppCompatActivity {

    private RelativeLayout verifyNow;
    private TextView valueText;
    private PinView firstPinView;
    private TextView countDown, resend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);
        initialization();
        declaration();
    }

    private void initialization() {
        valueText = findViewById(R.id.value);
        firstPinView = findViewById(R.id.firstPinView);
        countDown = findViewById(R.id.countDown);
        resend = findViewById(R.id.resend);
        verifyNow = findViewById(R.id.verifyNow);
    }

    private void declaration() {
        Intent intent = getIntent();
        String checkActivity = intent.getStringExtra("key");
        String value = intent.getStringExtra("value");
        if (checkActivity.equalsIgnoreCase("SignUpWithPhone")) {
            valueText.setText("+91 "+value);
        } else {
            valueText.setText(value);
        }
        countDown();
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(VerifyAccountActivity.this, R.anim.viewpush));
                countDown();
                resend.setVisibility(View.GONE);
                countDown.setVisibility(View.VISIBLE);
            }
        });
        verifyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(VerifyAccountActivity.this, R.anim.viewpush));
                if (firstPinView.getText().toString().isEmpty()) {
                    firstPinView.startAnimation(AnimationUtils.loadAnimation(VerifyAccountActivity.this, R.anim.shake_animation));
                    Toast.makeText(VerifyAccountActivity.this, "Enter Code" , Toast.LENGTH_SHORT).show();
                    return;
                }
                if (firstPinView.getText().toString().length() != 4) {
                    firstPinView.startAnimation(AnimationUtils.loadAnimation(VerifyAccountActivity.this, R.anim.shake_animation));
                    Toast.makeText(VerifyAccountActivity.this, "Enter vailid Code", Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivity(new Intent(VerifyAccountActivity.this, AccountCreatedActivity.class));
            }
        });
    }

    private void countDown() {
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                countDown.setText(""+ millisUntilFinished / 1000);
                // logic to set the EditText could go here
            }
            public void onFinish() {
                resend.setText(getString(R.string.resend_code));
                countDown.setText("00");
                resend.setVisibility(View.VISIBLE);
                countDown.setVisibility(View.GONE);
            }
        }.start();
    }
}