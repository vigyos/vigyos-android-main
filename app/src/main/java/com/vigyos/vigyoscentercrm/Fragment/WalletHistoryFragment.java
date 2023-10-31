package com.vigyos.vigyoscentercrm.Fragment;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Activity.LoginActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Model.WalletHistoryModel;
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
public class WalletHistoryFragment extends Fragment {

    private Activity activity;
    private Dialog dialog;
    private Spinner spinner;
    private String trxType = "ALL";
    private LottieAnimationView animationView;
    private ArrayList<WalletHistoryModel> walletHistoryModel = new ArrayList<>();
    private RecyclerView recyclerView;
    private WalletHistoryAdapter walletHistoryAdapter;
    private int page = 1;
    private boolean isLoading = false;

    public WalletHistoryFragment(Activity activity) {
        // Required empty public constructor
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet_history, container, false);
        initialization(view);
        declaration();
        return view;
    }

    private void initialization(View view) {
        animationView = view.findViewById(R.id.animationView);
        recyclerView = view.findViewById(R.id.walletRecyclerView);
        spinner = view.findViewById(R.id.spinner);
        spinner.setBackgroundResource(android.R.color.transparent);
    }

    private void declaration() {
        ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter<>(activity, R.layout.spinner_item, android.R.id.text1, getResources().getStringArray(R.array.walletHistoryType));
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = (String) parent.getItemAtPosition(position);
                if (type.equalsIgnoreCase("ALL")) {
                    trxType = "ALL";
                    walletHistoryModel.clear();
                    page = 1;
                } else if (type.equalsIgnoreCase("CREDIT")) {
                    trxType = "CREDIT";
                    walletHistoryModel.clear();
                    page = 1;
                } else {
                    trxType = "DEBIT";
                    walletHistoryModel.clear();
                    page = 1;
                }
                Log.i("2014855" ,"trxType " + trxType);
                walletAdapter();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void walletAdapter() {
        walletHistoryAdapter = new WalletHistoryAdapter(walletHistoryModel, activity);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 1 , GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(walletHistoryAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading && !recyclerView.canScrollVertically(1)) {
                    page++;
                    walletHistoryAPI(page);
                    isLoading = true;
                }
            }
        });
        walletHistoryAPI(page);
    }

    private void walletHistoryAPI(int page) {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().walletHistory(SplashActivity.prefManager.getToken(), SplashActivity.prefManager.getUserID(), page, trxType);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016","onResponse " + response);
                dismissDialog();
                isLoading = false;
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    Log.i("2016", "JSON Response: " + jsonObject); // Added this line for logging
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            WalletHistoryModel historyModel = new WalletHistoryModel();
                            if (jsonObject1.has("trx_id")) {
                                historyModel.setTrx_id(jsonObject1.getString("trx_id"));
                            }
                            if (jsonObject1.has("user_id")) {
                                historyModel.setUser_id(jsonObject1.getString("user_id"));
                            }
                            if (jsonObject1.has("trx_amount")) {
                                historyModel.setTrx_amount(jsonObject1.getInt("trx_amount"));
                            }
                            if (jsonObject1.has("trx_status")) {
                                historyModel.setTrx_status(jsonObject1.getString("trx_status"));
                            }
                            if (jsonObject1.has("trx_type")) {
                                historyModel.setTrx_type(jsonObject1.getString("trx_type"));
                            }
                            if (jsonObject1.has("description")) {
                                historyModel.setDescription(jsonObject1.getString("description"));
                            }
//                            if (jsonObject1.has("entity_id")) {
//                                historyModel.setEntity_id(jsonObject1.getInt("entity_id"));
//                            }
                            if (jsonObject1.has("entity_type")) {
                                historyModel.setEntity_type(jsonObject1.getString("entity_type"));
                            }
                            if (jsonObject1.has("amount_type")) {
                                historyModel.setAmount_type(jsonObject1.getString("amount_type"));
                            }
                            if (jsonObject1.has("timestamp")) {
                                historyModel.setTimestamp(jsonObject1.getString("timestamp"));
                            }
                            if (jsonObject1.has("proof")) {
                                historyModel.setProof(jsonObject1.getString("proof"));
                            }
                            if (jsonObject1.has("wallet_id")) {
                                historyModel.setWallet_id(jsonObject1.getString("wallet_id"));
                            }
                            if (jsonObject1.has("created_at")) {
                                historyModel.setCreated_at(jsonObject1.getString("created_at"));
                            }
                            if (jsonObject1.has("record_count")) {
                                historyModel.setRecord_count(jsonObject1.getInt("record_count"));
                            }
                            if (jsonObject1.has("first_name")) {
                                historyModel.setFirst_name(jsonObject1.getString("first_name"));
                            }
                            if (jsonObject1.has("last_name")) {
                                historyModel.setLast_name(jsonObject1.getString("last_name"));
                            }
                            if (jsonObject1.has("updated_amount")) {
                                historyModel.setUpdated_amount(jsonObject1.getInt("updated_amount"));
                            }
                            walletHistoryModel.add(historyModel);
                        }
                        if (walletHistoryModel.isEmpty()) {
                            animationView.setVisibility(View.VISIBLE);
                        } else {
                            animationView.setVisibility(View.GONE);
                        }
                        walletHistoryAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(activity, "Your session has expired. Please log in again to continue.", Toast.LENGTH_SHORT).show();
                        SplashActivity.prefManager.setClear();
                        startActivity(new Intent(activity, LoginActivity.class));
                        activity.finish();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2016","onFailure " + t);
                dismissDialog();
                isLoading = false;
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

    private class WalletHistoryAdapter extends RecyclerView.Adapter<WalletHistoryAdapter.Holder> {

        private ArrayList<WalletHistoryModel> walletHistoryModels;
        private Activity activity;

        public WalletHistoryAdapter(ArrayList<WalletHistoryModel> walletHistoryModels, Activity activity) {
            this.walletHistoryModels = walletHistoryModels;
            this.activity = activity;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_wallet_history, parent, false);
            return new Holder(view);
        }

        @Override
        public int getItemCount() {
            return walletHistoryModels.size();
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            WalletHistoryModel model = walletHistoryModels.get(position);
            holder.titleName.setText(model.getDescription());
            holder.orderID.setText("#"+ getLastThree(model.getTrx_id()));

            if (model.getUpdated_amount() == 0){
                holder.balance.setText("Balance: ₹ "+"0.00");
            } else {
                int i = model.getUpdated_amount();
                float v = (float) i;
                holder.balance.setText("Balance: ₹ "+ v);
            }

            String timestampStr = model.getTimestamp();
            if (timestampStr.matches("\\d+")) {
                long timestamp = Long.parseLong(timestampStr);
                holder.date.setText(formatTimestamp(timestamp));
            } else {
                try {
                    long timestamp = parseTimestamp(timestampStr);
                    holder.date.setText(formatTimestamp(timestamp));
                } catch (ParseException e) {
                    holder.date.setText("null");
                    e.printStackTrace();
                }
            }

            if (model.getTrx_type().equalsIgnoreCase("CREDIT")) {
                holder.amount.setTextColor(getResources().getColor(R.color.cr));
                holder.amount.setText("+ ₹"+model.getTrx_amount());
            } else {
                holder.amount.setTextColor(getResources().getColor(R.color.dr));
                holder.amount.setText("- ₹"+model.getTrx_amount());
            }
        }

        public String getLastThree(String myString) {
            if(myString.length() > 6)
                return myString.substring(myString.length()-6);
            else
                return myString;
        }

        private String formatTimestamp(long timestamp) {
            Date date = new Date(timestamp * 1000L); // Convert seconds to milliseconds
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy, hh:mm a", Locale.getDefault()); // Set your desired format
            return sdf.format(date);
        }

        private long parseTimestamp(String timestampString) throws ParseException {
            SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ssa");
            Date date = parser.parse(timestampString);
            return date.getTime() / 1000;
        }

        private class Holder extends RecyclerView.ViewHolder{

            TextView titleName;
            TextView date;
            TextView amount;
            TextView balance;
            TextView orderID;

            public Holder(@NonNull View itemView) {
                super(itemView);

                titleName = itemView.findViewById(R.id.titleName);
                date = itemView.findViewById(R.id.date);
                amount = itemView.findViewById(R.id.amount);
                balance = itemView.findViewById(R.id.balance);
                orderID = itemView.findViewById(R.id.orderID);
            }
        }
    }
}