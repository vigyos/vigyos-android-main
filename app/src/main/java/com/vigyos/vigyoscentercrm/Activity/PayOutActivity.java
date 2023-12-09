package com.vigyos.vigyoscentercrm.Activity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

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
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.os.BuildCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

@BuildCompat.PrereleaseSdkCheck
public class PayOutActivity extends AppCompatActivity {

    private ImageView ivBack;
    private LinearLayout payoutBankNameFocus, payoutAmountFocus;
    private RelativeLayout payoutBankNameLyt, payoutAmountLyt;
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
    private Animation animation;

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
        payoutBankNameFocus = findViewById(R.id.payoutBankNameFocus);
        payoutAmountFocus = findViewById(R.id.payoutAmountFocus);
        payoutBankNameLyt = findViewById(R.id.payoutBankNameLyt);
        payoutAmountLyt = findViewById(R.id.payoutAmountLyt);
    }

    private void declaration() {
        animation = AnimationUtils.loadAnimation(PayOutActivity.this, R.anim.shake_animation);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(PayOutActivity.this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (SplashActivity.prefManager.getFinoPayoutBalance() == 0){
            payoutBalance.setText("₹"+"0.00");
        } else {
            int i = SplashActivity.prefManager.getFinoPayoutBalance();
            float v = (float) i;
            payoutBalance.setText("₹"+ v);
        }
        addBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(PayOutActivity.this, R.anim.viewpush));
                startActivity(new Intent(PayOutActivity.this, AddBankAccountActivity.class));
            }
        });
        payoutList();
        payoutAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    payoutAmountLyt.setBackgroundResource(R.drawable.credential_border);
                }
            }
        });
        createPayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(PayOutActivity.this, R.anim.viewpush));
                if (payoutBankName.getSelectedItem().toString().trim().equals("Select your bank")) {
                    payoutBankNameLyt.startAnimation(animation);
                    payoutBankNameFocus.getParent().requestChildFocus(payoutBankNameFocus, payoutBankNameFocus);
                    Toast.makeText(PayOutActivity.this,"Select your bank",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(payoutAmount.getText().toString())){
                    payoutAmount.setError("This field is required");
                    payoutAmount.requestFocus();
                    payoutAmountLyt.startAnimation(animation);
                    payoutAmountFocus.getParent().requestChildFocus(payoutAmountFocus, payoutAmountFocus);
                    Toast.makeText(PayOutActivity.this, "Enter a Amount", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!isNumeric(payoutAmount.getText().toString())){
                        payoutAmount.setError("Enter a valid Amount");
                        payoutAmount.requestFocus();
                        payoutAmountLyt.startAnimation(animation);
                        payoutAmountFocus.getParent().requestChildFocus(payoutAmountFocus, payoutAmountFocus);
                        Toast.makeText(PayOutActivity.this, "Enter a valid Amount", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                payoutAmount.clearFocus();
                if (checkPermission()) {
                    areYouSure();
                } else {
                    requestPermissions();
                }
            }
        });
    }

    private void areYouSure(){
        dialog = new Dialog(PayOutActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_yes_or_no);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setLayout(-1, -1);
        TextView title = dialog.findViewById(R.id.title);
        title.setText("Create Payout!");
        TextView details = dialog.findViewById(R.id.details);
        details.setText("Are you sure, You want to Create Payout?");
        details.setMovementMethod(LinkMovementMethod.getInstance());
        dialog.findViewById(R.id.noLyt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the "GRANT!" button is clicked
                v.startAnimation(AnimationUtils.loadAnimation(PayOutActivity.this, R.anim.viewpush));
                dismissDialog();
            }
        });
        dialog.findViewById(R.id.yesLyt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the "GRANT!" button is clicked
                v.startAnimation(AnimationUtils.loadAnimation(PayOutActivity.this, R.anim.viewpush));
                dismissDialog();
                createPayoutAPI();
            }
        });
        dialog.show();
    }

    public static boolean isNumeric(String strNum){
        double amt = Double.parseDouble(strNum);
        return !(amt <= 0);
    }

    private void createPayoutAPI() {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().createPayOutAPI(SplashActivity.prefManager.getToken(), beneId, payoutAmount.getText().toString(), "IMPS", SplashActivity.prefManager.getFinoMerchantId(),
                "APP", currentDateAndTime, SplashActivity.prefManager.getAadhaarNumber(), SplashActivity.prefManager.getPhone(), String.valueOf(latitude), String.valueOf(longitude), ipAddress);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("2019", "onResponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")){
                        if (jsonObject.has("message")) {
                            Toast.makeText(PayOutActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        payoutBalanceUpdate();
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

    private void payoutBalanceUpdate() {
        Call<Object> objectCall = RetrofitClient.getApi().profile(SplashActivity.prefManager.getToken());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("12121", "onResponse " + response);
                if (response.code() == 200){
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            if (jsonObject1.has("payout_balance")) {
                                SplashActivity.prefManager.setFinoPayoutBalance(jsonObject1.getInt("payout_balance"));
                            }
                            if (SplashActivity.prefManager.getFinoPayoutBalance() == 0){
                                payoutBalance.setText("₹"+"0.00");
                            } else {
                                int i = SplashActivity.prefManager.getFinoPayoutBalance();
                                float v = (float) i;
                                payoutBalance.setText("₹"+ v);
                            }
                        } else {
                            if (jsonObject.has("message")) {
                                Toast.makeText(PayOutActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(PayOutActivity.this, "Maintenance underway. We'll be back soon.", Toast.LENGTH_SHORT).show();
                    SplashActivity.prefManager.setClear();
                    startActivity(new Intent(PayOutActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("12121", "onFailure " + t);
                Toast.makeText(PayOutActivity.this, "Maintenance underway. We'll be back soon.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void payoutList(){
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().payoutList(SplashActivity.prefManager.getToken(), SplashActivity.prefManager.getFinoMerchantId());
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
                        bankNameArray.add(0,"Select your bank");
                        if (jsonObject.has("message")) {
                            Toast.makeText(PayOutActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(PayOutActivity.this, R.layout.layout_spinner_item, bankNameArray); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    payoutBankName.setAdapter(spinnerArrayAdapter);
                    payoutBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (parent.getItemAtPosition(position).equals("Select your bank")) {
                                Log.i("12121","Select your bank");
                            } else {
                                ((TextView)parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_vigyos));
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
                Toast.makeText(PayOutActivity.this, "Maintenance underway. We'll be back soon.", Toast.LENGTH_SHORT).show();
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

    public void requestPermissions() {
        Dexter.withContext(PayOutActivity.this)
                .withPermission(ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getLocation();
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        // check for permanent decline of permission
                        if (permissionDeniedResponse.isPermanentlyDenied()) {
                            // navigate user to app settings
                            showSettingsDialog();
                        } else {
                            // Permission was temporarily denied
                            // Show a rationale to the user and request the permission again
                            showPermissionRationale();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void showPermissionRationale() {
        new AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("This app requires access to your location to proceed. Please grant permission.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User agreed to grant permission, continue the permission request
                        Dexter.withContext(PayOutActivity.this)
                                .withPermission(ACCESS_FINE_LOCATION)
                                .withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                        getLocation();
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
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User canceled the permission request, handle this case as needed
                    }
                }).show();
    }

    private void showSettingsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Need Permissions")
                .setMessage("This app needs location permission to use this feature. You can grant them in app settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
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