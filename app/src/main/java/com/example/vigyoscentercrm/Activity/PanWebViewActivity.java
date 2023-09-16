package com.example.vigyoscentercrm.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.vigyoscentercrm.R;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PanWebViewActivity extends AppCompatActivity {

    private ValueCallback<Uri[]> uploadMessage;
    private static final int FILE_CHOOSER_RESULT_CODE = 1;
    //public  = ""; // String to store the encdata value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pan_web_view);
        WebView webView = findViewById(R.id.webView);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String encData = intent.getStringExtra("encData");
        webView.getSettings().setJavaScriptEnabled(true);

        // Define a JavaScript function to submit the form
        String javascriptFunction = "window.onload = function() {\n" +
                "  console.log('Redirecting...');\n" +
                "  document.forms[0].submit();\n" +
                "}\n";

        String modifiedHtmlContent = "<html>\n" +
                "<body>\n" +
                "<form action=\"" + url + "\" method=\"POST\" >\n" +
                "  <textarea name=\"encdata\" style=\"display:none\">" + encData + "</textarea>\n" +
                "</form>\n" +
                "</body>\n" +
                "<script>\n" + javascriptFunction + "</script>\n" + // Include the JavaScript function
                "</html>";

        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            // For Android 5.0+
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }
                uploadMessage = filePathCallback;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Choose File"), FILE_CHOOSER_RESULT_CODE);
                return true;
            }
        });
        webView.loadDataWithBaseURL(null, modifiedHtmlContent, "text/html", "UTF-8", null);
    }


    private static class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            // Handle URL loading events here, if needed
            // Return true to indicate that the URL loading event has been handled.

//            String pageUrl = view.getUrl(); // This contains the URL of the loaded page
//
//            // Parse the URL to extract the encdata value
//            Uri uri = Uri.parse(pageUrl);
//            String encDataValue = uri.getQueryParameter("encdata");
//
//            if (encDataValue != null) {
//                String encData = encDataValue;
//                // encData now contains the extracted encdata value
//                Log.d("WebViewResponse", "encdata: " + encData);
//
//                Log.i("852146", "encdata : - "+ encData );
//            }

//            String Url = view.getUrl();
//
//            Log.i("121254","url: - " + Url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            String pageUrl = view.getUrl(); // This contains the URL of the loaded page

            // Parse the URL to extract the encdata value
            Uri uri = Uri.parse(pageUrl);
            String encDataValue = uri.getQueryParameter("encdata");

            if (encDataValue != null) {
                String encData = encDataValue;
                // encData now contains the extracted encdata value
                Log.d("WebViewResponse", "encdata: " + encData);

                Log.i("852146", "encdata : - "+ encData );
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (uploadMessage == null)
                return;
            Uri[] results = null;
            if (resultCode == RESULT_OK && data != null) {
                String dataString = data.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
            uploadMessage.onReceiveValue(results);
            uploadMessage = null;
        }
    }
}