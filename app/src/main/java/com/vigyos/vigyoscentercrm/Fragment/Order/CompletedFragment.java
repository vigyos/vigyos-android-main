package com.vigyos.vigyoscentercrm.Fragment.Order;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.os.BuildCompat;
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
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Activity.AccountActivity;
import com.vigyos.vigyoscentercrm.Activity.LoginActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Activity.WalletActivity;
import com.vigyos.vigyoscentercrm.Model.CompletedItemModel;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class CompletedFragment extends Fragment {

    private View view;
    private Dialog dialog;
    private CompletedListAdapter completedListAdapter;
    private ArrayList<CompletedItemModel> completedItemModels = new ArrayList<>();
    private TextView noComplete;
    private Activity activity;
    private int page = 1;
    private boolean isLoading = false;

    public CompletedFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_completed, container, false);
        noComplete = view.findViewById(R.id.noComplete);
        showCompletedList();
        return view;
    }

    private void showCompletedList(){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        completedListAdapter = new CompletedListAdapter(completedItemModels, activity);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(completedListAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading && !recyclerView.canScrollVertically(1)) {
                    page++;  // Increment the page
                    completedApi(page);
                    isLoading = true;
                }
            }
        });
        completedApi(page);
    }

    private void completedApi(int page){
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().serviceRequest(SplashActivity.prefManager.getToken(), SplashActivity.prefManager.getUserID(), page, "COMPLETE");
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016","onResponse" + response);
                dismissDialog();
                isLoading = false;
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        if (jsonObject.has("data")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                if (jsonObject1.has("status") && jsonObject1.getString("status").equalsIgnoreCase("COMPLETE")){
                                    CompletedItemModel completedItemModel = new CompletedItemModel();
                                    if (jsonObject1.has("service_name")) {
                                        completedItemModel.setService_name(jsonObject1.getString("service_name"));
                                    }
                                    if (jsonObject1.has("status")) {
                                        completedItemModel.setStatus(jsonObject1.getString("status"));
                                    }
                                    if (jsonObject1.has("user_service_id")) {
                                        completedItemModel.setUser_service_id(jsonObject1.getString("user_service_id"));
                                    }
                                    if (jsonObject1.has("customer_name")) {
                                        completedItemModel.setCustomer_name(jsonObject1.getString("customer_name"));
                                    }
                                    if (jsonObject1.has("customer_phone")) {
                                        completedItemModel.setCustomer_phone(jsonObject1.getString("customer_phone"));
                                    }
                                    if (jsonObject1.has("customer_email")) {
                                        completedItemModel.setCustomer_email(jsonObject1.getString("customer_email"));
                                    }
                                    if (jsonObject1.has("price")) {
                                        completedItemModel.setPrice(jsonObject1.getInt("price"));
                                    }
                                    if (jsonObject1.has("created_time")) {
                                        completedItemModel.setCreated_time(jsonObject1.getString("created_time"));
                                    }
                                    completedItemModels.add(completedItemModel);
                                }
                            }
                            if(completedItemModels.isEmpty()){
                                noComplete.setVisibility(View.VISIBLE);
                            } else {
                                noComplete.setVisibility(View.GONE);
                            }
                            completedListAdapter.notifyDataSetChanged();
                        }
                    } else {
                        if (jsonObject.has("message")) {
                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            SplashActivity.prefManager.setClear();
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            getActivity().finish();
                        }
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
                Toast.makeText(activity, "Maintenance underway. We'll be back soon.", Toast.LENGTH_SHORT).show();
            }
        });
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

    private class CompletedListAdapter extends RecyclerView.Adapter<CompletedListAdapter.Holder> {

        private ArrayList<CompletedItemModel> completedItemModels;
        private Activity activity;

        public CompletedListAdapter(ArrayList<CompletedItemModel> completedItemModels, Activity activity) {
            this.completedItemModels = completedItemModels;
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
            CompletedItemModel itemModel = completedItemModels.get(position);
            holder.serviceName.setText(itemModel.getService_name());
            holder.orderID.setText("#"+itemModel.getUser_service_id());
            holder.price.setText("₹"+ itemModel.getPrice());
            holder.status.setText(itemModel.getStatus());
            holder.status.setTextColor(getResources().getColor(R.color.completed));
            holder.phoneNumber.setText("+91-"+itemModel.getCustomer_phone());
            holder.customerName.setText(itemModel.getCustomer_name());

            String timestampStr = itemModel.getCreated_time();
            if (timestampStr != null && timestampStr.matches("\\d+")) {
                long timestamp = Long.parseLong(timestampStr);
                holder.date.setText(formatTimestamp(timestamp));
            } else {
                try {
                    if (timestampStr != null) {
                        long timestamp = parseTimestamp(timestampStr);
                        holder.date.setText(formatTimestamp(timestamp));
                    } else {
                        holder.date.setText("Invalid date format");
                    }
                } catch (ParseException e) {
                    holder.date.setText("Invalid date format");
                    e.printStackTrace();
                }
            }
        }

        private String formatTimestamp(long timestamp) {
            Date date = new Date(timestamp * 1000L);
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a, dd MMM yyyy", Locale.getDefault());
            return formatter.format(date);
        }

        private long parseTimestamp(String timestampString) throws ParseException {
            SimpleDateFormat parser = new SimpleDateFormat("hh:mm:ss a, dd MMM yyyy", Locale.getDefault());
            Date date = parser.parse(timestampString);
            return date.getTime() / 1000;
        }

        @Override
        public int getItemCount() {
            return completedItemModels.size();
        }

        private class Holder extends RecyclerView.ViewHolder {

            public TextView serviceName;
            public TextView orderID;
            public TextView price;
            public TextView status;
            public TextView phoneNumber;
            public TextView customerName;
            public TextView date;

            public Holder(@NonNull View itemView) {
                super(itemView);
                serviceName = itemView.findViewById(R.id.serviceName);
                orderID = itemView.findViewById(R.id.orderID);
                price = itemView.findViewById(R.id.price);
                status = itemView.findViewById(R.id.status);
                phoneNumber = itemView.findViewById(R.id.phoneNumber);
                customerName = itemView.findViewById(R.id.Name);
                date = itemView.findViewById(R.id.date);
            }
        }
    }
}