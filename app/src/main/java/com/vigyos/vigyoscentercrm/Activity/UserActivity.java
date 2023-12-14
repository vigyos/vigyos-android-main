package com.vigyos.vigyoscentercrm.Activity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.app.Activity;
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
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.os.BuildCompat;

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
import com.vigyos.vigyoscentercrm.Activity.AEPS.PayOutActivity;
import com.vigyos.vigyoscentercrm.FingerPrintModel.Opts;
import com.vigyos.vigyoscentercrm.FingerPrintModel.PidData;
import com.vigyos.vigyoscentercrm.FingerPrintModel.PidOptions;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class UserActivity extends AppCompatActivity {

    private LinearLayout accountInformation, payOutBalance;
    private LinearLayout screenLock, refundPolicy;
    private LinearLayout termAndConditions, privacyPolicy;
    private LinearLayout helpAndSupport, logout;
    private ImageView ivBack;
    public PidData pidData = null;
    private Serializer serializer = null;
    public ArrayList<String> positions;
    private Dialog dialog;
    private String ipAddress;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude;
    private double longitude;
    private String currentDateAndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initialization();
        declaration();
    }

    private void initialization() {
        ivBack = findViewById(R.id.ivBack);
        accountInformation = findViewById(R.id.accountInformation);
        payOutBalance = findViewById(R.id.payOutBalance);
        screenLock = findViewById(R.id.screenLock);
        refundPolicy = findViewById(R.id.refundPolicy);
        termAndConditions = findViewById(R.id.termAndConditions);
        privacyPolicy = findViewById(R.id.privacyPolicy);
        helpAndSupport = findViewById(R.id.helpAndSupport);
        logout = findViewById(R.id.logout);
    }

    private void declaration() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (checkPermission()){
            getLocation();
        } else {
            requestPermissions();
        }
        dateAndTime();
        iPAddress();
        serializer = new Persister();
        positions = new ArrayList<>();


        if (SplashActivity.prefManager.getBiometricSensor()) {
            screenLock.setVisibility(View.GONE);
        } else {
            screenLock.setVisibility(View.VISIBLE);
        }

        accountInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this, AccountActivity.class));
            }
        });
        payOutBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("741258", "time Aeps " + formatTimestamp(SplashActivity.prefManager.getFinoLastVerifyTimestampAeps()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date loginDate = sdf.parse(formatTimestamp(SplashActivity.prefManager.getFinoLastVerifyTimestampAeps()));
                    Calendar loginCalendar = Calendar.getInstance();
                    loginCalendar.setTime(loginDate);

                    Calendar currentCalendar = Calendar.getInstance();
                    long diffInMillis = currentCalendar.getTimeInMillis() - loginCalendar.getTimeInMillis();
                    long diffInHours = diffInMillis / (60 * 60 * 1000);

                    if(diffInHours >= 24) {
                        // More than 24 hours have passed since the login
                        // Prompt user to log in again
                        Log.i("741258","More than 24 hours");
                        if (checkPermission()) {
                            try {
                                String pidOption = getPIDOptions();
                                if (pidOption != null) {
                                    Log.e("PidOptions", pidOption);
                                    Intent intent9 = new Intent();
                                    intent9.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                                    intent9.putExtra("PID_OPTIONS", pidOption);
                                    startActivityForResult(intent9, 1);
                                } else {
                                    Log.i("454545","Device not found!");
                                }
                            } catch (Exception e) {
                                Log.e("Error", e.toString());
                                Toast.makeText(UserActivity.this, "Device not found!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            requestPermissions();
                        }
                    } else {
                        // Less than 24 hours have passed since the login
                        // User is still logged in
                        Log.i("741258","Less than 24 hours");
                        startActivity(new Intent(UserActivity.this, PayOutActivity.class));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        screenLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this, BiometricLockActivity.class));
            }
        });
        refundPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this, RefundPolicyActivity.class));
            }
        });
        termAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this, TermsAndConditionsActivity.class));
            }
        });
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this, PrivacyPolicyActivity.class));
            }
        });
        helpAndSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this, HelpAndSupportActivity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areYouSure();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    private void areYouSure(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(UserActivity.this);
        builder1.setMessage("Are you sure, You want to logout ?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        SplashActivity.prefManager.setClear();
                        startActivity(new Intent(UserActivity.this, LoginActivity.class));
                        finish();
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

    private String formatTimestamp(long timestamp) {
        Date date = new Date(timestamp * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return formatter.format(date);
    }

    private String getPIDOptions() {
        try {
            String posh = "UNKNOWN";
            if (positions.size() > 0) {
                posh = positions.toString().replace("[", "").replace("]", "").replaceAll("[\\s+]", "");
            }
            Opts opts = new Opts();
            opts.fCount = "1";
            opts.fType = "2";
            opts.iCount = "0";
            opts.iType = "0";
            opts.pCount = "0";
            opts.pType = "0";
            opts.format = "0";
            opts.pidVer = "2.0";
            opts.timeout = "10000";
            opts.posh = posh;
            opts.env = "p";

            PidOptions pidOptions = new PidOptions();
            pidOptions.ver = "1.0";
            pidOptions.Opts = opts;

            Serializer serializer = new Persister();
            StringWriter writer = new StringWriter();
            serializer.write(pidOptions, writer);
            return writer.toString();
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    if (data != null) {
                        String result = data.getStringExtra("PID_DATA");
                        if (result != null) {
                            pidData = serializer.read(PidData.class, result);
                            if (!pidData._Resp.errCode.equals("0")) {
                                Toast.makeText(UserActivity.this, "Device Not Found!", Toast.LENGTH_SHORT).show();
                            } else {
                                AuthAPI(result);
                                Log.i("78954", "case 2  result if : - " + pidData.toString());
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.i("78954", "Error while deserialize pid data " + e);
                    Toast.makeText(UserActivity.this, "Failed to scan finger print", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
        int result = ContextCompat.checkSelfPermission(UserActivity.this, ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        Dexter.withContext(UserActivity.this)
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
        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
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
        if (ContextCompat.checkSelfPermission(UserActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(UserActivity.this, new OnSuccessListener<Location>() {
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
                    .addOnFailureListener(UserActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("874521", "Error getting location");
                        }
                    });
        } else {
            requestPermissions();
        }
    }

    private void AuthAPI(String fingerData) {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().AuthAPI(SplashActivity.prefManager.getToken(), "APP", SplashActivity.prefManager.getAadhaarNumber(), SplashActivity.prefManager.getPhone(),
                String.valueOf(latitude), String.valueOf(longitude), currentDateAndTime, fingerData, ipAddress, "2", SplashActivity.prefManager.getFinoMerchantId());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("2016", "onResponse "+ response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                        Toast.makeText(UserActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserActivity.this, PayOutActivity.class));
                    } else {
                        if (jsonObject.has("message")) {
                            Snackbar.make(findViewById(android.R.id.content), jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("2016", "onFailure "+ t);
                Toast.makeText(UserActivity.this, "Failed Authentication", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pleaseWait() {
        dialog = new Dialog(UserActivity.this);
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
}