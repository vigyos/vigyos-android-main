package com.vigyos.vigyoscentercrm.Fragment;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.os.Build;
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
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
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
import com.vigyos.vigyoscentercrm.Activity.NotificationActivity;
import com.vigyos.vigyoscentercrm.Activity.PanCardActivity;
import com.vigyos.vigyoscentercrm.Activity.SeeMoreServicesActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Activity.SubCatServiceActivity;
import com.vigyos.vigyoscentercrm.Activity.UserActivity;
import com.vigyos.vigyoscentercrm.Activity.WalletActivity;
import com.vigyos.vigyoscentercrm.Adapter.BannerListAdapter;
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

@BuildCompat.PrereleaseSdkCheck
public class HomeFragment extends Fragment implements View.OnClickListener {

    public View view;
    private final Activity activity;
    private TextView userName , amount;
//    private CircleImageView userAccount;
    private RecyclerView bannerRecyclerView;
    private RelativeLayout notification;
    private LinearLayout walletView;
//    private LinearLayout seeMore;
//    private LinearLayout taxService, dscService, itrService;
//    private LinearLayout eWay, accounting, iec;
//    private LinearLayout ipr;
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
//        userAccount = view.findViewById(R.id.profile_image);
        notification = view.findViewById(R.id.notification);
        amount = view.findViewById(R.id.amount);
        bannerRecyclerView = view.findViewById(R.id.bannerRecyclerView);
        walletView = view.findViewById(R.id.walletView);
        aeps = view.findViewById(R.id.aeps);
        panCard = view.findViewById(R.id.panService);
        gst = view.findViewById(R.id.gst);
        udyam = view.findViewById(R.id.udyam);
//        taxService = view.findViewById(R.id.taxService);
//        dscService = view.findViewById(R.id.dscService);
//        itrService = view.findViewById(R.id.itrService);
//        eWay = view.findViewById(R.id.eWay);
//        accounting = view.findViewById(R.id.accounting);
//        iec = view.findViewById(R.id.iec);
//        ipr = view.findViewById(R.id.ipr);
//        seeMore = view.findViewById(R.id.seeMore);
    }

    private void declaration(){
        //SetListeners
//        userAccount.setOnClickListener(this);
        walletView.setOnClickListener(this);
        notification.setOnClickListener(this);
        aeps.setOnClickListener(this);
        panCard.setOnClickListener(this);
        gst.setOnClickListener(this);
        udyam.setOnClickListener(this);
//        taxService.setOnClickListener(this);
//        dscService.setOnClickListener(this);
//        itrService.setOnClickListener(this);
//        eWay.setOnClickListener(this);
//        accounting.setOnClickListener(this);
//        iec.setOnClickListener(this);
//        ipr.setOnClickListener(this);
//        seeMore.setOnClickListener(this);

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

//        if (SplashActivity.prefManager.getProfilePicture().equalsIgnoreCase("null") || SplashActivity.prefManager.getProfilePicture().equalsIgnoreCase("")) {
//            userAccount.setBackgroundResource(R.drawable.user_icon);
//        } else {
//            Picasso.get().load(SplashActivity.prefManager.getProfilePicture()).into(userAccount);
//        }

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
        //Welcome Vigyos Banner
//        arrayList.add("https://vigyos-upload-files.s3.amazonaws.com/d57fb922-5b7d-401e-83db-a0d3041fb05a");
        arrayList.add("https://vigyos-upload-files.s3.amazonaws.com/0f90ae5e-d3b8-4599-a630-e2570f47fed5");
        arrayList.add("https://vigyos-upload-files.s3.amazonaws.com/85490ded-cf05-471a-874c-81b444c75d68");

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
//        switch (v.getId()){
//            case R.id.seeMore:
//                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
//                startActivity(new Intent(activity, SeeMoreServicesActivity.class));
//                break;
//            case R.id.ipr:
//                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
//                Intent intent8 = new Intent(activity, SubCatServiceActivity.class);
//                intent8.putExtra("serviceName", "Intellectual Properties");
//                intent8.putExtra("serviceID", "1011");
//                startActivity(intent8);
//                break;
//            case R.id.iec:
//                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
//                Intent intent7 = new Intent(activity, SubCatServiceActivity.class);
//                intent7.putExtra("serviceName", "IEC – Import/Export Code");
//                intent7.putExtra("serviceID", "1015");
//                startActivity(intent7);
//                break;
//            case R.id.accounting:
//                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
//                Intent intent6 = new Intent(activity, SubCatServiceActivity.class);
//                intent6.putExtra("serviceName", "Accounting");
//                intent6.putExtra("serviceID", "1020");
//                startActivity(intent6);
//                break;
//            case R.id.eWay:
//                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
//                Intent intent5 = new Intent(activity, SubCatServiceActivity.class);
//                intent5.putExtra("serviceName", "E-Way Bills");
//                intent5.putExtra("serviceID", "1017");
//                startActivity(intent5);
//                break;
//            case R.id.itrService:
//                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
//                Intent intent4 = new Intent(activity, SubCatServiceActivity.class);
//                intent4.putExtra("serviceName", "Income Tax Returns");
//                intent4.putExtra("serviceID", "1012");
//                startActivity(intent4);
//                break;
//            case R.id.dscService:
//                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
//                Intent intent3 = new Intent(activity, SubCatServiceActivity.class);
//                intent3.putExtra("serviceName", "DSC Services");
//                intent3.putExtra("serviceID", "1013");
//                startActivity(intent3);
//                break;
//            case R.id.taxService:
//                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
//                Intent intent2 = new Intent(activity, SubCatServiceActivity.class);
//                intent2.putExtra("serviceName", "Tax Services");
//                intent2.putExtra("serviceID", "1018");
//                startActivity(intent2);
//                break;
//            case R.id.udyam:
//                Intent intent1 = new Intent(activity, SubCatServiceActivity.class);
//                intent1.putExtra("serviceName", "Registrations");
//                intent1.putExtra("serviceID", "1014");
//                startActivity(intent1);
//                break;
//            case R.id.gst:
//                Intent intent = new Intent(activity, SubCatServiceActivity.class);
//                intent.putExtra("serviceName", "GST");
//                intent.putExtra("serviceID", "1011");
//                startActivity(intent);
//                break;
//            case R.id.panService:
//                startActivity(new Intent(activity, PanCardActivity.class));
//                break;
//            case R.id.aeps:
////                registerDialog();
//                if (!SplashActivity.prefManager.getBankVerified().equalsIgnoreCase("APPROVED")) {
//                    registerDialog();
//                } else {
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    try {
//                        Date loginDate = sdf.parse(formatTimestamp(SplashActivity.prefManager.getLastVerifyTimeStampAeps()));
//                        Calendar loginCalendar = Calendar.getInstance();
//                        loginCalendar.setTime(loginDate);
//
//                        Calendar currentCalendar = Calendar.getInstance();
//                        long diffInMillis = currentCalendar.getTimeInMillis() - loginCalendar.getTimeInMillis();
//                        long diffInHours = diffInMillis / (60 * 60 * 1000);
//
//                        if(diffInHours >= 24) {
//                            // More than 24 hours have passed since the login
//                            // Prompt user to log in again
//                            Log.i("741258","More than 24 hours");
//                            if (checkPermission()) {
//                                try {
//                                    String pidOption = getPIDOptions();
//                                    if (pidOption != null) {
//                                        Log.e("PidOptions", pidOption);
//                                        Intent intent9 = new Intent();
//                                        intent9.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
//                                        intent9.putExtra("PID_OPTIONS", pidOption);
//                                        startActivityForResult(intent9, 1);
//                                    } else {
//                                        Log.i("454545","Device not found!");
//                                    }
//                                } catch (Exception e) {
//                                    Log.e("Error", e.toString());
//                                    Toast.makeText(activity, "Device not found!", Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                requestPermissions();
//                            }
//                        } else {
//                            // Less than 24 hours have passed since the login
//                            // User is still logged in
//                            Log.i("741258","Less than 24 hours");
//                            startActivity(new Intent(activity, AEPSActivity.class));
//                        }
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
//            case R.id.notification:
//                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
//                startActivity(new Intent(activity, NotificationActivity.class));
//                break;
//            case R.id.profile_image:
//                startActivity(new Intent(activity, UserActivity.class));
//                break;
//            case R.id.walletView:
//                startActivity(new Intent(activity, WalletActivity.class));
//                break;
//            default:
//                break;
//        }
    }

    private void registerDialog() {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_user_message);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setLayout(-1, -1);
        TextView enable = dialog.findViewById(R.id.enable);
        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                v.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
                if (checkPermission()) {
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
                    requestPermissions();
                }
            }
        });
        dialog.show();
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
        } else {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    if (data != null) {
                        String result = data.getStringExtra("PID_DATA");
                        if (result != null) {
                            pidData = serializer.read(PidData.class, result);
                            if (!pidData._Resp.errCode.equals("0")) {
                                Toast.makeText(activity, "Device Not Found!", Toast.LENGTH_SHORT).show();
                            } else {
                                bankRegistration(result);
                                Log.i("78954", "case 2  result if : - " + pidData.toString());
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.i("78954", "Error while deserialize pid data " + e);
                    Toast.makeText(activity, "Failed to scan finger print", Toast.LENGTH_SHORT).show();
                }
            }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withActivity(activity)
                    .withPermissions(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.POST_NOTIFICATIONS
                    )
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                // All permissions are granted. Do your work here.
                                Log.i("2012","Permission Granted!");
                                getLocation();
                            } else if (report.isAnyPermissionPermanentlyDenied()) {
                                // Handle the permanent denial of any permission
                                showSettingsDialog();
                            } else {
                                // Handle the temporary denial of any permission
                                showPermissionDeniedDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            // Handle permission rationale. Show a dialog explaining why the permission is needed
                            showPermissionRationaleDialog(token);
                        }
                    })
                    .check();
        } else {
            Dexter.withActivity(activity)
                    .withPermissions(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                // All permissions are granted. Do your work here.
                                Log.i("2012","Permission Granted!");
                                getLocation();
                            } else if (report.isAnyPermissionPermanentlyDenied()) {
                                // Handle the permanent denial of any permission
                                showSettingsDialog();
                            } else {
                                // Handle the temporary denial of any permission
                                showPermissionDeniedDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            // Handle permission rationale. Show a dialog explaining why the permission is needed
                            showPermissionRationaleDialog(token);
                        }
                    })
                    .check();
        }
    }

    private void showSettingsDialog() {
        new AlertDialog.Builder(activity)
                .setTitle("Need Permissions")
                .setMessage("This app needs permission to use this feature. You can grant them in app settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, 101);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(activity)
                .setTitle("Permission Denied")
                .setMessage("Permission was denied, but is needed for core functionality.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestPermissions(); // Try to request permissions again
                    }
                })
                .show();
    }

    private void showPermissionRationaleDialog(final PermissionToken token) {
        new AlertDialog.Builder(activity)
                .setTitle("Permission Required")
                .setMessage("This permission is necessary for certain features to function.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        token.continuePermissionRequest();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        token.cancelPermissionRequest();
                    }
                }).show();
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

    private void bankRegistration(String fingerData) {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().bankRegistrationAPI(SplashActivity.prefManager.getToken(), "APP", SplashActivity.prefManager.getAadhaarNumber(), SplashActivity.prefManager.getPhone(),
                String.valueOf(latitude), String.valueOf(longitude), currentDateAndTime, fingerData, ipAddress, "2", SplashActivity.prefManager.getMerchantId());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse "+ response);
                dismissDialog();
                Toast.makeText(activity, "AEPS service enable successfully!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("2016", "onFailure "+ t);
                Toast.makeText(activity, "Failed Authentication", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AuthAPI(String fingerData) {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().AuthAPI(SplashActivity.prefManager.getToken(), "APP", SplashActivity.prefManager.getAadhaarNumber(), SplashActivity.prefManager.getPhone(),
                String.valueOf(latitude), String.valueOf(longitude), currentDateAndTime, fingerData, ipAddress, "2", SplashActivity.prefManager.getMerchantId());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse "+ response);
                dismissDialog();
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

    private void pleaseWait() {
        dialog = new Dialog(activity);
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
}