package com.vigyos.vigyoscentercrm.Activity.BBPS;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Adapter.MunicipalityAdapter;
import com.vigyos.vigyoscentercrm.Adapter.MunicipalityItem;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class BBPSServicesActivity extends AppCompatActivity {

    private Dialog dialog;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbpsservices);
        Initialization();
        Declaration();
    }

    private void Initialization() {
        ivBack = findViewById(R.id.ivBack);
    }

    private void Declaration() {
        fetchDataFromApi();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fetchDataFromApi() {
        // Assuming you have a RecyclerView in your layout with the id recyclerViewMunicipality
        RecyclerView recyclerView = findViewById(R.id.recyclerViewBBPS);
        List<MunicipalityItem> municipalityList = new ArrayList<>(); // Populate this list with your data

        MunicipalityAdapter adapter = new MunicipalityAdapter(municipalityList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().payBillOperator(SplashActivity.prefManager.getToken(), "online");
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        if (jsonObject.has("data")) {
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            // Iterate through keys in the "data" object
                            Iterator<String> keys = dataObject.keys();
                            while (keys.hasNext()) {
                                String categoryName = keys.next();
                                JSONObject categoryObject = dataObject.getJSONObject(categoryName);
                                if (categoryObject.has("icon")) {
                                    String categoryIconUrl = categoryObject.getString("icon");
                                    municipalityList.add(new MunicipalityItem(categoryName, categoryIconUrl));
                                }
                            }
                            // Notify the adapter that the data set has changed
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2016", "onFailure " + t);
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