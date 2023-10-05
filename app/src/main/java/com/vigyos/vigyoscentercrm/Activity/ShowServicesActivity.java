package com.vigyos.vigyoscentercrm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
//    private RecyclerView recyclerView;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sesrvices);

        intent = getIntent();
        service = intent.getStringExtra("id");

        serviceNameText = findViewById(R.id.serviceName);
        serviceDetailsText = findViewById(R.id.serviceDetails);
        priceText = findViewById(R.id.price);
//        recyclerView = findViewById(R.id.recyclerView);
        spinner = findViewById(R.id.spinner);

        serviceData();

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(ShowServicesActivity.this, R.anim.viewpush));
                onBackPressed();
            }
        });
    }

    private void serviceDocuments(){
//        documentItemAdapter = new DocumentItemAdapter(documentItemModels, ShowServicesActivity.this);
//        gridLayoutManager = new GridLayoutManager(this, 1 , GridLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(documentItemAdapter);
    }

    private void serviceData() {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().serviceDetails(SplashActivity.prefManager.getToken(), service);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("2016", "onResponse" + response);
                try {
                    JSONObject  jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            serviceName = jsonObject2.getString("service_name");
                            serviceDetails = jsonObject2.getString("description");
                            price = jsonObject2.getInt("price");
                            JSONArray jsonArray1 = jsonObject2.getJSONArray("required_data");
                            for(int k = 0; k <jsonArray1.length(); k++){
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(k);
//                              DocumentItemModel itemModel = new DocumentItemModel();
//                              itemModel.setDocument_name(jsonObject1.getString("document_name"));
//                              documentItemModels.add(itemModel);
                                documentsName.add(jsonObject1.getString("document_name"));
                            }
                        }
                        serviceNameText.setText(serviceName);
                        serviceDetailsText.setText(serviceDetails);
                        priceText.setText("₹"+price);
//                      serviceDocuments();
                        spinner();
                    } else {
                        SplashActivity.prefManager.setClear();
                        startActivity(new Intent(ShowServicesActivity.this, LoginActivity.class));
                        finish();
                        Snackbar.make(findViewById(android.R.id.content), "Session expired please login again", Snackbar.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("2016", "onFailure" + t);
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
    }
}