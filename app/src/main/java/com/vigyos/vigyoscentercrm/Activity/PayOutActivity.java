package com.vigyos.vigyoscentercrm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Model.PayoutBankNameModel;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayOutActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView payoutBalance;
    private RelativeLayout addBank;
    private Spinner payoutBankName;
    private EditText payoutAmount;
    private RelativeLayout createPayout;
    private Dialog dialog;
    private ArrayList<PayoutBankNameModel> bankNameModels = new ArrayList<>();
    private ArrayList<String> bankNameArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_out);
        initialization();
        declaration();
    }

    private void initialization() {
        ivBack = findViewById(R.id.ivBack);
        payoutBalance = findViewById(R.id.payoutBalance);
        addBank = findViewById(R.id.addBank);
        payoutBankName = findViewById(R.id.payoutBankName);
        payoutAmount = findViewById(R.id.payoutAmount);
        createPayout = findViewById(R.id.createPayout);
    }

    private void declaration() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (SplashActivity.prefManager.getPayoutBalance() == 0){
            payoutBalance.setText("₹"+"0.00");
        } else {
            int i = SplashActivity.prefManager.getPayoutBalance();
            float v = (float) i;
            payoutBalance.setText("₹"+ v);
        }
        addBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PayOutActivity.this, AddBankAccountActivity.class));
            }
        });

        payoutList();

    }

    private void payoutList(){
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().payoutList(SplashActivity.prefManager.getToken(), SplashActivity.prefManager.getMerchantId());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("2016","onResponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("status")){
                        if (jsonObject.getString("status").equalsIgnoreCase("true")){
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i ++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                PayoutBankNameModel bankNameModel = new PayoutBankNameModel();
                                bankNameModel.setBeneid(jsonObject1.getString("beneid"));
                                bankNameModel.setMerchantcode(jsonObject1.getString("merchantcode"));
                                bankNameModel.setBankname(jsonObject1.getString("bankname"));
                                bankNameModel.setAccount(jsonObject1.getString("account"));
                                bankNameModel.setIfsc(jsonObject1.getString("ifsc"));
                                bankNameModel.setName(jsonObject1.getString("name"));
                                bankNameModel.setAccount_type(jsonObject1.getString("account_type"));
                                bankNameModels.add(bankNameModel);
                                bankNameArray.add(jsonObject1.getString("bankname"));
                            }
                        }
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(PayOutActivity.this, android.R.layout.simple_spinner_item, bankNameArray); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    payoutBankName.setAdapter(spinnerArrayAdapter);
                    payoutBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedItem = (String) parent.getItemAtPosition(position);
                            for (PayoutBankNameModel bankNameModel: bankNameModels){
                                if(bankNameModel.getBankname().equalsIgnoreCase(selectedItem)){
                                    String bankName = bankNameModel.getBankname();
                                    String beneId  = bankNameModel.getBeneid();
                                    if (bankName.isEmpty()) {
                                        Toast.makeText(PayOutActivity.this, "Add Bank Account", Toast.LENGTH_SHORT).show();
                                    }
                                    Log.i("789654", "Bank Name: - " + bankName + "  beneId  " + beneId);
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
                dismissDialog();
                Log.i("2016","onFailure" + t);
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
    public void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }
}