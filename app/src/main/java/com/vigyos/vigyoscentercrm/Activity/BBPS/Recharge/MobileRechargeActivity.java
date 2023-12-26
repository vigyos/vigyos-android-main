package com.vigyos.vigyoscentercrm.Activity.BBPS.Recharge;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.vigyos.vigyoscentercrm.Activity.MainActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Constant.DialogCustom;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class MobileRechargeActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView mobileNumberText;
    private TextView operatorAndCircleNameText;
    private TextView amountText;
    private TextView validity;
    private RelativeLayout changePlan;
    private TextView planDetails;
    private RelativeLayout rechargeButton;
    private String operatorID, mobileNumber;
    private String amount;
    private Dialog dialog;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_recharge);
        Initialization();
        Declaration();
    }

    private void Initialization() {
        ivBack = findViewById(R.id.ivBack);
        mobileNumberText = findViewById(R.id.mobileNumberText);
        operatorAndCircleNameText = findViewById(R.id.operatorAndCircleNameText);
        amountText = findViewById(R.id.amount);
        validity = findViewById(R.id.validity);
        changePlan = findViewById(R.id.changePlan);
        planDetails = findViewById(R.id.planDetails);
        rechargeButton = findViewById(R.id.rechargeButton);
    }

    private void Declaration() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MobileRechargeActivity.this);
        if (checkPermission()){
            getLocation();
        } else {
            DialogCustom.showAlertDialog(MobileRechargeActivity.this, getString(R.string.required_permissions), getString(R.string.allow_to_access), "GRANT!",true, (DialogCustom.AlertDialogListener) this::requestPermissions);
        }
        Intent intent = getIntent();
        operatorID = intent.getStringExtra("operatorID");
        mobileNumber = intent.getStringExtra("mobileNumber");
        amount = intent.getStringExtra("rs");
        mobileNumberText.setText("+91 "+intent.getStringExtra("mobileNumber"));
        operatorAndCircleNameText.setText(intent.getStringExtra("operatorName")+", "+intent.getStringExtra("circle"));
        amountText.setText("₹"+intent.getStringExtra("rs"));
        validity.setText(intent.getStringExtra("validity"));
        planDetails.setText(intent.getStringExtra("desc"));
        changePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(MobileRechargeActivity.this, R.anim.viewpush));
                finish();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rechargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(MobileRechargeActivity.this, R.anim.viewpush));
                areYouSure();
            }
        });
    }

    private void areYouSure(){
        dialog = new Dialog(MobileRechargeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_yes_or_no);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setLayout(-1, -1);
        TextView title = dialog.findViewById(R.id.title);
        title.setText("Mobile Recharge!");
        TextView details = dialog.findViewById(R.id.details);
        details.setText("Are you ready to complete the recharge?");
        details.setMovementMethod(LinkMovementMethod.getInstance());
        dialog.findViewById(R.id.noLyt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the "GRANT!" button is clicked
                v.startAnimation(AnimationUtils.loadAnimation(MobileRechargeActivity.this, R.anim.viewpush));
                dismissDialog();
            }
        });
        dialog.findViewById(R.id.yesLyt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the "GRANT!" button is clicked
                v.startAnimation(AnimationUtils.loadAnimation(MobileRechargeActivity.this, R.anim.viewpush));
                dismissDialog();
                DoRecharge();
            }
        });
        dialog.show();
    }

    private void DoRecharge() {
        pleaseWait();
        Call<Object> call = RetrofitClient.getApi().doRecharge(SplashActivity.prefManager.getToken(), SplashActivity.prefManager.getUserID(), operatorID, mobileNumber,
                Integer.parseInt(amount), String.valueOf(latitude), String.valueOf(longitude), SplashActivity.prefManager.getPhone(), "APP", "MOBILE_RECHARGE");
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016","onResponse" + response);
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        if (jsonObject.has("data")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            if (jsonObject1.has("message")) {
                                DialogCustom.showAlertDialog(MobileRechargeActivity.this, "Successfully Done!", jsonObject1.getString("message"), "OKAY",false, () -> {
                                    startActivity(new Intent(MobileRechargeActivity.this, MainActivity.class));
                                    finish();
                                });
                            }
                        }
                    } else {
                        if (jsonObject.has("message")){
                            DialogCustom.showAlertDialog(MobileRechargeActivity.this, "Alert!", jsonObject.getString("message"), "OKAY",true, () -> {});
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2016","onFailure" + t);
                dismissDialog();
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

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MobileRechargeActivity.this, ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions() {
        Dexter.withContext(MobileRechargeActivity.this)
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
                        Dexter.withContext(MobileRechargeActivity.this)
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
        if (ContextCompat.checkSelfPermission(MobileRechargeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(MobileRechargeActivity.this, new OnSuccessListener<Location>() {
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
                    .addOnFailureListener(MobileRechargeActivity.this, new OnFailureListener() {
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