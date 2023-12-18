package com.vigyos.vigyoscentercrm.Activity.BBPS;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Constant.DialogCustom;
import com.vigyos.vigyoscentercrm.Model.PayBillModel;
import com.vigyos.vigyoscentercrm.Model.PayBillsModel;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class BBPSPayBills2Activity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView operatorName;
    private TextView billNumber;
    private TextView customerName;
    private TextView billDate;
    private TextView dueDate;
    private TextView amount;
    private Dialog dialog1;
    private RelativeLayout payBillButton;
    private TextView payBillText;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbpspay_bills2);
        Initialization();
        Declaration();
    }

    private void Initialization() {
        ivBack = findViewById(R.id.ivBack);
        operatorName = findViewById(R.id.operatorName);
        billNumber = findViewById(R.id.billNumber);
        customerName = findViewById(R.id.customerName);
        billDate = findViewById(R.id.billDate);
        dueDate = findViewById(R.id.dueDate);
        amount = findViewById(R.id.amount);
        payBillButton = findViewById(R.id.payBillButton);
        payBillText = findViewById(R.id.payBillText);
    }

    private void Declaration() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (checkPermission()){
            getLocation();
        } else {
            requestPermissions();
        }
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String billAmount = intent.getStringExtra("billAmount");
        String billnetamount = intent.getStringExtra("billnetamount");
        String billDate = intent.getStringExtra("billdate");
        String dueDate = intent.getStringExtra("dueDate");
        String minBillAmount = intent.getStringExtra("minBillAmount");
        boolean acceptPayment = intent.getBooleanExtra("acceptPayment", false);
        boolean acceptPartPay = intent.getBooleanExtra("acceptPartPay", false);
        String cellNumber = intent.getStringExtra("cellNumber");
        String userName = intent.getStringExtra("userName");

        this.operatorName.setText(name);
        this.billNumber.setText(cellNumber);
        this.customerName.setText(userName);
        this.billDate.setText(billDate);
        this.dueDate.setText(dueDate);
        this.amount.setText("₹ "+billAmount);
        this.payBillText.setText("Pay Bill "+"(₹"+billAmount+")");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        payBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payBillAPI(id, "102277100", "8990.0", "8990.0", "01 Dec 2023", "2024-01-01", "8990", true, true, "SALMAN");
            }
        });
    }

    private void payBillAPI(String id, String cellNumber, String billAmount, String billnetamount, String billDate, String dueDate, String minBillAmount,
                            boolean acceptPayment, boolean acceptPartPay, String userName) {
        pleaseWait();
        PayBillsModel requestModel = new PayBillsModel();
        requestModel.setUser_id(SplashActivity.prefManager.getUserID());
        requestModel.setOperator(id);
        requestModel.setCanumber(cellNumber);
        requestModel.setAmount(billAmount);
        requestModel.setLatitude(String.valueOf(latitude));
        requestModel.setLongitude(String.valueOf(longitude));
        requestModel.setMode("online");

        // Set values for the "bill_fetch" object
        PayBillsModel.BillFetch billFetch = new PayBillsModel.BillFetch();
        billFetch.setBillAmount(billAmount);
        billFetch.setBillnetamount(billnetamount);
        billFetch.setBilldate(billDate);
        billFetch.setDueDate(dueDate);
//        billFetch.setM(minBillAmount);
        billFetch.setAcceptPayment(acceptPayment);
        billFetch.setAcceptPartPay(acceptPartPay);
        billFetch.setCellNumber(cellNumber);
        billFetch.setUserName(userName);

        requestModel.setBill_fetch(billFetch);
        requestModel.setMobilenumber(SplashActivity.prefManager.getPhone());
        requestModel.setAccessmodetype("APP");
        requestModel.setTransactiontype("BILL_PAY");

        Log.i("57411987", "dfgd  " +  requestModel.toString());

        Call<PayBillsModel> objectCall = RetrofitClient.getApi().payBill(SplashActivity.prefManager.getToken(), requestModel);
        objectCall.enqueue(new Callback<PayBillsModel>() {
            @Override
            public void onResponse(@NonNull Call<PayBillsModel> call, @NonNull Response<PayBillsModel> response) {
                Log.i("2016", "onResponse " + new Gson().toJson(response.body()));
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        if (jsonObject.has("message")) {
                            DialogCustom.showAlertDialog(BBPSPayBills2Activity.this, "Alert!", jsonObject.getString("message"), "OK", () -> {});
                        }
                    } else {
                        if (jsonObject.has("message")) {

                            Log.i("251455552", "bgvjdfkdg" + jsonObject.getString("message"));
                            DialogCustom.showAlertDialog(BBPSPayBills2Activity.this, "Alert!", jsonObject.getString("message"), "OK", () -> {});
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PayBillsModel> call, @NonNull Throwable t) {
                Log.i("2016", "onFailure " + t);
                dismissDialog();
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(BBPSPayBills2Activity.this, ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        Dexter.withContext(BBPSPayBills2Activity.this)
                .withPermission(ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        if (permissionDeniedResponse.isPermanentlyDenied()) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(BBPSPayBills2Activity.this);
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
        if (ContextCompat.checkSelfPermission(BBPSPayBills2Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(BBPSPayBills2Activity.this, new OnSuccessListener<Location>() {
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
                    .addOnFailureListener(BBPSPayBills2Activity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("874521", "Error getting location");
                        }
                    });
        } else {
            requestPermissions();
        }
    }

    private void pleaseWait() {
        dialog1 = new Dialog(BBPSPayBills2Activity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setContentView(R.layout.dialog_loader);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.show();
    }

    private void dismissDialog() {
        if (dialog1 != null && dialog1.isShowing()) {
            dialog1.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }
}