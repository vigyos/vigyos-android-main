package com.vigyos.vigyoscentercrm.Fragment;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.vigyos.vigyoscentercrm.Activity.AEPSActivity;
import com.vigyos.vigyoscentercrm.Activity.AccountActivity;
import com.vigyos.vigyoscentercrm.Activity.LoginActivity;
import com.vigyos.vigyoscentercrm.Activity.NotificationActivity;
import com.vigyos.vigyoscentercrm.Activity.PanCardActivity;
import com.vigyos.vigyoscentercrm.Activity.SeeMoreServicesActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Activity.SubCatServiceActivity;
import com.vigyos.vigyoscentercrm.Activity.UserActivity;
import com.vigyos.vigyoscentercrm.Activity.WalletActivity;
import com.vigyos.vigyoscentercrm.Adapter.BannerListAdapter;
import com.vigyos.vigyoscentercrm.AppController;
import com.vigyos.vigyoscentercrm.FingerPrintModel.Opts;
import com.vigyos.vigyoscentercrm.FingerPrintModel.PidData;
import com.vigyos.vigyoscentercrm.FingerPrintModel.PidOptions;
import com.vigyos.vigyoscentercrm.Model.BannerListModel;
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
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {

    public View view;
    private final Activity activity;
    private static final String TAG = "HomeFragment";
    private TextView userName , amount;
    private CircleImageView userAccount;
    private RecyclerView bannerRecyclerView;
    private RelativeLayout notification;
    private LinearLayout walletView;
    private LinearLayout seeMore;
    private LinearLayout taxService, dscService, itrService;
    private LinearLayout eWay, accounting, iec;
    private LinearLayout ipr;
    private LinearLayout aeps, panCard, gst, udyam;
    private String ipAddress;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude;
    private double longitude;
    private String currentDateAndTime;
    public PidData pidData = null;
    private Serializer serializer = null;
    public ArrayList<String> positions;
    private ArrayList<BannerListModel> bannerListModels = new ArrayList<>();
    private Dialog dialog;

    public HomeFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initialization();
        declaration();
        return view;
    }

    private void initialization(){
        userName = view.findViewById(R.id.userNameHome);
        userAccount = view.findViewById(R.id.profile_image);
        notification = view.findViewById(R.id.notification);
        amount = view.findViewById(R.id.amount);
        bannerRecyclerView = view.findViewById(R.id.bannerRecyclerView);
        walletView = view.findViewById(R.id.walletView);
        aeps = view.findViewById(R.id.aeps);
        panCard = view.findViewById(R.id.panService);
        gst = view.findViewById(R.id.gst);
        udyam = view.findViewById(R.id.udyam);
        taxService = view.findViewById(R.id.taxService);
        dscService = view.findViewById(R.id.dscService);
        itrService = view.findViewById(R.id.itrService);
        eWay = view.findViewById(R.id.eWay);
        accounting = view.findViewById(R.id.accounting);
        iec = view.findViewById(R.id.iec);
        ipr = view.findViewById(R.id.ipr);
        seeMore = view.findViewById(R.id.seeMore);
    }

    private void declaration(){
        //SetListeners
        userAccount.setOnClickListener(this);
        walletView.setOnClickListener(this);
        notification.setOnClickListener(this);
        aeps.setOnClickListener(this);
        panCard.setOnClickListener(this);
        gst.setOnClickListener(this);
        udyam.setOnClickListener(this);
        taxService.setOnClickListener(this);
        dscService.setOnClickListener(this);
        itrService.setOnClickListener(this);
        eWay.setOnClickListener(this);
        accounting.setOnClickListener(this);
        iec.setOnClickListener(this);
        ipr.setOnClickListener(this);
        seeMore.setOnClickListener(this);

//        AppController.backCheck = true;

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        if (checkPermission()){
            getLocation();
        } else {
            requestPermissions();
        }
        dateAndTime();
        iPAddress();
        serializer = new Persister();
        positions = new ArrayList<>();

        Picasso.get().load(SplashActivity.prefManager.getProfilePicture()).into(userAccount);
        userName.setText(SplashActivity.prefManager.getFirstName()+" "+SplashActivity.prefManager.getLastName());
        if (SplashActivity.prefManager.getAmount() == 0){
            amount.setText("0.00");
        } else {
            int i = SplashActivity.prefManager.getAmount();
            float v = (float) i;
            amount.setText(String.valueOf(v));
        }
        bannerShow();
    }

    private void bannerShow(){
        bannerRecyclerView = view.findViewById(R.id.bannerRecyclerView);
        BannerListModel bannerListModel = new BannerListModel();
        bannerListModel.setBannerImage("https://vigyos-upload-files.s3.amazonaws.com/68f0c22b-a037-41b5-9281-7e5ffba225c8");
        bannerListModel.setBannerImage("https://vigyos-upload-files.s3.amazonaws.com/f273d695-6fac-44f6-8557-502c5485d38b");
        bannerListModel.setBannerImage("https://vigyos-upload-files.s3.amazonaws.com/9d27b9cd-acdb-4188-a8ea-845e296cf977");
        bannerListModels.add(bannerListModel);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://vigyos-upload-files.s3.amazonaws.com/d57fb922-5b7d-401e-83db-a0d3041fb05a");
//        arrayList.add("https://vigyos-upload-files.s3.amazonaws.com/f273d695-6fac-44f6-8557-502c5485d38b");
//        arrayList.add("https://vigyos-upload-files.s3.amazonaws.com/9d27b9cd-acdb-4188-a8ea-845e296cf977");

        Log.i("98745","list size: -  "+ bannerListModels.size());
        BannerListAdapter bannerListAdapter = new BannerListAdapter(arrayList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 1,GridLayoutManager.HORIZONTAL, false);
        bannerRecyclerView.setLayoutManager(gridLayoutManager);
        bannerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        bannerRecyclerView.setAdapter(bannerListAdapter);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.seeMore:
                startActivity(new Intent(activity, SeeMoreServicesActivity.class));
                break;
            case R.id.ipr:
                Intent intent8 = new Intent(activity, SubCatServiceActivity.class);
                intent8.putExtra("serviceName", "Intellectual Properties");
                intent8.putExtra("serviceID", "1011");
                startActivity(intent8);
                break;
            case R.id.iec:
                Intent intent7 = new Intent(activity, SubCatServiceActivity.class);
                intent7.putExtra("serviceName", "IEC – Import/Export Code");
                intent7.putExtra("serviceID", "1015");
                startActivity(intent7);
                break;
            case R.id.accounting:
                Intent intent6 = new Intent(activity, SubCatServiceActivity.class);
                intent6.putExtra("serviceName", "Accounting");
                intent6.putExtra("serviceID", "1020");
                startActivity(intent6);
                break;
            case R.id.eWay:
                Intent intent5 = new Intent(activity, SubCatServiceActivity.class);
                intent5.putExtra("serviceName", "E-Way Bills");
                intent5.putExtra("serviceID", "1017");
                startActivity(intent5);
                break;
            case R.id.itrService:
                Intent intent4 = new Intent(activity, SubCatServiceActivity.class);
                intent4.putExtra("serviceName", "Income Tax Returns");
                intent4.putExtra("serviceID", "1012");
                startActivity(intent4);
                break;
            case R.id.dscService:
                Intent intent3 = new Intent(activity, SubCatServiceActivity.class);
                intent3.putExtra("serviceName", "DSC Services");
                intent3.putExtra("serviceID", "1013");
                startActivity(intent3);
                break;
            case R.id.taxService:
                Intent intent2 = new Intent(activity, SubCatServiceActivity.class);
                intent2.putExtra("serviceName", "Tax Services");
                intent2.putExtra("serviceID", "1018");
                startActivity(intent2);
                break;
            case R.id.udyam:
                Intent intent1 = new Intent(activity, SubCatServiceActivity.class);
                intent1.putExtra("serviceName", "Registrations");
                intent1.putExtra("serviceID", "1014");
                startActivity(intent1);
                break;
            case R.id.gst:
                Intent intent = new Intent(activity, SubCatServiceActivity.class);
                intent.putExtra("serviceName", "GST");
                intent.putExtra("serviceID", "1011");
                startActivity(intent);
                break;
            case R.id.panService:
                startActivity(new Intent(activity, PanCardActivity.class));
                break;
            case R.id.aeps:
                Log.i("741258", "time Aeps " + formatTimestamp(SplashActivity.prefManager.getLastVerifyTimeStampAeps()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date loginDate = sdf.parse(formatTimestamp(SplashActivity.prefManager.getLastVerifyTimeStampAeps()));
                    Calendar loginCalendar = Calendar.getInstance();
                    loginCalendar.setTime(loginDate);

                    Calendar currentCalendar = Calendar.getInstance();
                    long diffInMillis = currentCalendar.getTimeInMillis() - loginCalendar.getTimeInMillis();
                    long diffInHours = diffInMillis / (60 * 60 * 1000);

                    if(diffInHours >= 24) {
                        // More than 24 hours have passed since the login
                        // Prompt user to log in again
                        Log.i("741258","More than 24 hours");
                        try {
                            String pidOption = getPIDOptions();
                            if (pidOption != null) {
                                Log.e("PidOptions", pidOption);
                                Intent intent9 = new Intent();
                                intent9.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                                intent9.putExtra("PID_OPTIONS", pidOption);
                                startActivityForResult(intent9, 2);
                            } else {
                                Log.i("454545","Device not found!");
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.toString());
                            Toast.makeText(activity, "Device not found!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Less than 24 hours have passed since the login
                        // User is still logged in
                        Log.i("741258","Less than 24 hours");
                        startActivity(new Intent(activity, AEPSActivity.class));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                startActivity(new Intent(activity, AEPSActivity.class));
                break;
            case R.id.notification:
                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
                startActivity(new Intent(activity, NotificationActivity.class));
                break;
            case R.id.profile_image:
                startActivity(new Intent(activity, UserActivity.class));
                break;
            case R.id.walletView:
                startActivity(new Intent(activity, WalletActivity.class));
                break;
            default:
                break;
        }
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
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        if (data != null) {
                            String result = data.getStringExtra("DEVICE_INFO");
                            String rdService = data.getStringExtra("RD_SERVICE_INFO");
                            String display = "";
                            if (rdService != null) {
                                display = "RD Service Info :\n" + rdService + "\n\n";
                            }
                            if (result != null) {
                                display += "Device Info :\n" + result;
//                                textView.setText(display);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Error while deserialize device info", e);
                    }
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        if (data != null) {
                            String result = data.getStringExtra("PID_DATA");
                            if (result != null) {
                                pidData = serializer.read(PidData.class, result);
//                                String fingerPrint = "<?xml version=\"1.0\"?>\r\n<PidData>\r\n  <Resp errCode=\"0\" errInfo=\"Success.\" fCount=\"1\" fType=\"2\" nmPoints=\"44\" qScore=\"61\" />\r\n  <DeviceInfo dpId=\"MANTRA.MSIPL\" rdsId=\"MANTRA.WIN.001\" rdsVer=\"1.0.8\" mi=\"MFS100\" mc=\"MIIEGDCCAwCgAwIBAgIEAoDegDANBgkqhkiG9w0BAQsFADCB6jEqMCgGA1UEAxMhRFMgTUFOVFJBIFNPRlRFQ0ggSU5ESUEgUFZUIExURCAzMVUwUwYDVQQzE0xCLTIwMyBTaGFwYXRoIEhleGEgT3Bwb3NpdGUgR3VqYXJhdCBIaWdoIENvdXJ0IFMuRyBIaWdod2F5IEFobWVkYWJhZCAtMzgwMDYwMRIwEAYDVQQJEwlBSE1FREFCQUQxEDAOBgNVBAgTB0dVSkFSQVQxCzAJBgNVBAsTAklUMSUwIwYDVQQKExxNQU5UUkEgU09GVEVDSCBJTkRJQSBQVlQgTFREMQswCQYDVQQGEwJJTjAeFw0yMzEwMDkwNjU3MTRaFw0yMzEwMjkwNzA4NDJaMIGwMSUwIwYDVQQDExxNYW50cmEgU29mdGVjaCBJbmRpYSBQdnQgTHRkMR4wHAYDVQQLExVCaW9tZXRyaWMgTWFudWZhY3R1cmUxDjAMBgNVBAoTBU1TSVBMMRIwEAYDVQQHEwlBSE1FREFCQUQxEDAOBgNVBAgTB0dVSkFSQVQxCzAJBgNVBAYTAklOMSQwIgYJKoZIhvcNAQkBFhVzdXBwb3J0QG1hbnRyYXRlYy5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDbxcCiUP5jBz2UPgoLX6b5xiPaJOEVX3roatokc6WyCF24tqEB7L+tI3FhJ4buI0TI5BiGqflCBCRAhWe54EmH1x/XQM5GvZeJronuIPQZcr9gAMoYIJ4qRTjIqH7qBjd9Yv/niZ+HpCq90JOV9Is/bEvWonIS25WDcHggipNQbXW+a/e2C3WIhAnQxc06e6nazKN1wiweBqELH7C23awPXmOYyHi98yMkoWqIeSD3Xoj/g/OQTXroiuM/nQQArpc4Odhr+FvCFY/tuVOAJ/PDfXztqzyx87CBq+avASN7YcZiOMwnb46ChJwEDnm8uRf5CmNha5I8xLuZ5Gw4mzWnAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAAVJlCW4YR7U9Ou1N9HM6MpqUhbw7ZdZBIZG4EGWtXwYmENrFKuLiR/MmZwObeX9OkqQAnCxWcnG6d7xiAE2EmIfQoCfk8ifgp577AiT3uficJguEFqL14a1iDXYNWbPMNoexIS0WoK1Po+F65pUEZpSwM/RCPEv2NSJ6LK1n/3QeNg00w+K5zE4m6Yz5caw3t5yqSHWB45+3Kv2rmtEnR5DEL3nzdo31mTt3kU2qbJHMo7V/w0ljwLd1qivhynmz5E8dN5uKUfc59anNkFK1HtatJdlOUZazIF12D8dl+ynuhHHn6MM+jF+bjgp2bZRJus6Ynaob0H3ANU8cGQV4k0=\" dc=\"b22eb081-2c82-489b-b7fe-76612c89473b\">\r\n    <additional_info>\r\n      <Param name=\"srno\" value=\"6456562\" />\r\n      <Param name=\"sysid\" value=\"6ECFEBF9E9A3658FBFF0\" />\r\n      <Param name=\"ts\" value=\"2023-10-09T12:43:16+05:30\" />\r\n    </additional_info>\r\n  </DeviceInfo>\r\n  <Skey ci=\"20250923\">bQjwmWnRH7ViGX3G1EFBJIlP/jbPSBE6XreZJm0gA1lBropuI5CTAONLXGncQh60ar4JSsZK88Xrf6USfB1VplwZFDvGLk1aGbJQdsi8pIbpYc83ISgfBN7XqN+eFEBCkbA0vKagZgvLY2xja0zl7ExH7G6y/BIGLSq4QKKfCZRAxFa8ldSpIImX201bfdVIqLLOTNi9S7UGdFZqo5Ycq3QSKoJ9OUZaUJLwXRTdJpiFLs0TAF4YzVPjRn2gZNz31RswiAsgJ718l8b8mOLKRxcDC4SodVw71dQLrxWdfQn/BvIEWht4LFfPbhts9XgI4NkLCVqHGOvEJMq7apBBKw==</Skey>\r\n  <Hmac>mLEsPIGm6Fq9u+aTUkamUg2DBwmU4FX5AUIJCK5OvwDvs5+C38gIbs5VRZlNmFbf</Hmac>\r\n  <Data type=\"X\">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</Data>\r\n</PidData>";
//                                AuthAPI(fingerPrint);
                                if (!pidData._Resp.errCode.equals("0")) {
                                    Toast.makeText(activity, "Device Not Found!", Toast.LENGTH_SHORT).show();
                                } else {
                                    AuthAPI(result);
                                    Log.i("78954", "case 2  result if : - " + pidData.toString());
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.i("78954", "Error while deserialize pid data " + e);
                        Toast.makeText(activity, "Failed to scan finger print", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void dateAndTime(){
        currentDateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    private void iPAddress(){
        Context context = requireContext().getApplicationContext();
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        ipAddress = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        Dexter.withContext(activity)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            getLocation();
                            Log.i("874521", "Permission  granted..");
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(error -> {
                    Log.d(TAG, "Error occurred! ");
                })
                .onSameThread().check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });
        builder.show();
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
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
                    .addOnFailureListener(activity, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("874521", "Error getting location");
                        }
                    });
        } else {
            requestPermissions();
        }
    }

    private void AuthAPI(String fingerData){
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().AuthAPI(SplashActivity.prefManager.getToken(), "APP", SplashActivity.prefManager.getAadhaarNumber(), SplashActivity.prefManager.getPhone(),
                String.valueOf(latitude), String.valueOf(longitude), currentDateAndTime, fingerData, ipAddress, "2", SplashActivity.prefManager.getMerchantId());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("2016", "onResponse "+ response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                        Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(activity, AEPSActivity.class));
                    } else {
                        if (jsonObject.has("message")) {
                            Snackbar.make(activity.findViewById(android.R.id.content), jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();
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
                Toast.makeText(activity, "Failed Authentication", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pleaseWait(){
        dialog = new Dialog(activity);
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