package com.vigyos.vigyoscentercrm.Fragment;

import android.app.Activity;
import android.app.Dialog;
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

import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Model.CompletedItemModel;
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

public class ProcessingFragment extends Fragment {

    private View view;
    private Dialog dialog;
    private ArrayList<ProcessingItemModel> processingItemModels = new ArrayList<>();
    private TextView noPending;

    public ProcessingFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_processing, container, false);
        noPending = view.findViewById(R.id.noPending);
        processingApi();
        return view;
    }

    private void processingApi(){
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().serviceRequest(SplashActivity.prefManager.getToken(), SplashActivity.prefManager.getUserID());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                dismissDialog();
                Log.i("2016","onResponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success")){
                        if (jsonObject.getString("success").equalsIgnoreCase("true")){
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                if (jsonObject1.getString("status").equalsIgnoreCase("PENDING")){
                                    ProcessingItemModel processingItemModel = new ProcessingItemModel();
                                    processingItemModel.setService_name(jsonObject1.getString("service_name"));
                                    processingItemModel.setStatus(jsonObject1.getString("status"));
                                    processingItemModel.setUser_service_id(jsonObject1.getString("user_service_id"));
                                    processingItemModel.setCustomer_name(jsonObject1.getString("customer_name"));
                                    processingItemModel.setCustomer_phone(jsonObject1.getString("customer_phone"));
                                    processingItemModel.setPrice(jsonObject1.getInt("price"));
                                    processingItemModel.setCreated_time(jsonObject1.getString("created_time"));
                                    processingItemModels.add(processingItemModel);
                                }
                            }
                            if(processingItemModels.isEmpty()){
                                noPending.setVisibility(View.VISIBLE);
                            } else {
                                noPending.setVisibility(View.GONE);
                            }
                            showPendingList();
                        }
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

    private void showPendingList(){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        PendingListAdapter completedListAdapter = new PendingListAdapter(processingItemModels, getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(completedListAdapter);
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
        public PendingListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.layout_show_order_item, parent,false);
            return new PendingListAdapter.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            ProcessingItemModel itemModel = processingItemModels.get(position);
            holder.serviceName.setText(itemModel.getService_name());
            holder.orderID.setText("#Oreder: "+itemModel.getUser_service_id());
            holder.price.setText("₹"+ itemModel.getPrice());
            holder.status.setText(itemModel.getStatus());
            holder.statusBackground.setBackgroundResource(R.drawable.pending);
        }

        @Override
        public int getItemCount() {
            return processingItemModels.size();
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