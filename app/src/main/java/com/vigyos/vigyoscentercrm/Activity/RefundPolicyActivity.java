package com.vigyos.vigyoscentercrm.Activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.vigyos.vigyoscentercrm.R;

public class RefundPolicyActivity extends AppCompatActivity {

    public WebView refundWebView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_policy);
        progressBar = findViewById(R.id.progress_bar);
        refundWebView = findViewById(R.id.refundWebView);
        refundWebView.getSettings().setJavaScriptEnabled(true);
        refundWebView.loadUrl("file:///android_asset/refund.html");
        refundWebView.setWebViewClient(new WebViewClient(){
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
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}