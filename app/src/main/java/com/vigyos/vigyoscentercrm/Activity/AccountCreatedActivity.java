package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vigyos.vigyoscentercrm.R;

@BuildCompat.PrereleaseSdkCheck
public class AccountCreatedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_created);

        findViewById(R.id.Continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountCreatedActivity.this, LoginActivity.class));
            }
        });
    }
}