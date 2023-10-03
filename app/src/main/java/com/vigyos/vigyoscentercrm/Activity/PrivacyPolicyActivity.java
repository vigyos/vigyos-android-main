package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.vigyos.vigyoscentercrm.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        progressBar = findViewById(R.id.progress_bar);
        WebView privacyPolicy = findViewById(R.id.privacyPolicy);
        privacyPolicy.getSettings().setJavaScriptEnabled(true);
        privacyPolicy.loadUrl("file:///android_asset/privacy_policy.html");
        privacyPolicy.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                progressBar.show();
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                progressBar.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}