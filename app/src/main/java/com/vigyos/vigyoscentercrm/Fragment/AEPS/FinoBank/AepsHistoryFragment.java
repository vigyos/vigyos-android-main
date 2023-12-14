package com.vigyos.vigyoscentercrm.Fragment.AEPS.FinoBank;

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
import com.vigyos.vigyoscentercrm.Adapter.CustomArrayAdapter;
import com.vigyos.vigyoscentercrm.Model.AEPSHistoryModel;
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
public class AepsHistoryFragment extends Fragment {

    private Activity activity;
    private Dialog dialog;
    private Spinner spinner;
    private String trxType ;
    private LottieAnimationView animationView;
    private ArrayList<AEPSHistoryModel> aepsHistoryModels = new ArrayList<>();
    private RecyclerView recyclerView;
    private AEPSHistoryAdapter AEPSHistoryAdapter;
    private int page = 1;
    private boolean isLoading = false;

    public AepsHistoryFragment(Activity activity) {
        // Required empty public constructor
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_aeps_history, container, false);
        initialization(view);
        declaration();
        return view;
    }

    private void initialization(View view) {
        animationView = view.findViewById(R.id.animationView);
        recyclerView = view.findViewById(R.id.aepsRecyclerView);
        spinner = view.findViewById(R.id.spinner);
        spinner.setBackgroundResource(android.R.color.transparent);
    }

    private void declaration() {
        CustomArrayAdapter adapter = new CustomArrayAdapter(activity, R.layout.spinner_item, getResources().getStringArray(R.array.aepsHistoryType));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = (String) parent.getItemAtPosition(position);
                if (type.equalsIgnoreCase("ALL")) {
                    trxType = "ALL";
                    aepsHistoryModels.clear();
                    page = 1;
                } else if (type.equalsIgnoreCase("Cash Withdrawal")) {
                    trxType = "CW";
                    aepsHistoryModels.clear();
                    page = 1;
                } else if (type.equalsIgnoreCase("Balance Enquiry")) {
                    trxType = "BE";
                    aepsHistoryModels.clear();
                    page = 1;
                } else if (type.equalsIgnoreCase("Aadhaar Pay")) {
                    trxType = "FM";
                    aepsHistoryModels.clear();
                    page = 1;
                } else {
                    trxType = "MS";
                    aepsHistoryModels.clear();
                    page = 1;
                }
                Log.i("8521456" ,"trxType ----- " + trxType);
                aepsAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void aepsAdapter() {
        AEPSHistoryAdapter = new AEPSHistoryAdapter(aepsHistoryModels, activity);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 1 , GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(AEPSHistoryAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading && !recyclerView.canScrollVertically(1)) {
                    page++;
                    aepsHistoryApi(page);
                    isLoading = true;
                }
            }
        });
        aepsHistoryApi(page);
    }

    private void aepsHistoryApi(int page) {
        pleaseWait();

        Log.i("8521456","aepsHistoryApi  " + trxType);

        Call<Object> objectCall = RetrofitClient.getApi().aepsHistory(SplashActivity.prefManager.getToken(), SplashActivity.prefManager.getUserID(), page, trxType);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("20160","onResponse " + response);
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        if (jsonObject.has("data")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i<jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                AEPSHistoryModel historyModel = new AEPSHistoryModel();
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
                                    historyModel.setAmount(jsonObject1.getInt("amount"));
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
                                aepsHistoryModels.add(historyModel);
                            }
                            if (aepsHistoryModels.isEmpty()) {
                                animationView.setVisibility(View.VISIBLE);
                            } else {
                                animationView.setVisibility(View.GONE);
                            }
                            AEPSHistoryAdapter.notifyDataSetChanged();
                        }
                    } else {
                        if (jsonObject.has("message")) {
                            Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            SplashActivity.prefManager.setClear();
                            startActivity(new Intent(activity, LoginActivity.class));
                            activity.finish();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                isLoading = false;
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("20160","onFailure " + t);
                dismissDialog();
                isLoading = false;
                Toast.makeText(activity, "Maintenance underway. We'll be back soon.", Toast.LENGTH_SHORT).show();
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

    private class AEPSHistoryAdapter extends RecyclerView.Adapter<AEPSHistoryAdapter.Holder> {

        private ArrayList<AEPSHistoryModel> aepsHistoryModels;
        private Activity activity;

        public AEPSHistoryAdapter(ArrayList<AEPSHistoryModel> aepsHistoryModels, Activity activity) {
            this.aepsHistoryModels = aepsHistoryModels;
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
            return aepsHistoryModels.size();
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            AEPSHistoryModel model = aepsHistoryModels.get(position);
            holder.orderID.setText("#"+ getLastThree( model.getReferenceno()));
            holder.titleName.setText("Aadhaar: "+model.getAdhaarnumber());
            holder.date.setText(model.getTimestamp());

            if (model.getAmount() == 0){
                holder.amount.setText("₹0.00");
            } else {
                int i = model.getAmount();
                float v = (float) i;
                holder.amount.setText("₹"+ v);
            }

            if (model.getTransactiontype().equalsIgnoreCase("CW")) {
                holder.type.setText("Cash Withdrawal");
            } else if (model.getTransactiontype().equalsIgnoreCase("MS")) {
                holder.type.setText("Mini Statement");
                holder.amount.setText("");
            } else if (model.getTransactiontype().equalsIgnoreCase("BE")) {
                holder.type.setText("Balance Enquiry");
                holder.amount.setText("");
            } else if (model.getTransactiontype().equalsIgnoreCase("FM")){
                holder.type.setText("Aadhaar Pay");
            } else {
                holder.type.setText(model.getTransactiontype());
            }

//            String timestampStr = model.getTimestamp();
//            if (timestampStr != null && timestampStr.matches("\\d+")) {
//                long timestamp = Long.parseLong(timestampStr);
//                holder.date.setText(formatTimestamp(timestamp));
//            } else {
//                try {
//                    if (timestampStr != null) {
//                        long timestamp = parseTimestamp(timestampStr);
//                        holder.date.setText(formatTimestamp(timestamp));
//                    } else {
//                        holder.date.setText("Invalid date format");
//                    }
//                } catch (ParseException e) {
//                    holder.date.setText("Invalid date format");
//                    e.printStackTrace();
//                }
//            }
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

        private class Holder extends RecyclerView.ViewHolder{

            TextView titleName;
            TextView date;
            TextView amount;
            TextView orderID;
            TextView type;

            public Holder(@NonNull View itemView) {
                super(itemView);
                titleName = itemView.findViewById(R.id.titleName);
                date = itemView.findViewById(R.id.date);
                amount = itemView.findViewById(R.id.amount);
                orderID = itemView.findViewById(R.id.orderID);
                type = itemView.findViewById(R.id.type);
            }
        }
    }
}