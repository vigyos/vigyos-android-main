package com.vigyos.vigyoscentercrm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.vigyos.vigyoscentercrm.Model.ServiceListModel;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCatServiceActivity extends AppCompatActivity {

    private Dialog dialog;
    private ArrayList<ServiceListModel> serviceListModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_cat_service);
        Intent intent = getIntent();
        String serviceID = intent.getStringExtra("serviceID");
        String serviceName = intent.getStringExtra("serviceName");
        TextView serName = findViewById(R.id.serviceName);
        serName.setText(serviceName);

        services(serviceID);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void services(String serviceID){
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().servicesGC(SplashActivity.prefManager.getToken(), serviceID);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("45621", "onResponse "+ response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONArray jsonArray1 = jsonObject1.getJSONArray("services");
                            for (int k = 0; k < jsonArray1.length(); k++){
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(k);
                                ServiceListModel serviceListModel = new ServiceListModel();
                                serviceListModel.setService_id(jsonObject2.getString("service_id"));
                                serviceListModel.setService_name(jsonObject2.getString("service_name"));
                                serviceListModel.setPrice(jsonObject2.getInt("price"));
                                serviceListModels.add(serviceListModel);
                            }
                        }
                        callShowAdapter();
                    } else {
                        SplashActivity.prefManager.setClear();
                        startActivity(new Intent(SubCatServiceActivity.this, LoginActivity.class));
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
                Log.i("45621", "onFailure "+ t);
            }
        });
    }

    private void callShowAdapter(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ShowServiceListAdapter showServiceListAdapter = new ShowServiceListAdapter(serviceListModels, SubCatServiceActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SubCatServiceActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(showServiceListAdapter);
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

    private class ShowServiceListAdapter extends RecyclerView.Adapter<ShowServiceListAdapter.Holder>{

        private ArrayList<ServiceListModel> serviceListModels;
        private Activity activity;

        public ShowServiceListAdapter(ArrayList<ServiceListModel> serviceListModels, Activity activity) {
            this.serviceListModels = serviceListModels;
            this.activity = activity;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_service_list, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            ServiceListModel listModel = serviceListModels.get(position);
            holder.service_name.setText(listModel.getService_name());
            holder.amount.setText("₹ "+ listModel.getPrice());
            holder.cardNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ShowServicesActivity.class);
                    intent.putExtra("id", listModel.getService_id());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return serviceListModels.size();
        }

        public class Holder extends RecyclerView.ViewHolder{

            private TextView service_name;
            private TextView amount;
            private CardView cardNext;

            public Holder(@NonNull View itemView) {
                super(itemView);
                service_name = itemView.findViewById(R.id.service_name);
                amount = itemView.findViewById(R.id.amount);
                cardNext = itemView.findViewById(R.id.cardNext);

            }
        }
    }
}