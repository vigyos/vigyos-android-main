package com.vigyos.vigyoscentercrm.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Activity.SubCatServiceActivity;
import com.vigyos.vigyoscentercrm.Model.SeeMoreServiceModel;
import com.vigyos.vigyoscentercrm.Model.ServiceListModel;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SeeMoreServicesFragment extends Fragment {

    private View view;
    private ImageView ivBack;
    private RecyclerView recyclerViewMore;
    private ArrayList<SeeMoreServiceModel> seeMoreServiceModels = new ArrayList<>();
    private Dialog dialog;

    public SeeMoreServicesFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_see_more_services, container, false);
        initialization();
        declaration();
        return view;
    }

    private void initialization() {
        recyclerViewMore = view.findViewById(R.id.recyclerViewMore);
        ivBack = view.findViewById(R.id.ivBack);
    }

    private void declaration() {
//        ivBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
        serviceList();
    }

    private void serviceList(){
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().servicesGCList(SplashActivity.prefManager.getToken());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("2016","onResponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success")){
                        if (jsonObject.getString("success").equalsIgnoreCase("true")){
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i<jsonArray.length(); i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                SeeMoreServiceModel serviceModel = new SeeMoreServiceModel();
                                serviceModel.setServiceName(jsonObject1.getString("service_group_name"));
                                serviceModel.setServiceID(jsonObject1.getString("service_group_id"));
                                if (jsonObject1.has("service_group_icon")){
                                    serviceModel.setServiceIcon(jsonObject1.getString("service_group_icon"));
                                } else {
                                    serviceModel.setServiceIcon("https://www.indiafilings.com/learn/wp-content/uploads/2020/10/shutterstock_1067245667.jpg");
                                }
                                seeMoreServiceModels.add(serviceModel);
                            }
                            showAllService();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("2016","onFailure" + t);

            }
        });
    }

    private void showAllService(){
        ShowAllServicesAdapter showAllServicesAdapter = new ShowAllServicesAdapter(seeMoreServiceModels, getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        recyclerViewMore.setLayoutManager(gridLayoutManager);
        recyclerViewMore.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMore.setAdapter(showAllServicesAdapter);
    }

    private void pleaseWait(){
        dialog = new Dialog(getActivity());
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
    public void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }

    private class ShowAllServicesAdapter extends RecyclerView.Adapter<ShowAllServicesAdapter.Holder>{

        private ArrayList<SeeMoreServiceModel> seeMoreServiceModels;
        private Activity activity;

        public ShowAllServicesAdapter(ArrayList<SeeMoreServiceModel> seeMoreServiceModels, Activity activity) {
            this.seeMoreServiceModels = seeMoreServiceModels;
            this.activity = activity;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.seemaore_item_layout, parent, false);
            return new Holder(view);
        }

        @Override
        public int getItemCount() {
            return seeMoreServiceModels.size();
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            SeeMoreServiceModel serviceModel = seeMoreServiceModels.get(position);
            holder.serviceTitle.setText(serviceModel.getServiceName());
            Picasso.get().load(serviceModel.getServiceIcon()).into(holder.iconImage);
            holder.totalCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, SubCatServiceActivity.class);
                    intent.putExtra("serviceID", serviceModel.getServiceID());
                    intent.putExtra("serviceName", serviceModel.getServiceName());
                    startActivity(intent);
                }
            });
        }

        private class Holder extends RecyclerView.ViewHolder{

            private ImageView iconImage;
            private TextView serviceTitle;
            private CardView totalCardView;

            public Holder(@NonNull View itemView) {
                super(itemView);
                iconImage =  itemView.findViewById(R.id.iconImage);
                serviceTitle =  itemView.findViewById(R.id.serviceTitle);
                totalCardView =  itemView.findViewById(R.id.TotalCardView);
            }
        }
    }
}