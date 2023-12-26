package com.vigyos.vigyoscentercrm.Activity.BBPS.FasTag;

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
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class FasTagOperatorActivity extends AppCompatActivity {

    private ImageView ivBack;
    private AutoCompleteTextView searchView;
    private RecyclerView recyclerView;
    private Dialog dialog;
    private OperatorAdapter operatorAdapter;
    private List<Map<String, String>> originalOperators;
    private List<Map<String, String>> filteredOperators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fas_tag_operator);
        Initialization();
        Declaration();
    }

    private void Initialization() {
        ivBack = findViewById(R.id.ivBack);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void Declaration() {
        fastTag();
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Apply the filter to the adapter
                operatorAdapter.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fastTag() {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().fastTag(SplashActivity.prefManager.getToken());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse " + response);
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("data")) {
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        originalOperators = new ArrayList<>();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject operatorObject = dataArray.getJSONObject(i);
                            Map<String, String> operatorMap = new HashMap<>();
                            Iterator<String> keys = operatorObject.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                String value = operatorObject.getString(key);
                                operatorMap.put(key, value);
                                Log.i("68248+9", "key: " + key + " value: " + value);
                            }
                            originalOperators.add(operatorMap);
                        }
                        // Initialize the filteredOperators with the originalOperators
                        filteredOperators = new ArrayList<>(originalOperators);
                        showOperator(filteredOperators);
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

    private void showOperator(List<Map<String, String>> operators) {
        operatorAdapter = new OperatorAdapter(operators);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(FasTagOperatorActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(operatorAdapter);
    }

    private class OperatorAdapter extends RecyclerView.Adapter<OperatorAdapter.Holder> implements Filterable {

        public List<Map<String, String>> operators;

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String searchString = constraint.toString().toLowerCase(Locale.getDefault());
                    filteredOperators.clear();
                    if (searchString.isEmpty()) {
                        filteredOperators.addAll(originalOperators);
                    } else {
                        for (Map<String, String> operator : originalOperators) {
                            if (operator.containsKey("name")) {
                                String name = operator.get("name");
                                if (name.toLowerCase(Locale.getDefault()).contains(searchString)) {
                                    filteredOperators.add(operator);
                                }
                            }
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredOperators;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    // Update the filtered list and notify the adapter
                    notifyDataSetChanged();
                }
            };
        }

        public OperatorAdapter(List<Map<String, String>> operators) {
            this.operators = operators;
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
            Map<String, String> operatorMap = operators.get(position);
            if (operatorMap.containsKey("name")) {
                holder.listName.setText(operatorMap.get("name"));
            }
            holder.next_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FasTagOperatorActivity.this, FasTagFetchBillActivity.class);
                    if (operatorMap.containsKey("id")) {
                        intent.putExtra("id", operatorMap.get("id"));
                    }
                    if (operatorMap.containsKey("name")) {
                        intent.putExtra("name", operatorMap.get("name"));
                    }
                    if (operatorMap.containsKey("viewbill")) {
                        intent.putExtra("viewbill", operatorMap.get("viewbill"));
                    }
                    if (operatorMap.containsKey("displayname")) {
                        intent.putExtra("displayname", operatorMap.get("displayname"));
                    }
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return operators.size();
        }

        class Holder extends RecyclerView.ViewHolder {
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
}