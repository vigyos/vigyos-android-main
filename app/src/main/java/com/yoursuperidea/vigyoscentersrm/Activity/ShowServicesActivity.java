package com.yoursuperidea.vigyoscentersrm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.yoursuperidea.vigyoscentersrm.R;

public class ShowServicesActivity extends AppCompatActivity {

    private TextView serviceName;
    private String serviceDetails;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sesrvices);

        intent = getIntent();
        serviceDetails = intent.getStringExtra("details");
        serviceName = findViewById(R.id.serviceDetails);
        serviceName.setText(serviceDetails);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(ShowServicesActivity.this, R.anim.viewpush));
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}