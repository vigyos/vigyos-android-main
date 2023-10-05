package com.vigyos.vigyoscentercrm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Model.BankListModel;
import com.vigyos.vigyoscentercrm.Model.PayoutBankListModel;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBankAccountActivity extends AppCompatActivity {

    private ArrayList<String> bankName = new ArrayList<>();
    private ArrayList<PayoutBankListModel> bankListModels = new ArrayList<>();
    private Spinner bank_name;
    private EditText accountNumber;
    private EditText ifscNumber;
    private EditText accountName;
    private ImageView passbookImage;
    private Spinner selectDocumentType;
    private ImageView frontImage;
    private ImageView backImage;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_account);
        initialization();
        declaration();
    }

    private void initialization() {
        bank_name = findViewById(R.id.bank_name);
        accountNumber = findViewById(R.id.accountNumber);
        ifscNumber = findViewById(R.id.ifscNumber);
        accountName = findViewById(R.id.accountName);
        passbookImage = findViewById(R.id.passbookImage);
        selectDocumentType = findViewById(R.id.selectDocumentType);
        frontImage = findViewById(R.id.frontImage);
        frontImage = findViewById(R.id.frontImage);
        backImage = findViewById(R.id.backImage);
    }

    private void declaration() {
        payoutBanks();
    }

    private void payoutBanks(){
        Call<Object> objectCall = RetrofitClient.getApi().payoutBankList(SplashActivity.prefManager.getToken());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("123654", "onResponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")){
                        JSONArray jsonArray = jsonObject.getJSONArray("bank_list");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            PayoutBankListModel listModel = new PayoutBankListModel();
                            listModel.setBank_id(jsonObject1.getString("bank_id"));
                            listModel.setBank_name(jsonObject1.getString("bank_name"));
                            bankListModels.add(listModel);
                            bankName.add(jsonObject1.getString("bank_name"));
                        }
                    } else {
                        SplashActivity.prefManager.setClear();
                        startActivity(new Intent(AddBankAccountActivity.this, LoginActivity.class));
                        finish();
                        Snackbar.make(findViewById(android.R.id.content), "Session expired please login again", Snackbar.LENGTH_LONG).show();
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(AddBankAccountActivity.this, android.R.layout.simple_spinner_item, bankName); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    bank_name.setAdapter(spinnerArrayAdapter);
                    bank_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedItem = (String) parent.getItemAtPosition(position);
                            for (PayoutBankListModel listModel: bankListModels){
                                if(listModel.getBank_name().equalsIgnoreCase(selectedItem)){
                                    name = listModel.getBank_name();
                                    String bankId  = listModel.getBank_id();
                                    Log.i("789654", "Bank Name: - " + name + " " + bankId);
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
                Log.i("123654", "onFailure" + t);
            }
        });
    }
}