package com.vigyos.vigyoscentercrm.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class PanWebViewActivity extends AppCompatActivity {

    private ValueCallback<Uri[]> uploadMessage;
    private static final int FILE_CHOOSER_RESULT_CODE = 1;
    private static Dialog dialog;
    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pan_web_view);
        WebView webView = findViewById(R.id.webView);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String encData = intent.getStringExtra("encData");
        webView.getSettings().setJavaScriptEnabled(true);

        activity = this;

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

        if (BuildCompat.isAtLeastT()) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                    () -> {
                        Log.i("852145", "onBack");
                    }
            );
        }
    }


    private static class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            // Handle URL loading events here, if needed
            // Return true to indicate that the URL loading event has been handled.
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            String pageUrl = view.getUrl(); // This contains the URL of the loaded page

            Log.i("852146", "pageUrl: " + pageUrl);

            // Check if the URL starts with a valid scheme
            if (Uri.parse(pageUrl).isHierarchical()) {
                // Parse the URL to extract the encdata value
                Uri uri = Uri.parse(pageUrl);
                String encDataValue = uri.getQueryParameter("encdata");

                if (encDataValue != null) {
                    String encData = encDataValue;
                    // encData now contains the extracted encdata value
                    Log.d("WebViewResponse", "encdata: 001" + encData);

                    Log.i("852146", "encdata: 002" + encData);
                    panVerifyAPI(encData);
                }
            } else {
                // Handle non-hierarchical URLs if necessary
                Log.i("852146", "non-hierarchical URLs");
            }
        }
    }

    private static void panVerifyAPI(String encData) {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().panVerify(SplashActivity.prefManager.getToken(), encData);
        objectCall.enqueue(new Callback<Object>() {
            @BuildCompat.PrereleaseSdkCheck
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016","onResponse " + response);
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        if (jsonObject.has("data")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            if (jsonObject1.has("amount")) {
                                String amount = jsonObject1.getString("amount");
                            }
                            if (jsonObject1.has("ackno")) {
                                String ackno = jsonObject1.getString("ackno");
                            }
                            if (jsonObject1.has("message")) {
                                Toast.makeText(activity, jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            activity.startActivity(new Intent(activity, MainActivity.class));
                            activity.finish();
                        }
                    } else {
                        if (jsonObject.has("message")) {
                            Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2016","onFailure " + t);
                dismissDialog();
            }
        });
    }

    private static void pleaseWait() {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_loader);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private static void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
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