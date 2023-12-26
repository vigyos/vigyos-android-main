package com.vigyos.vigyoscentercrm.Fragment.Order;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Activity.LoginActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Model.CancelledItemModel;
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
public class CancelledFragment extends Fragment {

    private View view;
    private Dialog dialog;
    private CancelledListAdapter cancelledListAdapter;
    private ArrayList<CancelledItemModel> cancelledItemModels = new ArrayList<>();
    private TextView noCancelled;
    private final Activity activity;
    private int page = 1;
    private boolean isLoading = false;

    public CancelledFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cancelled, container, false);
        noCancelled = view.findViewById(R.id.noCancelled);
        showCancelledList();
        return view;
    }

    private void showCancelledList(){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        cancelledListAdapter = new CancelledListAdapter(cancelledItemModels, activity);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cancelledListAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading && !recyclerView.canScrollVertically(1)) {
                    page++;
                    cancelledApi(page);
                    isLoading = true;
                    Log.i("85214521","cancel 1 ");
                }
            }
        });
        Log.i("85214521","cancel 2");
        cancelledApi(page);
    }

    private void cancelledApi(int page){
        pleaseWait();
        Log.i("85214521","cancel 3");
        Call<Object> objectCall = RetrofitClient.getApi().serviceRequest(SplashActivity.prefManager.getToken(), SplashActivity.prefManager.getUserID(), page, "REJECTED");
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
                                if (jsonObject1.has("status") && jsonObject1.getString("status").equalsIgnoreCase("REJECTED") ){
                                    CancelledItemModel cancelledItemModel = new CancelledItemModel();
                                    if (jsonObject1.has("service_name")) {
                                        cancelledItemModel.setService_name(jsonObject1.getString("service_name"));
                                    }
                                    if (jsonObject1.has("status")) {
                                        cancelledItemModel.setStatus(jsonObject1.getString("status"));
                                    }
                                    if (jsonObject1.has("user_service_id")) {
                                        cancelledItemModel.setUser_service_id(jsonObject1.getString("user_service_id"));
                                    }
                                    if (jsonObject1.has("customer_name")) {
                                        cancelledItemModel.setCustomer_name(jsonObject1.getString("customer_name"));
                                    }
                                    if (jsonObject1.has("customer_phone")) {
                                        cancelledItemModel.setCustomer_phone(jsonObject1.getString("customer_phone"));
                                    }
                                    if (jsonObject1.has("customer_email")) {
                                        cancelledItemModel.setCustomer_email(jsonObject1.getString("customer_email"));
                                    }
                                    if (jsonObject1.has("price")) {
                                        cancelledItemModel.setPrice(jsonObject1.getInt("price"));
                                    }
                                    if (jsonObject1.has("created_time")) {
                                        cancelledItemModel.setCreated_time(jsonObject1.getString("created_time"));
                                    }
                                    cancelledItemModels.add(cancelledItemModel);
                                }
                            }
                            if(cancelledItemModels.isEmpty()){
                                noCancelled.setVisibility(View.VISIBLE);
                            } else {
                                noCancelled.setVisibility(View.GONE);
                            }
                            cancelledListAdapter.notifyDataSetChanged();
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
            holder.orderID.setText("#"+itemModel.getUser_service_id());
            holder.price.setText("₹"+ itemModel.getPrice());
            holder.status.setText(itemModel.getStatus());
            holder.status.setTextColor(getResources().getColor(R.color.rejected));
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
            return cancelledItemModels.size();
        }

        private class Holder extends RecyclerView.ViewHolder {

            public TextView serviceName;
            public TextView orderID;
            public TextView price;
            public TextView status;
            public TextView customerName;
            public TextView phoneNumber;
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