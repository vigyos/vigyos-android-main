package com.vigyos.vigyoscentercrm.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Adapter.CustomArrayAdapter;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class BBPSHistoryFragment extends Fragment {

    protected Activity activity;
    private LottieAnimationView animationView;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private String trxType;
    private int page = 1;
    private boolean isLoading = false;
    private Dialog dialog;
    protected List<Map<String, String>> logsList = new ArrayList<>();
    private BbpsHistoryAdapter bbpsHistoryAdapter;
    public GridLayoutManager gridLayoutManager;

    public BBPSHistoryFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bbps_history, container, false);
        Initialization(view);
        Declaration();
        return view;
    }

    private void Initialization(View view) {
        animationView = view.findViewById(R.id.animationView);
        recyclerView = view.findViewById(R.id.bbpsRecyclerView);
        spinner = view.findViewById(R.id.spinner);
        spinner.setBackgroundResource(android.R.color.transparent);
    }

    private void Declaration() {
        CustomArrayAdapter adapter = new CustomArrayAdapter(activity, R.layout.spinner_item, getResources().getStringArray(R.array.bbpsHistoryType));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = (String) parent.getItemAtPosition(position);
                if (type.equalsIgnoreCase("ALL")) {
                    trxType = "";
                    logsList.clear();
                    page = 1;
                } else if (type.equalsIgnoreCase("SUCCESS")) {
                    trxType = "SUCCESS";
                    logsList.clear();
                    page = 1;
                } else if (type.equalsIgnoreCase("FAILED")) {
                    trxType = "FAILED";
                    logsList.clear();
                    page = 1;
                }
                Log.i("8521456" ,"trxType ----- " + trxType);
                bbpsAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void bbpsAdapter() {
        bbpsHistoryAdapter = new BbpsHistoryAdapter(logsList);
        gridLayoutManager = new GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bbpsHistoryAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading && !recyclerView.canScrollVertically(1)) {
                    page++;
                    bbpsHistory(page);
                    isLoading = true;
                }
            }
        });
        bbpsHistory(page);
    }

    private void bbpsHistory(int page) {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().bbpsTransactionLogs(SplashActivity.prefManager.getToken(), SplashActivity.prefManager.getUserID(), page, trxType);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse "+ response);
                dismissDialog();
                isLoading = false;
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        if (jsonObject.has("data")) {
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject operatorObject = dataArray.getJSONObject(i);
                                Map<String, String> transactionMap = new HashMap<>();
                                Iterator<String> keys = operatorObject.keys();
                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    String value = operatorObject.getString(key);
                                    transactionMap.put(key, value);
                                    Log.i("68248+9", "key: " + key + " value: " + value);
                                }
                                logsList.add(transactionMap);
                            }
                            if (logsList.isEmpty()) {
                                animationView.setVisibility(View.VISIBLE);
                            } else {
                                animationView.setVisibility(View.GONE);
                            }
                            bbpsHistoryAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2016", "onFailure "+ t);
                dismissDialog();
                isLoading = false;
            }
        });
    }

    private class  BbpsHistoryAdapter extends RecyclerView.Adapter<BbpsHistoryAdapter.Holder>{

        protected List<Map<String, String>> logsList;

        public BbpsHistoryAdapter(List<Map<String, String>> logsList) {
            this.logsList = logsList;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_bbps_history, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            Map<String, String> mapList = logsList.get(position);
            if (mapList.containsKey("referenceno")) {
                holder.referenceID.setText("#"+ getLastThree(mapList.get("referenceno")));
            }
            if (mapList.containsKey("status")) {
                if (Objects.requireNonNull(mapList.get("status")).equalsIgnoreCase("SUCCESS")) {
                    holder.status.setText("SUCCESS");
                    holder.status.setTextColor(getResources().getColor(R.color.cr));
                } else if (Objects.requireNonNull(mapList.get("status")).equalsIgnoreCase("FAILED")){
                    holder.status.setText("FAILED");
                    holder.status.setTextColor(getResources().getColor(R.color.dr));
                }
            }
            if (mapList.containsKey("canumber")) {
                holder.billNumber.setText(mapList.get("canumber"));
            }
            if (mapList.containsKey("amount")) {
                holder.amount.setText("₹"+mapList.get("amount"));
            }
            if (mapList.containsKey("transactiontype")) {
                holder.transactionType.setText(mapList.get("transactiontype"));
            }
            if (mapList.containsKey("commission")) {
                holder.commissionAmount.setText("₹"+mapList.get("commission"));
            }
            if (mapList.containsKey("timestamp")) {
                String timestampStr = mapList.get("timestamp");
                if (timestampStr != null && timestampStr.matches("\\d+")) {
                    long timestamp = Long.parseLong(timestampStr);
                    holder.dateAndTime.setText(formatTimestamp(timestamp));
                } else {
                    try {
                        if (timestampStr != null) {
                            long timestamp = parseTimestamp(timestampStr);
                            holder.dateAndTime.setText(formatTimestamp(timestamp));
                        } else {
                            holder.dateAndTime.setText("Invalid date format");
                        }
                    } catch (ParseException e) {
                        holder.dateAndTime.setText("Invalid date format");
                        e.printStackTrace();
                    }
                }
            }
            if (mapList.containsKey("user_info")) {
                try {
                    JSONObject object  = new JSONObject(Objects.requireNonNull(mapList.get("user_info")));
                    String firstName = null, lastName = null;
                    if (object.has("first_name")) {
                        firstName = object.getString("first_name");
                    }
                    if (object.has("last_name")) {
                        lastName = object.getString("last_name");
                    }
                    holder.userName.setText(firstName+" "+lastName);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public String getLastThree(String myString) {
            if(myString.length() > 6)
                return myString.substring(myString.length()-6);
            else
                return myString;
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
            return logsList.size();
        }

        private class Holder extends RecyclerView.ViewHolder{
            TextView referenceID, status;
            TextView userName, amount;
            TextView billNumber, transactionType;
            TextView commissionAmount, dateAndTime;
            public Holder(@NonNull View itemView) {
                super(itemView);
                referenceID = itemView.findViewById(R.id.referenceID);
                status = itemView.findViewById(R.id.status);
                userName = itemView.findViewById(R.id.userName);
                amount = itemView.findViewById(R.id.amount);
                billNumber = itemView.findViewById(R.id.billNumber);
                transactionType = itemView.findViewById(R.id.transactionType);
                commissionAmount = itemView.findViewById(R.id.commissionAmount);
                dateAndTime = itemView.findViewById(R.id.dateAndTime);
            }
        }
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
        dismissDialog();  // Make sure to dismiss the dialog
        super.onDestroy();
    }
}