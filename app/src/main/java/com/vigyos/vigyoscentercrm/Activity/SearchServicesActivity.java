package com.vigyos.vigyoscentercrm.Activity;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Model.SearchServicesModel;
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
public class SearchServicesActivity extends AppCompatActivity {

    private ImageView ivBack;
    private AutoCompleteTextView autoCompleteTextView;
    private RecyclerView recyclerView;
    private ServiceItemListAdapter serviceItemListAdapter;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<SearchServicesModel> searchServicesModels = new ArrayList<>();
    private Dialog dialog;
    private int page = 1;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_services);
        initialization();
        declaration();
    }

    private void initialization(){
        ivBack = findViewById(R.id.ivBack);
        autoCompleteTextView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.order_list);
    }

    private void declaration(){
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                serviceItemListAdapter.getFilter().filter(charSequence);
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
//        serviceList();
        getServicesData(page);
    }

    private void serviceList() {
        serviceItemListAdapter = new ServiceItemListAdapter(searchServicesModels);
        gridLayoutManager = new GridLayoutManager(this, 1 , GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(serviceItemListAdapter);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (!isLoading && !recyclerView.canScrollVertically(1)) {
//                    page++;
//                    getServicesData(page);
//                    isLoading = true;
//                }
//            }
//        });
//        getServicesData(page);
    }

    private void getServicesData(int page) {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().getServiceName(SplashActivity.prefManager.getToken(), "10000");
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse " + response);
                dismissDialog();
                isLoading = false;
                if (response.code() == 200){
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                            if (jsonObject.has("data")) {
                                JSONArray jsonObject1 = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonObject1.length(); i++ ){
                                    JSONObject jsonObject2 = jsonObject1.getJSONObject(i);
                                    SearchServicesModel servicesModel = new SearchServicesModel();
                                    if (jsonObject2.has("service_id")) {
                                        servicesModel.setService_id(jsonObject2.getString("service_id"));
                                    }
                                    if (jsonObject2.has("service_name")) {
                                        servicesModel.setService_name(jsonObject2.getString("service_name"));
                                    }
                                    if (jsonObject2.has("description")) {
                                        servicesModel.setDescription(jsonObject2.getString("description"));
                                    }
                                    searchServicesModels.add(servicesModel);
                                }
                            }
//                            serviceItemListAdapter.notifyDataSetChanged();
                            serviceList();
                        } else {
                            if (jsonObject.has("message")) {
                                Toast.makeText(SearchServicesActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2016", "onFailure " + t);
                dismissDialog();
                isLoading = false;
                Toast.makeText(SearchServicesActivity.this, "Maintenance underway. We'll be back soon.", Toast.LENGTH_SHORT).show();
            }
        });
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
    protected void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class ServiceItemListAdapter extends RecyclerView.Adapter<ServiceItemListAdapter.MyViewHolder> implements Filterable {

        public ArrayList<SearchServicesModel> searchListArray;
        public ArrayList<SearchServicesModel> searchFilterList;

        @Override
        public Filter getFilter() {
            return filterData;
        }

        private Filter filterData =new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<SearchServicesModel> filteredList = new ArrayList<>();
                if (charSequence == null || charSequence.length() ==0 ){
                    filteredList.addAll(searchFilterList);
                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();
                    for (SearchServicesModel item : searchFilterList){
                        if (item.getService_name().toLowerCase().contains(filterPattern)){
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
                searchListArray.clear();
                searchListArray.addAll((ArrayList) filterResults.values);
                notifyDataSetChanged();
            }
        };

        public ServiceItemListAdapter(ArrayList<SearchServicesModel> searchServicesModels) {
            super();
            this.searchListArray = searchServicesModels;
            this.searchFilterList = new ArrayList<>(searchServicesModels);
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_search_services_item, parent, false);
            return new ServiceItemListAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ServiceItemListAdapter.MyViewHolder holder, int position) {
            SearchServicesModel model = searchListArray.get(position);
            holder.listName.setText(model.getService_name());
            holder.next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SearchServicesActivity.this, ShowServicesActivity.class);
                    intent.putExtra("details", model.getDescription());
                    intent.putExtra("id", model.getService_id());
                    intent.putExtra("service", model.getService());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return searchListArray.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            public TextView listName;
            public RelativeLayout next;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                listName = itemView.findViewById(R.id.listName);
                next = itemView.findViewById(R.id.next_ll);
            }
        }
    }
}