package com.vigyos.vigyoscentercrm.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Activity.AccountActivity;
import com.vigyos.vigyoscentercrm.Activity.LoginActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Model.CancelledItemModel;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelledFragment extends Fragment {

    private View view;
    private Dialog dialog;
    private ArrayList<CancelledItemModel> cancelledItemModels = new ArrayList<>();
    private TextView noCancelled;
    private final Activity activity;

    public CancelledFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cancelled, container, false);
        noCancelled = view.findViewById(R.id.noCancelled);
        cancelledApi();
        return view;
    }

    private void cancelledApi(){
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().serviceRequest(SplashActivity.prefManager.getToken(), SplashActivity.prefManager.getUserID());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("2016","onResponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            if (jsonObject1.getString("status").equalsIgnoreCase("REJECTED") ){
                                CancelledItemModel cancelledItemModel = new CancelledItemModel();
                                cancelledItemModel.setService_name(jsonObject1.getString("service_name"));
                                cancelledItemModel.setStatus(jsonObject1.getString("status"));
                                cancelledItemModel.setUser_service_id(jsonObject1.getString("user_service_id"));
                                cancelledItemModel.setCustomer_name(jsonObject1.getString("customer_name"));
                                cancelledItemModel.setCustomer_phone(jsonObject1.getString("customer_phone"));
                                cancelledItemModel.setPrice(jsonObject1.getInt("price"));
                                cancelledItemModel.setCreated_time(jsonObject1.getString("created_time"));
                                cancelledItemModels.add(cancelledItemModel);
                            }
                        }
                        if(cancelledItemModels.isEmpty()){
                            noCancelled.setVisibility(View.VISIBLE);
                        } else {
                            noCancelled.setVisibility(View.GONE);
                        }
                        showCancelledList();
                    } else {
                        SplashActivity.prefManager.setClear();
                        startActivity(new Intent(activity, LoginActivity.class));
                        activity.finish();
                        Snackbar.make(activity.findViewById(android.R.id.content), "Session expired please login again", Snackbar.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
            }
        });
    }

    private void showCancelledList(){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        CancelledListAdapter cancelledListAdapter = new CancelledListAdapter(cancelledItemModels, activity);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cancelledListAdapter);
    }

    private void pleaseWait(){
        dialog = new Dialog(activity);
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

    private class CancelledListAdapter extends RecyclerView.Adapter<CancelledListAdapter.Holder> {

        private ArrayList<CancelledItemModel> cancelledItemModels;
        private Activity activity;

        public CancelledListAdapter(ArrayList<CancelledItemModel> cancelledItemModels, Activity activity) {
            this.cancelledItemModels = cancelledItemModels;
            this.activity = activity;
        }

        @NonNull
        @Override
        public CancelledListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.layout_show_order_item, parent,false);
            return new CancelledListAdapter.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CancelledListAdapter.Holder holder, int position) {
            CancelledItemModel itemModel = cancelledItemModels.get(position);
            holder.serviceName.setText(itemModel.getService_name());
            holder.orderID.setText("#Oreder: "+itemModel.getUser_service_id());
            holder.price.setText("₹"+ itemModel.getPrice());
            holder.status.setText(itemModel.getStatus());
            holder.statusBackground.setBackgroundResource(R.drawable.cancelled);
        }

        @Override
        public int getItemCount() {
            return cancelledItemModels.size();
        }

        private class Holder extends RecyclerView.ViewHolder {

            private TextView serviceName;
            private TextView orderID;
            private TextView price;
            private TextView status;
            private RelativeLayout statusBackground;

            public Holder(@NonNull View itemView) {
                super(itemView);
                serviceName = itemView.findViewById(R.id.serviceName);
                orderID = itemView.findViewById(R.id.orderID);
                price = itemView.findViewById(R.id.price);
                status = itemView.findViewById(R.id.status);
                statusBackground = itemView.findViewById(R.id.statusBackground);
            }
        }
    }
}