package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.vigyos.vigyoscentercrm.R;

public class VerifyAccountActivity extends AppCompatActivity {

    private RelativeLayout verifyNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);
        initialization();
        declaration();
    }

    private void initialization() {
        verifyNow = findViewById(R.id.verifyNow);

    }

    private void declaration() {
        verifyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VerifyAccountActivity.this, AccountCreatedActivity.class));
            }
        });

    }
}