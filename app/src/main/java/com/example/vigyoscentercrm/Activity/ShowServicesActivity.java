package com.example.vigyoscentercrm.Activity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.vigyoscentercrm.Adapter.DocumentItemAdapter;
import com.example.vigyoscentercrm.Model.DocumentItemModel;
import com.example.vigyoscentercrm.Model.SearchServicesModel;
import com.example.vigyoscentercrm.R;
import com.example.vigyoscentercrm.Retrofit.RetrofitClient;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowServicesActivity extends AppCompatActivity {

    private String serviceName;
    private String serviceDetails;
    private String price;
    private String service;
    private Intent intent;
    private TextView serviceNameText;
    private TextView serviceDetailsText;
    private TextView priceText;
    private ArrayList<DocumentItemModel> documentItemModels = new ArrayList<>();
    private DocumentItemAdapter documentItemAdapter;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sesrvices);

        intent = getIntent();
        service = intent.getStringExtra("service");

        serviceNameText = findViewById(R.id.serviceName);
        serviceDetailsText = findViewById(R.id.serviceDetails);
        priceText = findViewById(R.id.price);
        recyclerView = findViewById(R.id.recyclerView);

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
        documentItemAdapter = new DocumentItemAdapter(documentItemModels, ShowServicesActivity.this);
        gridLayoutManager = new GridLayoutManager(this, 1 , GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(documentItemAdapter);
    }

    private void serviceData() {
        try {
            JSONObject jsonObject = new JSONObject(service);
            serviceName = jsonObject.getString("service_name");
            serviceDetails = jsonObject.getString("description");
            price = jsonObject.getString("price");

            JSONArray jsonObject1 = jsonObject.getJSONArray("required_data");
            for (int i = 0; i < jsonObject1.length(); i++ ){
                JSONObject jsonObject2 = jsonObject1.getJSONObject(i);
                DocumentItemModel itemModel = new DocumentItemModel();
                itemModel.setDocument_name(jsonObject2.getString("document_name"));
                documentItemModels.add(itemModel);
            }

            serviceNameText.setText(serviceName);
            serviceDetailsText.setText(serviceDetails);
            priceText.setText("₹"+price);
            serviceDocuments();
            Log.i("252525","jsonObject " + jsonObject);
        }catch (JSONException err){
            Log.d("Error", err.toString());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}