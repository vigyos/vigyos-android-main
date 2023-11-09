package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.vigyos.vigyoscentercrm.R;

@BuildCompat.PrereleaseSdkCheck
public class BiometricLockActivity extends AppCompatActivity {

    private Switch switchView;
    private ImageView help;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometrc_lock);
        switchView = findViewById(R.id.idSwitch);
        help = findViewById(R.id.help);
        ivBack = findViewById(R.id.ivBack);
        switchView.setChecked(SplashActivity.prefManager.getBiometricLock());
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SplashActivity.prefManager.setBiometricLock(true);
                    Log.i("201445","Biometric Auth is enable");
//                    Toast.makeText(BiometricLockActivity.this, "Switch is Checked", Toast.LENGTH_SHORT).show();
                } else {
                    SplashActivity.prefManager.setBiometricLock(false);
                    Log.i("201445","Biometric Auth is disable");
//                    Toast.makeText(BiometricLockActivity.this, "Switch is UnChecked", Toast.LENGTH_SHORT).show();
                }
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BiometricLockActivity.this, HelpAndSupportActivity.class));
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}