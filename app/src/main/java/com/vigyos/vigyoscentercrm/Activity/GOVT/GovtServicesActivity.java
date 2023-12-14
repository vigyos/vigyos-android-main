package com.vigyos.vigyoscentercrm.Activity.GOVT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Model.GovtServicesListModel;
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
public class GovtServicesActivity extends AppCompatActivity {

    private Dialog dialog;
    private final ArrayList<GovtServicesListModel> govtServicesListModels =  new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govt_services);
        recyclerView = findViewById(R.id.recyclerView);
        servicesAPI();
        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void servicesAPI(){
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().govtServices(SplashActivity.prefManager.getToken());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse "+response);
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        if (jsonObject.has("data")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                GovtServicesListModel listModel = new GovtServicesListModel();
                                if (jsonObject1.has("group_id")) {
                                    listModel.setGroup_id(jsonObject1.getString("group_id"));
                                }
                                if (jsonObject1.has("group_icon")) {
                                    listModel.setGroup_id(jsonObject1.getString("group_icon"));
                                }
                                if (jsonObject1.has("group_name")) {
                                    listModel.setGroup_name(jsonObject1.getString("group_name"));
                                }
                                if (jsonObject1.has("active")) {
                                    listModel.setActive(jsonObject1.getBoolean("active"));
                                }
                                govtServicesListModels.add(listModel);
                            }
                        }
                    }
                    serviceList();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2016", "onFailure "+t);
                dismissDialog();
            }
        });
    }

    private void serviceList() {
        GovtServicesAdapter govtServicesAdapter = new GovtServicesAdapter(govtServicesListModels, GovtServicesActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(govtServicesAdapter);
    }

    public class GovtServicesAdapter extends RecyclerView.Adapter<GovtServicesAdapter.MyViewHolder>{

        private final ArrayList<GovtServicesListModel> searchListArray;
        private final Activity activity;

        public GovtServicesAdapter(ArrayList<GovtServicesListModel> searchServicesModels, Activity activity) {
            super();
            this.searchListArray = searchServicesModels;
            this.activity = activity;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_search_services_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            GovtServicesListModel model = searchListArray.get(position);
            holder.listName.setText(model.getGroup_name());
            holder.next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, GovtServicesShowActivity.class);
                    intent.putExtra("group_id", model.getGroup_id());
                    intent.putExtra("group_icon", model.getGroup_icon());
                    intent.putExtra("group_name", model.getGroup_name());
                    intent.putExtra("active", model.getActive());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return searchListArray.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            TextView listName;
            RelativeLayout next;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                listName = itemView.findViewById(R.id.listName);
                next = itemView.findViewById(R.id.next_ll);
            }
        }
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