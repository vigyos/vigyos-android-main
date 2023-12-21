package com.vigyos.vigyoscentercrm.Activity.BBPS;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Activity.MainActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Constant.DialogCustom;
import com.vigyos.vigyoscentercrm.Model.BbpsPayBillModel;
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
public class BBPSOperatorListActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView titleNameText;
    private Dialog dialog;
    private PayBillAdapter payBillAdapter;
    private ArrayList<BbpsPayBillModel> bbpsPayBillModels = new ArrayList<>();
    private RecyclerView recyclerView;
    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbps_pay_bills);
        Initialization();
        Declaration();
    }

    private void Initialization() {
        ivBack = findViewById(R.id.ivBack);
        titleNameText = findViewById(R.id.titleName);
        recyclerView = findViewById(R.id.recyclerView);
        autoCompleteTextView = findViewById(R.id.searchView);
    }

    private void Declaration() {
        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("categoryData");
        String titleName = intent.getStringExtra("titleName");
        titleNameText.setText(categoryName);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                payBillAdapter.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        api(categoryName);
    }

    private void api(String desiredCategory) {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().payBillOperator(SplashActivity.prefManager.getToken(), "online");
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse " + response);
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        if (jsonObject.has("data")) {
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            if (dataObject.has(desiredCategory)) {
                                JSONObject categoryObject = dataObject.getJSONObject(desiredCategory);
                                if (categoryObject.has("icon")) {
                                    String iconUrl = categoryObject.getString("icon");
                                }
                                if (categoryObject.has("data")) {
                                    JSONArray dataArray = categoryObject.getJSONArray("data");
                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject itemObject = dataArray.getJSONObject(i);
                                        BbpsPayBillModel payBillModel = new BbpsPayBillModel();
                                        if (itemObject.has("id")) {
                                            payBillModel.setId(itemObject.getString("id"));
                                        }
                                        if (itemObject.has("name")) {
                                            payBillModel.setName(itemObject.getString("name"));
                                        }
                                        if (itemObject.has("category")) {
                                            payBillModel.setCategory(itemObject.getString("category"));
                                        }
                                        if (itemObject.has("viewbill")) {
                                            payBillModel.setViewbill(itemObject.getString("viewbill"));
                                        }
                                        if (itemObject.has("regex")) {
                                            payBillModel.setRegex(itemObject.optString("regex", ""));
                                        }
                                        if (itemObject.has("displayname")) {
                                            payBillModel.setDisplayname(itemObject.getString("displayname"));
                                        }
                                        if (itemObject.has("ad1_d_name")) {
                                            payBillModel.setAd1_d_name(itemObject.getString("ad1_d_name"));
                                        }
                                        if (itemObject.has("ad1_name")) {
                                            payBillModel.setAd1_name(itemObject.getString("ad1_name"));
                                        }
                                        if (itemObject.has("ad1_regex")) {
                                            payBillModel.setAd1_regex(itemObject.getString("ad1_regex"));
                                        }
                                        if (itemObject.has("ad2_d_name")) {
                                            payBillModel.setAd2_d_name(itemObject.getString("ad2_d_name"));
                                        }
                                        if (itemObject.has("ad2_name")) {
                                            payBillModel.setAd2_name(itemObject.getString("ad2_name"));
                                        }
                                        if (itemObject.has("ad2_regex")) {
                                            payBillModel.setAd2_regex(itemObject.getString("ad2_regex"));
                                        }
                                        if (itemObject.has("ad3_d_name")) {
                                            payBillModel.setAd3_d_name(itemObject.getString("ad3_d_name"));
                                        }
                                        if (itemObject.has("ad3_name")) {
                                            payBillModel.setAd3_name(itemObject.getString("ad3_name"));
                                        }
                                        if (itemObject.has("ad3_regex")) {
                                            payBillModel.setAd3_regex(itemObject.getString("ad3_regex"));
                                        }
                                        bbpsPayBillModels.add(payBillModel);
                                        Log.i("2019", "Category: " + desiredCategory);
                                    }
                                }
                            } else {
                                Log.i("2019", "Desired category not found in the response");
                            }
                        }
                        callAdapter();
                    } else {
                        if (jsonObject.has("message")) {
                            DialogCustom.showAlertDialog(BBPSOperatorListActivity.this, "Alert!", jsonObject.getString("message"), "OK",true, () -> {});
                        } else {
                            DialogCustom.showAlertDialog(BBPSOperatorListActivity.this, "Alert!", "Maintenance underway. We'll be back soon. Try after sometime...", "OKAY",true, () -> {
                                startActivity(new Intent(BBPSOperatorListActivity.this, MainActivity.class));
                                finish();
                            });
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2016", "onFailure " + t);
                dismissDialog();
            }
        });
    }

    private void callAdapter() {
        payBillAdapter = new PayBillAdapter(bbpsPayBillModels);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(payBillAdapter);
    }

    private class PayBillAdapter extends RecyclerView.Adapter<PayBillAdapter.Holder> implements Filterable {

        public ArrayList<BbpsPayBillModel> bbpsPayBillModels;
        private ArrayList<BbpsPayBillModel> searchFilterList;

        @Override
        public Filter getFilter() {
            return filterData;
        }

        private Filter filterData = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<BbpsPayBillModel> filteredList = new ArrayList<>();
                if (charSequence == null || charSequence.length() ==0 ){
                    filteredList.addAll(searchFilterList);
                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();
                    for (BbpsPayBillModel item : searchFilterList){
                        if (item.getName().toLowerCase().contains(filterPattern)){
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                bbpsPayBillModels.clear();
                bbpsPayBillModels.addAll((ArrayList) filterResults.values);
                notifyDataSetChanged();
            }
        };

        public PayBillAdapter(ArrayList<BbpsPayBillModel> bbpsPayBillModels) {
            this.bbpsPayBillModels = bbpsPayBillModels;
            this.searchFilterList = new ArrayList<>(bbpsPayBillModels);
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_search_services_item, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            BbpsPayBillModel billModel = bbpsPayBillModels.get(position);
            holder.listName.setText(billModel.getName());
            holder.next_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BBPSOperatorListActivity.this, BBPSPayBillsActivity.class);
                    intent.putExtra("id", billModel.getId());
                    intent.putExtra("name", billModel.getName());
                    intent.putExtra("category", billModel.getCategory());
                    intent.putExtra("viewbill", billModel.getViewbill());
                    intent.putExtra("regex", billModel.getRegex());
                    intent.putExtra("displayname", billModel.getDisplayname());
                    intent.putExtra("ad1_d_name", billModel.getAd1_d_name());
                    intent.putExtra("ad1_name", billModel.getAd1_name());
                    intent.putExtra("ad1_regex", billModel.getAd1_regex());
                    intent.putExtra("ad2_d_name", billModel.getAd2_d_name());
                    intent.putExtra("ad2_name", billModel.getAd2_name());
                    intent.putExtra("ad2_regex", billModel.getAd2_regex());
                    intent.putExtra("ad3_d_name", billModel.getAd3_d_name());
                    intent.putExtra("ad3_name", billModel.getAd3_name());
                    intent.putExtra("ad3_regex", billModel.getAd3_regex());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return bbpsPayBillModels.size();
        }

        private class Holder extends RecyclerView.ViewHolder{

            RelativeLayout next_ll;
            TextView listName;

            public Holder(@NonNull View itemView) {
                super(itemView);
                next_ll = itemView.findViewById(R.id.next_ll);
                listName = itemView.findViewById(R.id.listName);
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