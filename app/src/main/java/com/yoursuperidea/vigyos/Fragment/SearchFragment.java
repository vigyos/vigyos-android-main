package com.yoursuperidea.vigyos.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.google.gson.Gson;
import com.yoursuperidea.vigyos.Activity.SplashActivity;
import com.yoursuperidea.vigyos.Adapter.AdapterForRecyclerview;
import com.yoursuperidea.vigyos.R;
import com.yoursuperidea.vigyos.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private AutoCompleteTextView searchView;
    private RecyclerView recyclerView;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_search, container, false);

        String[] arr = {"GST Registration", "GST Return", "GST Return Nil", "GST Return Regular", "Income tax", "FSSAI Registration", "LOGO design","GST Registration", "GST Return", "GST Return Nil", "GST Return Regular", "Income tax"};

        getServicesData();

        return view;
    }

    private void initialization(){
        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.order_list);
    }

    private void Declaration(){

//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(new AdapterForRecyclerview(arr));
//
//        searchView.clearFocus();

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return true;
//            }
//        });

    }

    private void getServicesData(){
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        Call<Object> objectCall = RetrofitClient.getApi().getServiceData("Bearer "+SplashActivity.prefManager.getToken(), "100");
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                progressDialog.dismiss();
                Log.i("12121", "onResponse " + response);
                if (response.code() == 200){
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        JSONArray jsonObject1 = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonObject1.length(); i++ ){
                            JSONObject jsonObject2 = jsonObject1.getJSONObject(i);

                            String name = jsonObject2.getString("service_name");

                            Log.i("21212112","name - "+ name);

                        }



                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("12121", "onFailure " + t);
            }
        });
    }

}