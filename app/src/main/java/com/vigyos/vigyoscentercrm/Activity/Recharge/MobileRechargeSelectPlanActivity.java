package com.vigyos.vigyoscentercrm.Activity.Recharge;

import android.annotation.SuppressLint;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.os.BuildCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Activity.MainActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Constant.DialogCustom;
import com.vigyos.vigyoscentercrm.Model.KeyValueModel;
import com.vigyos.vigyoscentercrm.Model.MobileRechargeOperatorModel;
import com.vigyos.vigyoscentercrm.Model.PlanDetailsModel;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class MobileRechargeSelectPlanActivity extends AppCompatActivity {

    private String operatorID, operatorName;
    private String circle;
    private TextView operatorNameText;
    private TextView mobileNumberText;
    private Spinner operatorList;
    private Spinner stateList;
    private RelativeLayout numberAndOperatorLyt;
    private RecyclerView topUpRecyclerView;
    private RecyclerView planDetailsRecyclerView;
    private Dialog dialog;
    private ArrayList<String> bankListArray = new ArrayList<>();
    private ArrayList<PlanDetailsModel> planDetailsModels = new ArrayList<>();
    private PlanDetailsAdapter detailsAdapter;
    private boolean isFirstCall = false;
    private ArrayList<MobileRechargeOperatorModel> mobileRechargeOperatorModels = new ArrayList<>();
    private boolean isInitialSetup = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_recharge_select_plan);
        Initialization();
        Declaration();
    }

    private void Initialization() {
        operatorNameText = findViewById(R.id.operatorName);
        mobileNumberText = findViewById(R.id.mobileNumber);
        operatorList = findViewById(R.id.operatorList);
        stateList = findViewById(R.id.stateList);
        numberAndOperatorLyt = findViewById(R.id.numberAndOperatorLyt);
        topUpRecyclerView = findViewById(R.id.topUpRecyclerView);
        planDetailsRecyclerView = findViewById(R.id.planDetailsRecyclerView);
    }

    private void Declaration() {
        Intent intent = getIntent();
        OperatorList();
//        CircleList();
        planForRecharge(intent.getStringExtra("mobileNumber"));
    }

    private void OperatorList() {
        Call<Object> objectCall = RetrofitClient.getApi().mobileRechargeOperator(SplashActivity.prefManager.getToken());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse " + response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        if (jsonObject.has("data")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            bankListArray.add(0, "Operator");
                            for (int i = 0; i<jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                MobileRechargeOperatorModel rechargeOperatorModel = new MobileRechargeOperatorModel();
                                if (object.has("category") && object.getString("category").equalsIgnoreCase("Prepaid")) {
                                    if (object.has("id")) {
                                        rechargeOperatorModel.setOperatorId(object.getString("id"));
                                    }
                                    if (object.has("name")) {
                                        rechargeOperatorModel.setOperatorName(object.getString("name"));
                                        bankListArray.add(object.getString("name"));
                                    }
                                    if (object.has("category")) {
                                        rechargeOperatorModel.setOperatorCategory(object.getString("category"));
                                    }
                                    mobileRechargeOperatorModels.add(rechargeOperatorModel);
                                }
                            }
                        }
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(MobileRechargeSelectPlanActivity.this, R.layout.layout_spinner_item, bankListArray); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    operatorList.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    operatorList.setAdapter(spinnerArrayAdapter);
                    operatorList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            spinnerArrayAdapter.remove("Jio");

                            if (parent.getItemAtPosition(position).equals("Operator")) {
                                Log.i("12121", "Operator");
                            } else {
                                ((TextView)parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_vigyos));
                                String selectedItem = (String) parent.getItemAtPosition(position);
                                for (MobileRechargeOperatorModel bankListModel : mobileRechargeOperatorModels) {
                                    if (bankListModel.getOperatorName().equalsIgnoreCase(selectedItem)) {
                                        operatorName = bankListModel.getOperatorName();
                                        operatorID = bankListModel.getOperatorId();
                                        Log.i("789654", "Operator Name: - " + operatorName + " Operator ID: -  " + operatorID);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) { }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2016", "onFailure " + t);
            }
        });
    }

    private void CircleList() {
        Call<Object> objectCall = RetrofitClient.getApi().mobileRechargeCircle(SplashActivity.prefManager.getToken());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse " + response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        if (jsonObject.has("data")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject dataObject = jsonArray.getJSONObject(i);
                                MobileRechargeOperatorModel rechargeOperatorModel = new MobileRechargeOperatorModel();
                                if (dataObject.has("key")) {
                                    rechargeOperatorModel.setKey(dataObject.getString("key"));
                                }
                                if (dataObject.has("name")) {
                                    rechargeOperatorModel.setName(dataObject.getString("name"));
                                }
                                mobileRechargeOperatorModels.add(rechargeOperatorModel);
                            }
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2016", "onFailure " + t);
            }
        });
    }

    private void planForRecharge(String mobileNumber) {
        pleaseWait();
        Call<Object> call = RetrofitClient.getApi().planForRecharge(SplashActivity.prefManager.getToken(), mobileNumber,"mobile");
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse "+ response);
                dismissDialog();
                List<KeyValueModel> keyValueList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        // Iterate through operator_id object
                        if (jsonObject.has("operator_id")) {
                            JSONObject operatorIdObject = jsonObject.getJSONObject("operator_id");
                            if (operatorIdObject.has("id")) {
                                operatorID = operatorIdObject.getString("id");
                            }
                            if (operatorIdObject.has("name")) {
                                operatorName = operatorIdObject.getString("name");
                            }
                        }
                        if (jsonObject.has("circle")) {
                            JSONObject circleObject = jsonObject.getJSONObject("circle");
                            if (circleObject.has("circle")) {
                                circle = circleObject.getString("circle");
                            }
                        }
                        // Iterate through plans object
                        if (jsonObject.has("plans")) {
                            JSONObject plansObject = jsonObject.getJSONObject("plans");
                            // Extract values from the info object within plans
                            if (plansObject.has("info")) {
                                JSONObject infoObject = plansObject.getJSONObject("info");
                                iterateJsonObject(keyValueList, infoObject);
                            }
                        }

                        numberAndOperatorLyt.setVisibility(View.VISIBLE);
                        operatorNameText.setText(operatorName);
                        mobileNumberText.setText("+91-"+mobileNumber);
                    } else {
                        if (jsonObject.has("message")){
                            DialogCustom.showAlertDialog(MobileRechargeSelectPlanActivity.this, "Alert!", jsonObject.getString("message"), "OK",true, () -> {});
                        } else {
                            DialogCustom.showAlertDialog(MobileRechargeSelectPlanActivity.this, "Alert!", "Maintenance underway. We'll be back soon. Try after sometime...", "OKAY",true, () -> {
                                startActivity(new Intent(MobileRechargeSelectPlanActivity.this, MainActivity.class));
                                finish();
                            });
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                // Now you have a list of KeyValueModel objects containing the key-value pairs
                // Pass this list to your RecyclerView adapter
                Log.i("6481644", "fgg " + keyValueList.toString());

                PlanAdaptor adapter = new PlanAdaptor(keyValueList);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(MobileRechargeSelectPlanActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
                topUpRecyclerView.setLayoutManager(gridLayoutManager);
                topUpRecyclerView.setItemAnimator(new DefaultItemAnimator());
                topUpRecyclerView.setAdapter(adapter);
                isFirstCall = true;
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2016", "onFailure "+ t);
                dismissDialog();
            }
        });
    }

    private void iterateJsonObject(List<KeyValueModel> keyValueList, JSONObject jsonObject) throws JSONException {
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);
            Log.i("852147", "  key: " + key + " value: " + value);
            // Add the key-value pair to the list
            keyValueList.add(new KeyValueModel(key, value.toString()));
        }
    }

    private class PlanAdaptor extends RecyclerView.Adapter<PlanAdaptor.Holder>{

        private List<KeyValueModel> keyValueList;
        private int selectedItemPosition = 0; // Initialize with an invalid value

        public PlanAdaptor(List<KeyValueModel> keyValueList) {
            this.keyValueList = keyValueList;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_plan_title, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {
            KeyValueModel keyValueModel = keyValueList.get(position);
            holder.planTitle.setText(keyValueModel.getKey());

            if (position == 0 && isFirstCall) {
                showDetailsForPlan(keyValueModel.getKey());
                holder.planTitle.setTextColor(getColor(R.color.dark_vigyos));
                holder.viewLine.setVisibility(View.VISIBLE);
                isFirstCall = false;
                Log.i("829471","position "+ keyValueModel.getKey() + " position - " + position );
            }

            // Set text color and visibility based on the selected position
            boolean isSelected = (position == selectedItemPosition);
            holder.planTitle.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), isSelected ? R.color.dark_vigyos : R.color.not_click));
            holder.viewLine.setVisibility(isSelected ? View.VISIBLE : View.GONE);

            holder.topUpLyt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle item click here
                    KeyValueModel clickedModel = keyValueList.get(position);  // Get the specific model for the clicked position
                    showDetailsForPlan(clickedModel.getKey());
                    setSelectedItemPosition(position);
                    Log.i("829471","position 2 "+ clickedModel.getKey() + " position - " + position);
                }
            });
        }

        public void setSelectedItemPosition(int position) {
            if (selectedItemPosition != position) {
                int previousSelectedItem = selectedItemPosition;
                selectedItemPosition = position;

                // Notify item change to update colors
                notifyItemChanged(previousSelectedItem);
                notifyItemChanged(selectedItemPosition);
            }
        }

        private void showDetailsForPlan(String planTitle) {
            // Find the details for the selected planTitle
            List<KeyValueModel> detailsList = findDetailsForPlan(planTitle);
            // Extract "rs" and "desc" values from the JSON array
            List<PlanDetailsModel> rsAndDescList = new ArrayList<>();
            planDetailsModels.clear();
            try {
                // Assuming detailsList contains only one item, which is a JSON array
                JSONArray jsonArray = new JSONArray(detailsList.get(0).getValue());
                // Iterate through each object in the array
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    PlanDetailsModel planDetailsModel = new PlanDetailsModel();
                    if (jsonObject.has("rs")) {
                        planDetailsModel.setRs(jsonObject.optString("rs", "N/A"));
                    }
                    if (jsonObject.has("desc")) {
                        planDetailsModel.setDesc(jsonObject.optString("desc", "No description available"));
                    }
                    if (jsonObject.has("validity")) {
                        planDetailsModel.setValidity(jsonObject.optString("validity", "N/A"));
                    }
                    planDetailsModels.add(planDetailsModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Set up and display the details in planDetailsRecyclerView
            detailsAdapter = new PlanDetailsAdapter(planDetailsModels);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(MobileRechargeSelectPlanActivity.this, 1, GridLayoutManager.VERTICAL, false);
            planDetailsRecyclerView.setLayoutManager(gridLayoutManager);
            planDetailsRecyclerView.setItemAnimator(new DefaultItemAnimator());
            planDetailsRecyclerView.setAdapter(detailsAdapter);
            detailsAdapter.notifyDataSetChanged();
        }

        private List<KeyValueModel> findDetailsForPlan(String planTitle) {
            List<KeyValueModel> detailsList = new ArrayList<>();
            // Iterate through your original data (e.g., stored in keyValueList)
            for (KeyValueModel keyValueModel : keyValueList) {
                if (keyValueModel.getKey().equals(planTitle)) {
                    // Add the details for the selected planTitle
                    detailsList.add(keyValueModel);
                }
            }
            return detailsList;
        }

        @Override
        public int getItemCount() {
            return keyValueList.size();
        }

        private class Holder extends RecyclerView.ViewHolder{

            RelativeLayout topUpLyt;
            TextView planTitle;
            View viewLine;

            public Holder(@NonNull View itemView) {
                super(itemView);
                topUpLyt = itemView.findViewById(R.id.topUpLyt);
                planTitle = itemView.findViewById(R.id.planTitle);
                viewLine = itemView.findViewById(R.id.viewLine);
            }
        }
    }

    private class PlanDetailsAdapter extends RecyclerView.Adapter<PlanDetailsAdapter.Holder> {

        private ArrayList<PlanDetailsModel> detailsList;

        public PlanDetailsAdapter(ArrayList<PlanDetailsModel> detailsList) {
            this.detailsList = detailsList;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater =  LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_plan_details , parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            PlanDetailsModel keyValueModel = detailsList.get(position);
            holder.planAmount.setText(keyValueModel.getRs());
            holder.planDetails.setText(keyValueModel.getDesc());
            holder.validity.setText(keyValueModel.getValidity());


            Log.i("81488","plan rs "+ keyValueModel.getRs());
            Log.i("81488","desc "+ keyValueModel.getDesc());
        }

        @Override
        public int getItemCount() {
            return detailsList.size();
        }

        private class Holder extends RecyclerView.ViewHolder{
            TextView planAmount;
            TextView planDetails;
            TextView validity;
            public Holder(@NonNull View itemView) {
                super(itemView);
                planAmount = itemView.findViewById(R.id.planAmount);
                planDetails = itemView.findViewById(R.id.planDetails);
                validity = itemView.findViewById(R.id.validity);
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
    protected void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }
}