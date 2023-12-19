package com.vigyos.vigyoscentercrm.Activity.Recharge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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

import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Activity.BBPS.BBPSOperatorListActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Model.BbpsPayBillModel;
import com.vigyos.vigyoscentercrm.Model.MobileRechargeOperatorModel;
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
public class MobileRechargeOperatorActivity extends AppCompatActivity {

    private ImageView ivBack;
    private AutoCompleteTextView autoCompleteTextView;
    private RecyclerView recyclerView;
    private OperatorListAdaptor operatorListAdaptor;
    private ArrayList<MobileRechargeOperatorModel> mobileRechargeOperatorModels = new ArrayList<>();
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_recharge_operator);
        Initialization();
        Declaration();
    }

    private void Initialization() {
        ivBack = findViewById(R.id.ivBack);
        autoCompleteTextView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void Declaration() {
        OperatorList();
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
                operatorListAdaptor.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void OperatorList() {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().mobileRechargeOperator(SplashActivity.prefManager.getToken());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse " + response);
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        if (jsonObject.has("data")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i<jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                MobileRechargeOperatorModel rechargeOperatorModel = new MobileRechargeOperatorModel();
                                if (object.has("id")) {
                                    rechargeOperatorModel.setOperatorId(object.getString("id"));
                                }
                                if (object.has("name")) {
                                    rechargeOperatorModel.setOperatorName(object.getString("name"));
                                }
                                if (object.has("category")) {
                                    rechargeOperatorModel.setOperatorCategory(object.getString("category"));
                                }
                                mobileRechargeOperatorModels.add(rechargeOperatorModel);
                            }
                        }
                        AdaptorCall();
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

    private void AdaptorCall() {
        operatorListAdaptor = new OperatorListAdaptor(mobileRechargeOperatorModels);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(operatorListAdaptor);
    }

    private class OperatorListAdaptor extends RecyclerView.Adapter<OperatorListAdaptor.Holder> implements Filterable {

        private ArrayList<MobileRechargeOperatorModel> mobileRechargeOperatorModels;
        private ArrayList<MobileRechargeOperatorModel> searchFilterList;

        @Override
        public Filter getFilter() {
            return filterData;
        }

        private Filter filterData = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<MobileRechargeOperatorModel> filteredList = new ArrayList<>();
                if (charSequence == null || charSequence.length() ==0 ){
                    filteredList.addAll(searchFilterList);
                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();
                    for (MobileRechargeOperatorModel item : searchFilterList){
                        if (item.getOperatorName().toLowerCase().contains(filterPattern)){
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
                mobileRechargeOperatorModels.clear();
                mobileRechargeOperatorModels.addAll((ArrayList) filterResults.values);
                notifyDataSetChanged();
            }
        };

        public OperatorListAdaptor(ArrayList<MobileRechargeOperatorModel> mobileRechargeOperatorModels) {
            this.mobileRechargeOperatorModels = mobileRechargeOperatorModels;
            this.searchFilterList = new ArrayList<>(mobileRechargeOperatorModels);
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
            MobileRechargeOperatorModel operatorModel = mobileRechargeOperatorModels.get(position);
            holder.listName.setText(operatorModel.getOperatorName());
            holder.next_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return mobileRechargeOperatorModels.size();
        }

        private class Holder extends RecyclerView.ViewHolder{

            TextView listName;
            RelativeLayout next_ll;

            public Holder(@NonNull View itemView) {
                super(itemView);
                listName = itemView.findViewById(R.id.listName);
                next_ll = itemView.findViewById(R.id.next_ll);
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