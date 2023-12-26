package com.vigyos.vigyoscentercrm.Activity.BBPS.BillPay;

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
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class BBPSPayBillsActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView titleName;
    private TextView operatorName;
    private TextView displayName, billAmountText;
    private LinearLayout billAmountFocus;
    private RelativeLayout billNumberLyt, billAmountLyt;
    private EditText billNumber, billAmount;
    private TextView payBillText;
    private RelativeLayout payBillButton;
    private Dialog dialog;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbpspay_bills);
        Initialization();
        Declaration();
    }

    private void Initialization() {
        ivBack = findViewById(R.id.ivBack);
        titleName = findViewById(R.id.titleName);
        operatorName = findViewById(R.id.operatorName);
        displayName = findViewById(R.id.displayName);
        billAmountText = findViewById(R.id.billAmountText);
        billAmountFocus = findViewById(R.id.billAmountFocus);
        billNumberLyt = findViewById(R.id.billNumberLyt);
        billAmountLyt = findViewById(R.id.billAmountLyt);
        billNumber = findViewById(R.id.billNumber);
        billAmount = findViewById(R.id.billAmount);
        payBillText = findViewById(R.id.payBillText);
        payBillButton = findViewById(R.id.payBillButton);
    }

    private void Declaration() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (checkPermission()){
            getLocation();
        } else {
            requestPermissions();
        }

        Intent intent = getIntent();
        String OperatorId = intent.getStringExtra("id");
        String OperatorName = intent.getStringExtra("name");
        String category = intent.getStringExtra("category");
        String viewbill = intent.getStringExtra("viewbill");
        String regex = intent.getStringExtra("regex");
        String displayname = intent.getStringExtra("displayname");
        String ad1_d_name = intent.getStringExtra("ad1_d_name");
        String ad1_name = intent.getStringExtra("ad1_name");
        String ad1_regex = intent.getStringExtra("ad1_regex");
        String ad2_name = intent.getStringExtra("ad2_name");
        String ad3_name = intent.getStringExtra("ad3_name");
        String ad3_regex = intent.getStringExtra("ad3_regex");

        titleName.setText(category);
        operatorName.setText(OperatorName);
        displayName.setText(displayname);

        if (viewbill.equalsIgnoreCase("0")) {
            payBillText.setText("Pay Bill");
            billAmountFocus.setVisibility(View.VISIBLE);
        } else {
            payBillText.setText("Fetch Bill Details");
            billAmountFocus.setVisibility(View.GONE);
        }
        billNumber.requestFocus();
        billNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    billNumberLyt.setBackgroundResource(R.drawable.credential_border);
                    billAmountLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        billAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    billNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    billAmountLyt.setBackgroundResource(R.drawable.credential_border);
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        payBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(BBPSPayBillsActivity.this, R.anim.viewpush));
                if (TextUtils.isEmpty(billNumber.getText().toString())) {
                    billNumber.setError("This field is required");
                    billNumber.requestFocus();
                    billNumberLyt.startAnimation(AnimationUtils.loadAnimation(BBPSPayBillsActivity.this, R.anim.shake_animation));
                    Toast.makeText(BBPSPayBillsActivity.this, "Enter " +displayname, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (viewbill.equalsIgnoreCase("0")) {
                    if (TextUtils.isEmpty(billAmount.getText().toString())) {
                        billAmount.setError("This field is required");
                        billAmount.requestFocus();
                        billAmountLyt.startAnimation(AnimationUtils.loadAnimation(BBPSPayBillsActivity.this, R.anim.shake_animation));
                        Toast.makeText(BBPSPayBillsActivity.this, "Enter Amount", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    areYouSure(OperatorId, billNumber.getText().toString(), billAmount.getText().toString());
                } else {
                    fetchBill(OperatorId, billNumber.getText().toString(), OperatorName);
                }
            }
        });
    }

    private void areYouSure(String operatorId, String billNumber, String billAmount) {
        dialog = new Dialog(BBPSPayBillsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_yes_or_no);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setLayout(-1, -1);
        TextView title = dialog.findViewById(R.id.title);
        title.setText("Alert!");
        TextView details = dialog.findViewById(R.id.details);
        details.setText("Please verify all the details before paying. Do you want to proceed with the payment?");
        details.setMovementMethod(LinkMovementMethod.getInstance());
        dialog.findViewById(R.id.noLyt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the "GRANT!" button is clicked
                v.startAnimation(AnimationUtils.loadAnimation(BBPSPayBillsActivity.this, R.anim.viewpush));
                dismissDialog();
            }
        });
        dialog.findViewById(R.id.yesLyt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the "GRANT!" button is clicked
                v.startAnimation(AnimationUtils.loadAnimation(BBPSPayBillsActivity.this, R.anim.viewpush));
                dismissDialog();
                payBillWithoutFetch(operatorId, billNumber, billAmount);
            }
        });
        dialog.show();
    }

    private void payBillWithoutFetch(String operatorId, String billNumber, String billAmount) {
        pleaseWait();

        float floatValue = Float.parseFloat(billAmount);
        int intValue = (int) floatValue;
        String stringWithoutDecimal = String.valueOf(intValue);

        PayBillsModel.RequestModel requestModel = new PayBillsModel.RequestModel();
        requestModel.setUserId(SplashActivity.prefManager.getUserID());
        requestModel.setOperator(operatorId);
        requestModel.setCanumber(billNumber);
        requestModel.setAmount(stringWithoutDecimal);
        requestModel.setLatitude(String.valueOf(latitude));
        requestModel.setLongitude(String.valueOf(longitude));
        requestModel.setMode("online");
        requestModel.setMobilenumber(SplashActivity.prefManager.getPhone());
        requestModel.setAccessmodetype("APP");
        requestModel.setTransactiontype("BILL_PAY");

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(gson.toJson(requestModel), JsonObject.class);

        Log.i("24864680", "bdfjdg "+ jsonObject.toString());

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
                            DialogCustom.showAlertDialog(BBPSPayBillsActivity.this, "Payment Successful!", jsonObject1.getString("message"), "OKAY", false, () -> {
                                startActivity(new Intent(BBPSPayBillsActivity.this, MainActivity.class));
                                finish();
                            });
                        }
                    } else {
                        if (jsonObject1.has("message")) {
                            DialogCustom.showAlertDialog(BBPSPayBillsActivity.this, "Alert!", jsonObject1.getString("message"), "OK", true, () -> {});
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

    private void fetchBill(String OperatorId, String canumber, String OperatorName) {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().fetchBill(SplashActivity.prefManager.getToken(), OperatorId, canumber, "online");
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse " + response);
                dismissDialog();
                try {
                    Intent intent = new Intent(BBPSPayBillsActivity.this, BBPSPayBills2Activity.class);
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        Iterator<String> keys = jsonObject.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            try {
                                Object value = jsonObject.get(key);
                                intent.putExtra(key, value.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        intent.putExtra("OperatorId", OperatorId);
                        intent.putExtra("OperatorName", OperatorName);
                        intent.putExtra("billnumer", canumber);
                        startActivity(intent);
                    } else {
                        if (jsonObject.has("message")) {
                            DialogCustom.showAlertDialog(BBPSPayBillsActivity.this, "Alert!", jsonObject.getString("message"), "OK", true, () -> {});
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
                Toast.makeText(BBPSPayBillsActivity.this, "Maintenance underway. We'll be back soon.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pleaseWait() {
        dialog = new Dialog(BBPSPayBillsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
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

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(BBPSPayBillsActivity.this, ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        Dexter.withContext(BBPSPayBillsActivity.this)
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
        AlertDialog.Builder builder = new AlertDialog.Builder(BBPSPayBillsActivity.this);
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
        if (ContextCompat.checkSelfPermission(BBPSPayBillsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(BBPSPayBillsActivity.this, new OnSuccessListener<Location>() {
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
                    .addOnFailureListener(BBPSPayBillsActivity.this, new OnFailureListener() {
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