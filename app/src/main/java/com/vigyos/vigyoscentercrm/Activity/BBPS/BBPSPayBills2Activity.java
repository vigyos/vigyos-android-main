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
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.vigyos.vigyoscentercrm.Activity.MainActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Constant.DialogCustom;
import com.vigyos.vigyoscentercrm.Model.PayBillsModel;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class BBPSPayBills2Activity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView operatorNameText, billNumberText;
    private TextView customerNameText, billDateText;
    private TextView dueDateText, amountText;
    private String operatorName, billNumber;
    private String customerName, billDate;
    private String dueDate, amount;
    private String OperatorId, bill_fetch;
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
        operatorNameText = findViewById(R.id.operatorName);
        billNumberText = findViewById(R.id.billNumber);
        customerNameText = findViewById(R.id.customerName);
        billDateText = findViewById(R.id.billDate);
        dueDateText = findViewById(R.id.dueDate);
        amountText = findViewById(R.id.amount);
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
        if (intent != null) {
            // Iterate over the extras in the intent
            Bundle extras = intent.getExtras();
            if (extras != null) {
                for (String key : extras.keySet()) {
                    Object value = extras.get(key);
                    if (key.equals("OperatorId") && value instanceof String) {
                        OperatorId = (String) value;
                    }
                    if (key.equals("OperatorName") && value instanceof String) {
                        operatorName = (String) value;
                    }
                    if (key.equals("billnumer") && value instanceof String) {
                        billNumber = (String) value;
                    }
                    if (key.equals("name") && value instanceof String) {
                        customerName = (String) value;
                    }
                    if (key.equals("billdate") && value instanceof String) {
                        billDate = (String) value;
                    }
                    if (key.equals("duedate") && value instanceof String) {
                        dueDate = (String) value;
                    }
                    if (key.equals("amount") && value instanceof String) {
                        amount = (String) value;
                    }
                    if (key.equals("bill_fetch") && value instanceof String) {
                        bill_fetch = (String) value;
                    }
                }
            }
        }
        this.operatorNameText.setText(operatorName);
        this.billNumberText.setText(billNumber);
        this.customerNameText.setText(customerName);
        this.billDateText.setText(billDate);
        this.dueDateText.setText(dueDate);
        this.amountText.setText("₹ "+amount);
        this.payBillText.setText("Pay Bill "+"(₹"+amount+")");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        payBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(BBPSPayBills2Activity.this, R.anim.viewpush));
                areYouSure(OperatorId, billNumber, amount);
            }
        });
    }

    private void areYouSure(String OperatorId, String billNumber, String amount){
        dialog1 = new Dialog(BBPSPayBills2Activity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setContentView(R.layout.dialog_yes_or_no);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog1.getWindow().setLayout(-1, -1);
        TextView title = dialog1.findViewById(R.id.title);
        title.setText("Alert!");
        TextView details = dialog1.findViewById(R.id.details);
        details.setText("Please verify all the bill details before paying. Do you want to proceed with the payment?");
        details.setMovementMethod(LinkMovementMethod.getInstance());
        dialog1.findViewById(R.id.noLyt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the "GRANT!" button is clicked
                v.startAnimation(AnimationUtils.loadAnimation(BBPSPayBills2Activity.this, R.anim.viewpush));
                dismissDialog();
            }
        });
        dialog1.findViewById(R.id.yesLyt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the "GRANT!" button is clicked
                v.startAnimation(AnimationUtils.loadAnimation(BBPSPayBills2Activity.this, R.anim.viewpush));
                dismissDialog();
                payBillAPI(OperatorId, billNumber, amount);
            }
        });
        dialog1.show();
    }

    private void payBillAPI(String OperatorId, String billNumber, String amount) {
        pleaseWait();
        float floatValue = Float.parseFloat(amount);
        int intValue = (int) floatValue;
        String stringWithoutDecimal = String.valueOf(intValue);
        PayBillsModel.RequestModel requestModel = new PayBillsModel.RequestModel();
        requestModel.setUserId(SplashActivity.prefManager.getUserID());
        requestModel.setOperator(OperatorId);
        requestModel.setCanumber(billNumber);
        requestModel.setAmount(stringWithoutDecimal);
        requestModel.setLatitude(String.valueOf(latitude));
        requestModel.setLongitude(String.valueOf(longitude));
        requestModel.setMode("online");
        Gson gson = new Gson();
        JsonObject billFetchJson = gson.fromJson(bill_fetch, JsonObject.class);
        Map<String, String> billFetchObject = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : billFetchJson.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isString()) {
                billFetchObject.put(key, value.getAsString());
            }
        }
        requestModel.setBillFetch(billFetchObject);
        requestModel.setMobilenumber(SplashActivity.prefManager.getPhone());
        requestModel.setAccessmodetype("APP");
        requestModel.setTransactiontype("BILL_PAY");
        JsonObject jsonObject = gson.fromJson(gson.toJson(requestModel), JsonObject.class);
        Call<Object> call = RetrofitClient.getApi().payBill(SplashActivity.prefManager.getToken(), jsonObject);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse " + response);
                dismissDialog();
                try {
                    JSONObject jsonObject1 = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject1.has("success") && jsonObject1.getBoolean("success")) {
                        if (jsonObject1.has("message")) {
                            DialogCustom.showAlertDialog(BBPSPayBills2Activity.this, "Payment Successful!", jsonObject1.getString("message"), "OKAY", false, () -> {
                                startActivity(new Intent(BBPSPayBills2Activity.this, MainActivity.class));
                                finish();
                            });
                        }
                    } else {
                        if (jsonObject1.has("message")) {
                            DialogCustom.showAlertDialog(BBPSPayBills2Activity.this, "Alert!", jsonObject1.getString("message"), "OK", true, () -> {});
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
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