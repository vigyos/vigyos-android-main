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
import android.widget.Spinner;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Model.AEPSHistoryModel;
import com.vigyos.vigyoscentercrm.Model.PayoutHistoryModel;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayoutHistoryFragment extends Fragment {

    private Activity activity;
    private Dialog dialog;
    private Spinner spinner;
    private LottieAnimationView animationView;
    private ArrayList<PayoutHistoryModel> payoutHistoryModels = new ArrayList<>();
    private RecyclerView recyclerView;
    private PayoutHistoryAdapter payoutHistoryAdapter;
    private int page = 1;
    private boolean isLoading = false;

    public PayoutHistoryFragment(Activity activity) {
        // Required empty public constructor
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payout_history, container, false);
        initialization(view);
        declaration();
        return view;
    }

    private void declaration() {
        aepsAdapter();
    }

    private void initialization(View view) {
        animationView = view.findViewById(R.id.animationView);
        recyclerView = view.findViewById(R.id.payoutRecyclerView);
        spinner = view.findViewById(R.id.spinner);
        spinner.setBackgroundResource(android.R.color.transparent);
    }

    private void aepsAdapter() {
        payoutHistoryAdapter = new PayoutHistoryAdapter(payoutHistoryModels, activity);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 1 , GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(payoutHistoryAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading && !recyclerView.canScrollVertically(1)) {
                    aepsHistoryApi(page++);
                    isLoading = true;
                }
            }
        });
        aepsHistoryApi(page);
    }

    private void aepsHistoryApi(int page) {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().aepsHistory(SplashActivity.prefManager.getToken(), SplashActivity.prefManager.getUserID(), page, "PAYOUT");
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("20160","onResponse " + response);
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            PayoutHistoryModel historyModel = new PayoutHistoryModel();
                            if (jsonObject1.has("user_id")) {
                                historyModel.setUser_id(jsonObject1.getString("user_id"));
                            }
                            if (jsonObject1.has("commission")) {
                                historyModel.setCommission(jsonObject1.getString("commission"));
                            }
                            if (jsonObject1.has("referenceno")) {
                                historyModel.setReferenceno(jsonObject1.getString("referenceno"));
                            }
                            if (jsonObject1.has("transactiontype")) {
                                historyModel.setTransactiontype(jsonObject1.getString("transactiontype"));
                            }
                            if (jsonObject1.has("amount")) {
                                historyModel.setAmount(jsonObject1.getString("amount"));
                            }
                            if (jsonObject1.has("timestamp")) {
                                historyModel.setTimestamp(jsonObject1.getString("timestamp"));
                            }
                            if (jsonObject1.has("bene_id")) {
                                historyModel.setBene_id(jsonObject1.getString("bene_id"));
                            }
                            if (jsonObject1.has("adhaarnumber")) {
                                historyModel.setAdhaarnumber(jsonObject1.getString("adhaarnumber"));
                            }
                            if (jsonObject1.has("ackno")) {
                                historyModel.setAckno(jsonObject1.getString("ackno"));
                            }
                            payoutHistoryModels.add(historyModel);
                        }
                        if (payoutHistoryModels.isEmpty()) {
                            animationView.setVisibility(View.VISIBLE);
                        } else {
                            animationView.setVisibility(View.GONE);
                        }
                        payoutHistoryAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("20160","onFailure " + t);
                dismissDialog();
            }
        });
    }

    private void pleaseWait() {
        dialog = new Dialog(activity);
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
    public void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }

    private class PayoutHistoryAdapter extends RecyclerView.Adapter<PayoutHistoryAdapter.Holder> {

        private ArrayList<PayoutHistoryModel> payoutHistoryModels;
        private Activity activity;

        public PayoutHistoryAdapter(ArrayList<PayoutHistoryModel> payoutHistoryModels, Activity activity) {
            this.payoutHistoryModels = payoutHistoryModels;
            this.activity = activity;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_aeps_history, parent, false);
            return new Holder(view);
        }

        @Override
        public int getItemCount() {
            return payoutHistoryModels.size();
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            PayoutHistoryModel model = payoutHistoryModels.get(position);
            holder.titleName.setText(model.getReferenceno());

            holder.date.setText(model.getTimestamp());
            holder.amount.setText(model.getTransactiontype());

//            String timestampStr = model.getTimestamp();
//            if (timestampStr.matches("\\d+")) {
//                long timestamp = Long.parseLong(timestampStr);
//                holder.date.setText(formatTimestamp(timestamp));
//            } else {
//                try {
//                    long timestamp = parseTimestamp(timestampStr);
//                    holder.date.setText(formatTimestamp(timestamp));
//                } catch (ParseException e) {
//                    holder.date.setText("Invalid date format");
//                    e.printStackTrace();
//                }
//            }

//            if (model.getTrx_type().equalsIgnoreCase("CREDIT")) {
//                holder.amount.setTextColor(getResources().getColor(R.color.cr));
//                holder.amount.setText("+ ₹"+model.getTrx_amount());
//            } else {
//                holder.amount.setTextColor(getResources().getColor(R.color.dr));
//                holder.amount.setText("- ₹"+model.getTrx_amount());
//            }
        }

        private String formatTimestamp(long timestamp) {
            Date date = new Date(timestamp * 1000L);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            return formatter.format(date);
        }

        private long parseTimestamp(String timestampString) throws ParseException {
            SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
            Date date = parser.parse(timestampString);
            return date.getTime() / 1000;
        }

        private class Holder extends RecyclerView.ViewHolder{

            TextView titleName;
            TextView date;
            TextView amount;

            public Holder(@NonNull View itemView) {
                super(itemView);

                titleName = itemView.findViewById(R.id.titleName);
                date = itemView.findViewById(R.id.date);
                amount = itemView.findViewById(R.id.amount);
            }
        }
    }
}