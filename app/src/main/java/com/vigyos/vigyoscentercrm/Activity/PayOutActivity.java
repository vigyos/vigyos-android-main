package com.vigyos.vigyoscentercrm.Activity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.Formatter;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.vigyos.vigyoscentercrm.Model.PayoutBankNameModel;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayOutActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView payoutBalance;
    private RelativeLayout addBank;
    private String bankName;
    private String beneId;
    private Spinner payoutBankName;
    private EditText payoutAmount;
    private RelativeLayout createPayout;
    private Dialog dialog;
    private ArrayList<PayoutBankNameModel> bankNameModels = new ArrayList<>();
    private ArrayList<String> bankNameArray = new ArrayList<>();
    private String ipAddress;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude;
    private double longitude;
    private String currentDateAndTime;

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(PayOutActivity.this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
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

        createPayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payoutBankName.getSelectedItem().toString().trim().equals("Select your bank")) {
                    Toast.makeText(PayOutActivity.this,"Select your bank",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(payoutAmount.getText().toString())){
                    payoutAmount.setError("This field is required");
                    Toast.makeText(PayOutActivity.this, "Enter a Amount", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!isNumeric(payoutAmount.getText().toString())){
                        payoutAmount.setError("Enter a valid Amount");
                        Toast.makeText(PayOutActivity.this, "Enter a valid Amount", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                payoutAmount.clearFocus();

                areYouSure();
            }
        });
    }

    private void areYouSure(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PayOutActivity.this);
        builder1.setMessage("Are you sure, You want to Create Payout ?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        createPayoutAPI();
                    }
                });
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static boolean isNumeric(String strNum){
        double amt = Double.parseDouble(strNum);
        return !(amt <= 0);
    }

    private void createPayoutAPI() {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().createPayOutAPI(SplashActivity.prefManager.getToken(), beneId, payoutAmount.getText().toString(), "IMPS", SplashActivity.prefManager.getMerchantId(),
                "APP", currentDateAndTime, SplashActivity.prefManager.getAadhaarNumber(), SplashActivity.prefManager.getPhone(), String.valueOf(latitude), String.valueOf(longitude), ipAddress);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("2019", "onResponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")){
                        if (jsonObject.has("message")){
                            Toast.makeText(PayOutActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (jsonObject.has("message")){
                            Toast.makeText(PayOutActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("2019", "onFailure" + t);
                Toast.makeText(PayOutActivity.this, "Payout Failed", Toast.LENGTH_SHORT).show();
            }
        });
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
                    if (jsonObject.has("status") && jsonObject.getBoolean("status") ){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        bankNameArray.add(0,"Select your bank");
                        for (int i = 0; i < jsonArray.length(); i ++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            PayoutBankNameModel bankNameModel = new PayoutBankNameModel();
                            if (jsonObject1.has("beneid")) {
                                bankNameModel.setBeneid(jsonObject1.getString("beneid"));
                            }
                            if (jsonObject1.has("merchantcode")) {
                                bankNameModel.setMerchantcode(jsonObject1.getString("merchantcode"));
                            }
                            if (jsonObject1.has("bankname")) {
                                bankNameModel.setBankname(jsonObject1.getString("bankname"));
                            }
                            if (jsonObject1.has("account")) {
                                bankNameModel.setAccount(jsonObject1.getString("account"));
                            }
                            if (jsonObject1.has("ifsc")) {
                                bankNameModel.setIfsc(jsonObject1.getString("ifsc"));
                            }
                            if (jsonObject1.has("name")) {
                                bankNameModel.setName(jsonObject1.getString("name"));
                                bankNameArray.add(jsonObject1.getString("bankname"));
                            }
                            if (jsonObject1.has("account_type")) {
                                bankNameModel.setAccount_type(jsonObject1.getString("account_type"));
                            }
                            bankNameModels.add(bankNameModel);
                        }
                    } else {
                        SplashActivity.prefManager.setClear();
                        startActivity(new Intent(PayOutActivity.this, LoginActivity.class));
                        finish();
                        Snackbar.make(findViewById(android.R.id.content), "Session expired please login again", Snackbar.LENGTH_LONG).show();
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(PayOutActivity.this, android.R.layout.simple_spinner_item, bankNameArray); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    payoutBankName.setAdapter(spinnerArrayAdapter);
                    payoutBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (parent.getItemAtPosition(position).equals("Select your bank")) {
                                Log.i("12121","Select your bank");
                            } else {
                                String selectedItem = (String) parent.getItemAtPosition(position);
                                for (PayoutBankNameModel bankNameModel: bankNameModels){
                                    if(bankNameModel.getBankname().equalsIgnoreCase(selectedItem)){
                                        bankName = bankNameModel.getBankname();
                                        beneId = bankNameModel.getBeneid();
                                        if (bankName.isEmpty()) {
                                            Toast.makeText(PayOutActivity.this, "Add Bank Account", Toast.LENGTH_SHORT).show();
                                        }
                                        Log.i("789654", "Bank Name: - " + bankName + "  beneId  " + beneId);
                                    }
                                }
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) { }
                    });

                    dateAndTime();
                    iPAddress();
                    if (checkPermission()) {
                        getLocation();
                    } else {
                        requestPermissions();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void dateAndTime(){
        currentDateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    private void iPAddress(){
        Context context = getApplicationContext();
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        ipAddress = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(PayOutActivity.this, ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        Dexter.withContext(PayOutActivity.this)
                .withPermission(ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getLocation();
                        Log.i("874521", "Permission  granted..");
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        // check for permanent decline of permission
                        if (permissionDeniedResponse.isPermanentlyDenied()) {
                            // navigate user to app settings
                            showSettingsDialog();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PayOutActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });
        builder.show();
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(PayOutActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(PayOutActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Log.i("874521", "Your Location: - "+ "Latitude: " + latitude + "\nLongitude: " + longitude);
                            } else {
                                Log.i("874521", "Location not available");
                            }
                        }
                    })
                    .addOnFailureListener(PayOutActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("874521", "Error getting location");
                        }
                    });
        } else {
            requestPermissions();
        }
    }
}