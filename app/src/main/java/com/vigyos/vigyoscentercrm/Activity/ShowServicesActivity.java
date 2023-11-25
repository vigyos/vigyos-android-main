package com.vigyos.vigyoscentercrm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Adapter.DocumentItemAdapter;
import com.vigyos.vigyoscentercrm.Model.DocumentItemModel;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class ShowServicesActivity extends AppCompatActivity {

    private String serviceName;
    private String serviceDetails;
    private int price;
    private String service;
    private Intent intent;
    private Spinner spinner;
    private TextView serviceNameText;
    private TextView serviceDetailsText;
    private TextView priceText;
    private ArrayList<DocumentItemModel> documentItemModels = new ArrayList<>();
    private ArrayList<String> documentsName = new ArrayList<>();
    private DocumentItemAdapter documentItemAdapter;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    private Dialog dialog;
    private RelativeLayout applyNow;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sesrvices);
        initialization();
        declaration();
    }

    private void initialization() {
        ivBack = findViewById(R.id.ivBack);
        serviceNameText = findViewById(R.id.serviceName);
        serviceDetailsText = findViewById(R.id.serviceDetails);
        priceText = findViewById(R.id.price);
        recyclerView = findViewById(R.id.recyclerView);
        spinner = findViewById(R.id.spinner);
        applyNow = findViewById(R.id.applyNow);
    }

    private void declaration() {
        intent = getIntent();
        service = intent.getStringExtra("id");
        serviceData();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(ShowServicesActivity.this, R.anim.viewpush));
                onBackPressed();
            }
        });
        applyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(ShowServicesActivity.this, R.anim.viewpush));
                if (!SplashActivity.prefManager.getServices()) {
                    Intent intent1 = new Intent(ShowServicesActivity.this, BuyServiceActivity.class);
                    intent1.putExtra("serviceID", service);
                    intent1.putExtra("serviceName", serviceName);
                    startActivity(intent1);
                } else {
                    startActivity(new Intent(ShowServicesActivity.this, PlansActivity.class));
                }
            }
        });
    }

    private void serviceDocuments(){
        documentItemAdapter = new DocumentItemAdapter(documentItemModels, ShowServicesActivity.this);
        gridLayoutManager = new GridLayoutManager(this, 1 , GridLayoutManager.VERTICAL, false);
        // Disable scrolling for RecyclerView
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(documentItemAdapter);
    }

    private void serviceData() {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().serviceDetails(SplashActivity.prefManager.getToken(), service);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse" + response);
                dismissDialog();
                try {
                    JSONObject  jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            serviceName = jsonObject2.getString("service_name");
                            serviceDetails = jsonObject2.getString("description");
                            serviceDetails = serviceDetails.replaceAll("<p>", "").replaceAll("</p>", "");
                            price = jsonObject2.getInt("price");
                            JSONArray jsonArray1 = jsonObject2.getJSONArray("required_data");
                            for(int k = 0; k <jsonArray1.length(); k++){
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(k);
                                DocumentItemModel itemModel = new DocumentItemModel();
                                itemModel.setDocument_name(jsonObject1.getString("document_name"));
                                documentItemModels.add(itemModel);
                                documentsName.add(jsonObject1.getString("document_name"));
                            }
                        }
                        serviceNameText.setText(serviceName);
                        serviceDetailsText.setText(serviceDetails);
                        float v = (float) price;
                        priceText.setText("₹"+v);
                        serviceDocuments();
                    } else {
                        if (jsonObject.has("message")) {
                            Toast.makeText(ShowServicesActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            SplashActivity.prefManager.setClear();
                            startActivity(new Intent(ShowServicesActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("2016", "onFailure" + t);
                Toast.makeText(ShowServicesActivity.this, "Maintenance underway. We'll be back soon.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void spinner(){
        ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, documentsName);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);
    }

    private void pleaseWait(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_loader);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void dismissDialog(){
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}