package com.vigyos.vigyoscentercrm.Fragment;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vigyos.vigyoscentercrm.Activity.AEPSActivity;
import com.vigyos.vigyoscentercrm.Activity.PanCardActivity;
import com.vigyos.vigyoscentercrm.Activity.SearchServicesActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Activity.SubCatServiceActivity;
import com.vigyos.vigyoscentercrm.Adapter.BannerListAdapter;
import com.vigyos.vigyoscentercrm.FingerPrintModel.Opts;
import com.vigyos.vigyoscentercrm.FingerPrintModel.PidData;
import com.vigyos.vigyoscentercrm.FingerPrintModel.PidOptions;
import com.vigyos.vigyoscentercrm.Model.BannerListModel;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {

    public View view;
    private Activity activity;
    private static final String TAG = "HomeFragment";
    private TextView userName , amount;
    private CircleImageView userAccount;
    private RecyclerView bannerRecyclerView;
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
        amount = view.findViewById(R.id.amount);
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
                replaceFragment(new SeeMoreServicesFragment());
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
                startActivity(new Intent(activity, AEPSActivity.class));
//                try {
//                    String pidOption = getPIDOptions();
//                    if (pidOption != null) {
//                        Log.e("PidOptions", pidOption);
//                        Intent intent2 = new Intent();
//                        intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
//                        intent2.putExtra("PID_OPTIONS", pidOption);
//                        startActivityForResult(intent2, 2);
//                    }
//                } catch (Exception e) {
//                    Log.e("Error", e.toString());
//                    Toast.makeText(activity, "Device not found!", Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.profile_image:
                replaceFragment(new UserFragment());
                break;
            case R.id.walletView:
                replaceFragment(new WalletFragment());
                break;
            default:
                break;
        }
    }

    private String getPIDOptions() {
        try {
            String posh = "UNKNOWN";
            if (positions.size() > 0) {
                posh = positions.toString().replace("[", "").replace("]", "").replaceAll("[\\s+]", "");
            }
            Opts opts = new Opts();
            opts.fCount = "1";
            opts.fType = "0";
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
//        super.onActivityResult(requestCode, resultCode, data);
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
                        Log.e("Error", "Error while deserialze device info", e);
                    }
                }
                break;
            case 2:
                Log.i("78954", "case 2 ");
                if (resultCode == Activity.RESULT_OK) {
                    Log.i("78954", "case 2  if");
                    try {
                        Log.i("78954", "case 2  try");
                        if (data != null) {
                            Log.i("78954", "case 2  data if");
                            String result = data.getStringExtra("PID_DATA");
                            if (result != null) {
                                pidData = serializer.read(PidData.class, result);
//                                remark_heading.setText(result);
                                AuthAPI(result);
                                Log.i("78954", "case 2  result if");
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Error while deserialze pid data", e);
                        Log.i("78954", "case 2  catch");
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
        Call<Object> objectCall = RetrofitClient.getApi().AuthAPI(SplashActivity.prefManager.getToken(), "APP", SplashActivity.prefManager.getAadhaarNumber(), SplashActivity.prefManager.getPhone(),
                String.valueOf(latitude), String.valueOf(longitude), currentDateAndTime, fingerData, ipAddress, "2", SplashActivity.prefManager.getMerchantId());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse "+ response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("status")){
                        if (jsonObject.getString("status").equalsIgnoreCase("true")){
                            String message = jsonObject.getString("message");
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2016", "onFailure "+ t);
                Toast.makeText(activity, "Failed ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment); // R.id.fragment_container is the container in your activity layout where fragments are placed
        fragmentTransaction.addToBackStack(null); // This allows the user to navigate back to FragmentA when they press the back button
        fragmentTransaction.commit();
    }
}