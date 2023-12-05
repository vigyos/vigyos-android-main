package com.vigyos.vigyoscentercrm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class CategoryDetailsActivity extends AppCompatActivity {

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);
        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("categoryData");
        api(categoryName);
    }

    private void api(String desiredCategory) {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().payBillOperator(SplashActivity.prefManager.getToken(), "online");
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2019", "onResponse " + response);
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        JSONObject dataObject = jsonObject.getJSONObject("data");

                        // Check if the desired category exists in the response
                        if (dataObject.has(desiredCategory)) {
                            JSONObject categoryObject = dataObject.getJSONObject(desiredCategory);
                            String iconUrl = categoryObject.getString("icon");
                            JSONArray dataArray = categoryObject.getJSONArray("data");

                            // Loop through the "data" array
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject itemObject = dataArray.getJSONObject(i);

                                // Accessing item properties
                                String id = itemObject.getString("id");
                                String name = itemObject.getString("name");
                                String viewBill = itemObject.getString("viewbill");
                                String regex = itemObject.optString("regex", ""); // Use optString to handle null values

                                // Perform actions with the extracted data
                                // Example: Log or use the data in your Android application
                                Log.i("2019", "Category: " + desiredCategory);
                                Log.i("2019", "ID: " + id);
                                Log.i("2019", "Name: " + name);
                                Log.i("2019", "View Bill: " + viewBill);
                                Log.i("2019", "Regex: " + regex);
                            }
                        } else {
                            Log.i("2019", "Desired category not found in the response");
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2019", "onFailure " + t);
                dismissDialog();
            }
        });
    }

    private void pleaseWait() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_loader);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }
}