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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class SearchServicesActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;
    private RecyclerView recyclerView;
    private ServiceItemListAdapter serviceItemListAdapter;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<SearchServicesModel> searchServicesModels = new ArrayList<>();
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_services);

        getServicesData();
        initialization();
        declaration();
        serviceList();

    }

    private void initialization(){
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
    }

//    @NonNull
//    @Override
//    public OnBackInvokedDispatcher getOnBackInvokedDispatcher() {
//        Log.i("1212"," backPressed");
//
//        return super.getOnBackInvokedDispatcher();
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("12121","onBackPressed");
        //        startActivity(new Intent(SearchServicesActivity.this, MainActivity.class));
        finish();

    }

    private void getServicesData(){
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().getServiceName(SplashActivity.prefManager.getToken(), "100");
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                dismissDialog();
                Log.i("12121", "onResponse " + response);
                if (response.code() == 200){
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        if (jsonObject.has("success") && jsonObject.getBoolean("success")){
                            JSONArray jsonObject1 = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonObject1.length(); i++ ){
                                JSONObject jsonObject2 = jsonObject1.getJSONObject(i);
                                SearchServicesModel servicesModel = new SearchServicesModel();
                                servicesModel.setService_id(jsonObject2.getString("service_id"));
                                servicesModel.setService(jsonObject2.toString());
                                servicesModel.setService_name(jsonObject2.getString("service_name"));
                                servicesModel.setDescription(jsonObject2.getString("description"));
                                searchServicesModels.add(servicesModel);
                            }
                        } else {
                            SplashActivity.prefManager.setClear();
                            startActivity(new Intent(SearchServicesActivity.this, LoginActivity.class));
                            finish();
                            Snackbar.make(findViewById(android.R.id.content), "Session expired please login again", Snackbar.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    serviceList();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                dismissDialog();
                Log.i("12121", "onFailure " + t);
            }
        });
    }

    private void pleaseWait(){
        dialog = new Dialog(this);
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
    protected void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }

    private void serviceList(){
        serviceItemListAdapter = new ServiceItemListAdapter(searchServicesModels, SearchServicesActivity.this);
        gridLayoutManager = new GridLayoutManager(this, 1 , GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(serviceItemListAdapter);
    }

    public class ServiceItemListAdapter extends RecyclerView.Adapter<ServiceItemListAdapter.MyViewHolder> implements Filterable {

        private ArrayList<SearchServicesModel> searchListArray;
        private ArrayList<SearchServicesModel> searchFilterList;
        private Activity activity;

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

        public ServiceItemListAdapter(ArrayList<SearchServicesModel> searchServicesModels, Activity activity) {
            super();
            this.searchListArray = searchServicesModels;
            this.activity = activity;
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

            private TextView listName;
            private LinearLayout next;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                listName = itemView.findViewById(R.id.listName);
                next = itemView.findViewById(R.id.next_ll);
            }
        }
    }
}