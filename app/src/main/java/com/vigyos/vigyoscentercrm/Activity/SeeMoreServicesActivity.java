package com.vigyos.vigyoscentercrm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.os.BuildCompat;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.vigyos.vigyoscentercrm.Model.SeeMoreServiceModel;
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
public class SeeMoreServicesActivity extends AppCompatActivity {

    private ImageView ivBack;
    private RecyclerView recyclerViewMore;
    private ShowAllServicesAdapter showAllServicesAdapter;
    private ArrayList<SeeMoreServiceModel> seeMoreServiceModels = new ArrayList<>();
    private Dialog dialog;
    private int page = 1;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_more_services);
        initialization();
        declaration();
    }

    private void initialization() {
        recyclerViewMore = findViewById(R.id.recyclerViewMore);
        ivBack = findViewById(R.id.ivBack);
    }

    private void declaration() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        seeMoreServiceModels.clear();
        showAllService();
    }

    private void showAllService(){
        showAllServicesAdapter = new ShowAllServicesAdapter(seeMoreServiceModels, SeeMoreServicesActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SeeMoreServicesActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recyclerViewMore.setLayoutManager(gridLayoutManager);
        recyclerViewMore.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMore.setAdapter(showAllServicesAdapter);
        recyclerViewMore.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading && !recyclerView.canScrollVertically(1)) {
                    page++;  // Increment the page
                    serviceList(page);
                    isLoading = true;
                }
            }
        });

        serviceList(page);  // Load the first page
    }

    private void serviceList(int page){
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().servicesGCList(SplashActivity.prefManager.getToken(), page);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016","onResponse" + response);
                dismissDialog();
                isLoading = false;
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")){
                        if (jsonObject.has("data")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i<jsonArray.length(); i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                SeeMoreServiceModel serviceModel = new SeeMoreServiceModel();
                                if (jsonObject1.has("service_group_name") && !jsonObject1.getString("service_group_name").equalsIgnoreCase("Pan")) {
                                    if (jsonObject1.has("service_group_name")) {
                                        serviceModel.setServiceName(jsonObject1.getString("service_group_name"));
                                    }
                                    if (jsonObject1.has("service_group_id")) {
                                        serviceModel.setServiceID(jsonObject1.getString("service_group_id"));
                                    }
                                    if (jsonObject1.has("service_group_icon")){
                                        serviceModel.setServiceIcon(jsonObject1.getString("service_group_icon"));
                                    } else {
                                        serviceModel.setServiceIcon("https://vigyos-upload-files.s3.amazonaws.com/bbfba05a-d49a-47ef-ab84-40b8d7398d25");
                                    }
                                    seeMoreServiceModels.add(serviceModel);
                                }
                            }
                        }
                        showAllServicesAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(SeeMoreServicesActivity.this, "Your session has expired. Please log in again to continue.", Toast.LENGTH_SHORT).show();
                        SplashActivity.prefManager.setClear();
                        startActivity(new Intent(SeeMoreServicesActivity.this, LoginActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("2016","onFailure" + t);
                isLoading = false;
            }
        });
    }

    private void pleaseWait(){
        dialog = new Dialog(SeeMoreServicesActivity.this);
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
    public void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private class ShowAllServicesAdapter extends RecyclerView.Adapter<ShowAllServicesAdapter.Holder>{

        private ArrayList<SeeMoreServiceModel> seeMoreServiceModels;
        private Activity activity;

        public ShowAllServicesAdapter(ArrayList<SeeMoreServiceModel> seeMoreServiceModels, Activity activity) {
            this.seeMoreServiceModels = seeMoreServiceModels;
            this.activity = activity;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.layout_seemaore_item, parent, false);
            return new Holder(view);
        }

        @Override
        public int getItemCount() {
            return seeMoreServiceModels.size();
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            SeeMoreServiceModel serviceModel = seeMoreServiceModels.get(position);
            holder.serviceTitle.setText(serviceModel.getServiceName());
            Picasso.get().load(serviceModel.getServiceIcon()).into(holder.iconImage);
            holder.totalCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, SubCatServiceActivity.class);
                    intent.putExtra("serviceID", serviceModel.getServiceID());
                    intent.putExtra("serviceName", serviceModel.getServiceName());
                    startActivity(intent);
                }
            });
        }

        private class Holder extends RecyclerView.ViewHolder{

            private ImageView iconImage;
            private TextView serviceTitle;
            private CardView totalCardView;

            public Holder(@NonNull View itemView) {
                super(itemView);
                iconImage =  itemView.findViewById(R.id.iconImage);
                serviceTitle =  itemView.findViewById(R.id.serviceTitle);
                totalCardView =  itemView.findViewById(R.id.TotalCardView);
            }
        }
    }
}