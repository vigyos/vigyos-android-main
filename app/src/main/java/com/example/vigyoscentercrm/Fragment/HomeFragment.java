package com.example.vigyoscentercrm.Fragment;

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
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.vigyoscentercrm.Activity.AEPSActivity;
import com.example.vigyoscentercrm.Activity.SearchServicesActivity;
import com.example.vigyoscentercrm.Activity.ShowTopServiceActivity;
import com.example.vigyoscentercrm.Activity.SplashActivity;
import com.example.vigyoscentercrm.FingerPrintModel.Opts;
import com.example.vigyoscentercrm.FingerPrintModel.PidData;
import com.example.vigyoscentercrm.FingerPrintModel.PidOptions;
import com.example.vigyoscentercrm.R;
import com.example.vigyoscentercrm.Retrofit.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {

    public View view;
    private Activity activity;
    private TextView userName;
    private ImageView userAccount;
    private ImageSlider imageSlider;
    private LinearLayout walletView;
    private LinearLayout seeMore;
    private LinearLayout gstRegistration, tdsReturn;
    private LinearLayout eWay, itr1;
    private LinearLayout udyamRegistration;
    private LinearLayout aeps;
    private String ipAddress;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude;
    private double longitude;
    private String currentDateAndTime;
    public PidData pidData = null;
    private Serializer serializer = null;
    public ArrayList<String> positions;

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
        imageSlider = view.findViewById(R.id.image_slider);
        walletView = view.findViewById(R.id.walletView);
        gstRegistration = view.findViewById(R.id.gstRegistration);
        tdsReturn = view.findViewById(R.id.tdsReturn);
        eWay = view.findViewById(R.id.eWay);
        itr1 = view.findViewById(R.id.itr1);
        udyamRegistration = view.findViewById(R.id.udyamRegistration);
        seeMore = view.findViewById(R.id.seeMore);
        aeps = view.findViewById(R.id.aeps);
    }

    private void declaration(){
        //SetListeners
        userAccount.setOnClickListener(this);
        imageSlider.setOnClickListener(this);
        walletView.setOnClickListener(this);
        gstRegistration.setOnClickListener(this);
        tdsReturn.setOnClickListener(this);
        eWay.setOnClickListener(this);
        itr1.setOnClickListener(this);
        udyamRegistration.setOnClickListener(this);
        seeMore.setOnClickListener(this);
        aeps.setOnClickListener(this);

        if (checkPermission()){
            getLocation();
        } else {
            requestPermissions();
        }
        dateAndTime();
        iPAddress();

        serializer = new Persister();
        positions = new ArrayList<>();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

        userName.setText(SplashActivity.prefManager.getFirstName()+" "+SplashActivity.prefManager.getLastName());
        ArrayList<SlideModel> img = new ArrayList<>();
        img.add(new SlideModel(R.drawable.banner_1,  ScaleTypes.CENTER_INSIDE));
        img.add(new SlideModel(R.drawable.banner_2, ScaleTypes.FIT));
        img.add(new SlideModel(R.drawable.banner_3, ScaleTypes.CENTER_INSIDE));
        imageSlider.setImageList(img);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.aeps:
                try {
                    String pidOption = getPIDOptions();
                    if (pidOption != null) {
                        Log.e("PidOptions", pidOption);
                        Intent intent2 = new Intent();
                        intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                        intent2.putExtra("PID_OPTIONS", pidOption);
                        startActivityForResult(intent2, 2);
                    }
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }

//                startActivity(new Intent(getActivity(), AEPSActivity.class));
                break;
            case R.id.profile_image:
                replaceFragment(new UserFragment());
                break;
            case R.id.walletView:
                replaceFragment(new WalletFragment());
                break;
            case R.id.gstRegistration:
                Intent intent = new Intent(getActivity(), ShowTopServiceActivity.class);
                intent.putExtra("name", getString(R.string.gst_service));
                intent.putExtra("Description", getString(R.string.gst_Description));
                intent.putExtra("price", getString(R.string.gst_price));
                intent.putExtra("RequiredDocument", getString(R.string.gst_Required_Document));
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.tdsReturn:
                Intent intent1 = new Intent(getActivity(), ShowTopServiceActivity.class);
                intent1.putExtra("name", getString(R.string.TDS_Return));
                intent1.putExtra("Description", getString(R.string.TDS_Description));
                intent1.putExtra("price", getString(R.string.TDS_Price));
                intent1.putExtra("RequiredDocument", getString(R.string.TDS_RequiredDocument));
                startActivity(intent1);
                getActivity().finish();
                break;
            case R.id.eWay:
                Intent intent2 = new Intent(getActivity(), ShowTopServiceActivity.class);
                intent2.putExtra("name", getString(R.string.Eway));
                intent2.putExtra("Description", getString(R.string.Eway_Description));
                intent2.putExtra("price", getString(R.string.Eway_Price));
                intent2.putExtra("RequiredDocument", getString(R.string.Eway_RequiredDocument));
                startActivity(intent2);
                getActivity().finish();
                break;
            case R.id.itr1:
                Intent intent3 = new Intent(getActivity(), ShowTopServiceActivity.class);
                intent3.putExtra("name", getString(R.string.ITR1));
                intent3.putExtra("Description", getString(R.string.ITR1_Description));
                intent3.putExtra("price", getString(R.string.ITR1_Price));
                intent3.putExtra("RequiredDocument", getString(R.string.TDS_RequiredDocument));
                startActivity(intent3);
                getActivity().finish();
                break;
            case R.id.udyamRegistration:
                Intent intent4 = new Intent(getActivity(), ShowTopServiceActivity.class);
                intent4.putExtra("name", getString(R.string.Udyam_Registration));
                intent4.putExtra("Description", getString(R.string.Udyam_RegistrationDescription));
                intent4.putExtra("price", getString(R.string.Udyam_RegistrationPrice));
                intent4.putExtra("RequiredDocument", getString(R.string.Udyam_RegistrationRequiredDocument));
                startActivity(intent4);
                getActivity().finish();
                break;
            case R.id.seeMore:
                startActivity(new Intent(getActivity(), SearchServicesActivity.class));
                getActivity().finish();
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
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        if (data != null) {
                            String result = data.getStringExtra("PID_DATA");
                            if (result != null) {
                                pidData = serializer.read(PidData.class, result);
//                                remark_heading.setText(result);
                                AuthAPI(result);
                                Log.i("78954", "pidData " + result);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Error while deserialze pid data", e);
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
        Call<Object> objectCall = RetrofitClient.getApi().AuthAPI("Bearer " + SplashActivity.prefManager.getToken(), "APP", SplashActivity.prefManager.getAadhaarNumber(), SplashActivity.prefManager.getPhone(),
                String.valueOf(latitude), String.valueOf(longitude), currentDateAndTime, fingerData, ipAddress, "2", SplashActivity.prefManager.getMerchantId());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse "+ response);
                Toast.makeText(activity, "Authenticated Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2016", "onFailure "+ t);
                Toast.makeText(activity, "Failed Successfully", Toast.LENGTH_SHORT).show();

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