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
public class VerifyAccountActivity extends AppCompatActivity {

    private RelativeLayout verifyNow;
    private TextView value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);
        initialization();
        declaration();
    }

    private void initialization() {
        value = findViewById(R.id.value);
        verifyNow = findViewById(R.id.verifyNow);
    }

    private void declaration() {
        Intent intent = getIntent();
        String checkActivity = intent.getStringExtra("key");
        if (checkActivity.equalsIgnoreCase("SignUpWithPhone")) {
//            value.setText("+91 "+ );
        }

        verifyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VerifyAccountActivity.this, AccountCreatedActivity.class));
            }
        });
    }
}