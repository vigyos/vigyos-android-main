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
import com.vigyos.vigyoscentercrm.Activity.LoginActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Model.ProcessingItemModel;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingFragment extends Fragment {

    private Dialog dialog;
    private PendingListAdapter completedListAdapter;
    private ArrayList<ProcessingItemModel> processingItemModels = new ArrayList<>();
    private TextView noPending;
    private int page = 1;
    private boolean isLoading = false;

    public PendingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending, container, false);
        noPending = view.findViewById(R.id.noPending);
        showPendingList(view);
        return view;
    }

    private void showPendingList(View view){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        completedListAdapter = new PendingListAdapter(processingItemModels, getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(completedListAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading && !recyclerView.canScrollVertically(1)) {
                    processingApi(page++);
                    isLoading = true;
                }
            }
        });
        processingApi(page);
    }

    private void processingApi(int page){
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().serviceRequest(SplashActivity.prefManager.getToken(), SplashActivity.prefManager.getUserID(), page, "PENDING");
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016","onResponse" + response);
                dismissDialog();
                isLoading = false;
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            if (jsonObject1.getString("status").equalsIgnoreCase("PENDING")){
                                ProcessingItemModel processingItemModel = new ProcessingItemModel();
                                if (jsonObject1.has("service_name")) {
                                    processingItemModel.setService_name(jsonObject1.getString("service_name"));
                                }
                                if (jsonObject1.has("status")) {
                                    processingItemModel.setStatus(jsonObject1.getString("status"));
                                }
                                if (jsonObject1.has("user_service_id")) {
                                    processingItemModel.setUser_service_id(jsonObject1.getString("user_service_id"));
                                }
                                if (jsonObject1.has("customer_name")) {
                                    processingItemModel.setCustomer_name(jsonObject1.getString("customer_name"));
                                }
                                if (jsonObject1.has("customer_phone")) {
                                    processingItemModel.setCustomer_phone(jsonObject1.getString("customer_phone"));
                                }
                                if (jsonObject1.has("customer_email")) {
                                    processingItemModel.setCustomer_email(jsonObject1.getString("customer_email"));
                                }
                                if (jsonObject1.has("price")) {
                                    processingItemModel.setPrice(jsonObject1.getInt("price"));
                                }
                                if (jsonObject1.has("created_time")) {
                                    processingItemModel.setCreated_time(jsonObject1.getString("created_time"));
                                }
                                processingItemModels.add(processingItemModel);
                            }
                        }
                        if(processingItemModels.isEmpty()){
                            noPending.setVisibility(View.VISIBLE);
                        } else {
                            noPending.setVisibility(View.GONE);
                        }
                        completedListAdapter.notifyDataSetChanged();
                    } else {
                        SplashActivity.prefManager.setClear();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Session expired please login again", Snackbar.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2016","onFailure" + t);
                dismissDialog();
                isLoading = false;
            }
        });
    }

    private void pleaseWait(){
        dialog = new Dialog(getActivity());
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

    private class PendingListAdapter extends RecyclerView.Adapter<PendingListAdapter.Holder> {

        private ArrayList<ProcessingItemModel> processingItemModels;
        private Activity activity;

        public PendingListAdapter(ArrayList<ProcessingItemModel> processingItemModels, Activity activity) {
            this.processingItemModels = processingItemModels;
            this.activity = activity;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.layout_show_order_item, parent,false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            ProcessingItemModel itemModel = processingItemModels.get(position);
            holder.serviceName.setText(itemModel.getService_name());
            holder.orderID.setText("#"+itemModel.getUser_service_id());
            holder.price.setText("₹ "+ itemModel.getPrice());
            holder.status.setText(itemModel.getStatus());
            holder.status.setTextColor(Color.parseColor("#FFA900"));
            holder.statusBackground.setBackgroundResource(R.drawable.pending);
            holder.emailID.setText(itemModel.getCustomer_email());
            holder.phoneNumber.setText("+91-"+itemModel.getCustomer_phone());
            holder.customerName.setText(itemModel.getCustomer_name());
        }

        @Override
        public int getItemCount() {
            return processingItemModels.size();
        }

        private class Holder extends RecyclerView.ViewHolder {

            public TextView serviceName;
            public TextView orderID;
            public TextView price;
            public TextView status;
            public TextView emailID;
            public TextView phoneNumber;
            public TextView customerName;
            public RelativeLayout statusBackground;

            public Holder(@NonNull View itemView) {
                super(itemView);
                serviceName = itemView.findViewById(R.id.serviceName);
                orderID = itemView.findViewById(R.id.orderID);
                price = itemView.findViewById(R.id.price);
                status = itemView.findViewById(R.id.status);
                statusBackground = itemView.findViewById(R.id.statusBackground);
                emailID = itemView.findViewById(R.id.emailID);
                phoneNumber = itemView.findViewById(R.id.phoneNumber);
                customerName = itemView.findViewById(R.id.customerName);
            }
        }
    }
}