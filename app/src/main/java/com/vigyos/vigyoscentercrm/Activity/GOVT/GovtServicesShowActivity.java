package com.vigyos.vigyoscentercrm.Activity.GOVT;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
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
public class GovtServicesShowActivity extends AppCompatActivity {

    private Dialog dialog;
    private ImageView ivBack;
    private TextView groupNameText;
    private RecyclerView recyclerView;
    private ArrayList<GovtServicesListModel> govtServicesListModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govt_services_show);
        Initialization();
        Declaration();
    }

    private void Initialization() {
        ivBack = findViewById(R.id.ivBack);
        groupNameText = findViewById(R.id.groupName);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void Declaration() {
        Intent intent = getIntent();
        String groupId = intent.getStringExtra("group_id");
        String groupName = intent.getStringExtra("group_name");
        groupNameText.setText(groupName);
        showServices(groupId);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showServices(String groupId) {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().govtServices(SplashActivity.prefManager.getToken());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016","onResponse "+response);
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        if (jsonObject.has("data")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i<jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                if (jsonObject1.has("group_id") && jsonObject1.getString("group_id").equalsIgnoreCase(groupId)) {
                                    if (jsonObject1.has("items")) {
                                        JSONArray itemArray = jsonObject1.getJSONArray("items");
                                        for (int k = 0; k < itemArray.length(); k++){
                                            JSONObject object = itemArray.getJSONObject(k);
                                            GovtServicesListModel listModel = new GovtServicesListModel();
                                            if (object.has("service_icon")) {
                                                listModel.setService_icon(object.getString("service_icon"));
                                            }
                                            if (object.has("service_link")) {
                                                listModel.setService_link(object.getString("service_link"));
                                            }
                                            if (object.has("service_active")) {
                                                listModel.setService_active(object.getBoolean("service_active"));
                                            }
                                            if (object.has("govt_service_name")) {
                                                listModel.setGovt_service_name(object.getString("govt_service_name"));
                                            }
                                            govtServicesListModels.add(listModel);
                                        }
                                    }
                                }
                            }
                        }
                        callAdapter();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2016","onFailure "+t);
                dismissDialog();
            }
        });
    }

    private void callAdapter() {
        ShowGovtServicesAdapter govtServicesAdapter = new ShowGovtServicesAdapter(govtServicesListModels);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(govtServicesAdapter);
    }

    private class ShowGovtServicesAdapter extends RecyclerView.Adapter<ShowGovtServicesAdapter.Holder> {

        private ArrayList<GovtServicesListModel> govtServicesListModels;

        public ShowGovtServicesAdapter(ArrayList<GovtServicesListModel> govtServicesListModels) {
            this.govtServicesListModels = govtServicesListModels;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_show_govt_services, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            GovtServicesListModel model = govtServicesListModels.get(position);
            Picasso.get()
                    .load(model.getService_icon())
                    .into(holder.iconImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progress_bar.setVisibility(View.GONE);
                        }
                        @Override
                        public void onError(Exception e) { }
                    });
            holder.titleName.setText(model.getGovt_service_name());
            holder.mainLyt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(model.getService_link())));

//                    Intent intent = new Intent(GovtServicesShowActivity.this, GovtServicesWebViewActivity.class);
//                    intent.putExtra("servicesLink", model.getService_link());
//                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return govtServicesListModels.size();
        }

        private class Holder extends RecyclerView.ViewHolder {

            RelativeLayout mainLyt;
            ImageView iconImage;
            TextView titleName;
            ProgressBar progress_bar;

            public Holder(@NonNull View itemView) {
                super(itemView);
                mainLyt = itemView.findViewById(R.id.mainLyt);
                iconImage = itemView.findViewById(R.id.iconImage);
                titleName = itemView.findViewById(R.id.titleName);
                progress_bar = itemView.findViewById(R.id.progress_bar);
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