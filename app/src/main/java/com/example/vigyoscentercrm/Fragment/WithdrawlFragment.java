package com.example.vigyoscentercrm.Fragment;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
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
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.vigyoscentercrm.Activity.SplashActivity;
import com.example.vigyoscentercrm.Model.BankListModel;
import com.example.vigyoscentercrm.R;
import com.example.vigyoscentercrm.Retrofit.RetrofitClient;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawlFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private View view;
    private Spinner spinner;
    private Activity activity;
    private EditText aadhaar_num,amount;
    private RelativeLayout button_done;
    private ArrayList<BankListModel> bankListModels = new ArrayList<>();
    private ArrayList<String> backListArray = new ArrayList<>();
    private Dialog dialog;
    private String ipAddress;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude;
    private double longitude;
    private String currentDate;
    private String currentTime;

    public WithdrawlFragment(){ }

    public WithdrawlFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_withdrawl, container, false);
       initialization();
       declaration();
       bankList();
       return view;
    }

    private void initialization() {
        spinner = view.findViewById(R.id.bank_name);
        aadhaar_num = view.findViewById(R.id.aadhaar_number);
        button_done = view.findViewById(R.id.button_done);
        amount = view.findViewById(R.id.amount);
    }

    private void declaration() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        button_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(aadhaar_num.getText().toString())||!isValidAadhaarNumber(aadhaar_num.getText().toString())){
                    aadhaar_num.setError("Please enter a Valid number");
                    return;
                }
                if(TextUtils.isEmpty(amount.getText().toString())||!isNumeric(amount.getText().toString())){
                    amount.setError("Please enter a valid amount");
                    return;
                }
                if (spinner.getSelectedItem().toString().trim().equals("--Select your Bank--")) {
                    Toast.makeText(activity,"Please select your bank",Toast.LENGTH_SHORT).show();
                    return;
                }
                String timeAndDate = currentTime +" "+currentDate;
                String fingerData = "fingerData";
                withdrawal(aadhaar_num.getText().toString(),timeAndDate, fingerData, "10101", "remarks", "0.0");
            }
        });
    }

    public static boolean isNumeric(String strNum){
        double amt = Double.parseDouble(strNum);
        return !(amt <= 0);
    }

    public static boolean isValidAadhaarNumber(String str) {
        String regex = "^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$";
        Log.d("790585", "isValidAadhaarNumber: "+ regex);
        Pattern p = Pattern.compile(regex);
        if (str == null) {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.matches();
    }

    private void bankList(){
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().bankList("Bearer "+ SplashActivity.prefManager.getToken());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("123345","onResponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.getString("status").equalsIgnoreCase("true")){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("banklist");
                        JSONArray jsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            BankListModel bankListModel = new BankListModel();
                            if (jsonObject2.has("id")){
                                bankListModel.setId(jsonObject2.getInt("id"));
                            } else {
                                bankListModel.setId(0);
                            }
                            if(jsonObject2.has("bankName")){
                                bankListModel.setBankName(jsonObject2.getString("bankName"));
                                backListArray.add(jsonObject2.getString("bankName"));
                            } else {
                                bankListModel.setBankName("Bank Name");
                            }
                            if (jsonObject2.has("iinno")){
                                bankListModel.setIinno(jsonObject2.getInt("iinno"));
                            } else {
                                bankListModel.setIinno(0);
                            }
                            if (jsonObject2.has("activeFlag")){
                                bankListModel.setActiveFlag(jsonObject2.getString("activeFlag"));
                            } else {
                                bankListModel.setActiveFlag("0");
                            }
                            if (jsonObject2.has("aadharpayiinno")){
                                bankListModel.setAadharpayiinno(jsonObject2.getString("aadharpayiinno"));
                            } else {
                                bankListModel.setAadharpayiinno("aadharpayiinno");
                            }
                            bankListModels.add(bankListModel);
                        }
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, backListArray); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(spinnerArrayAdapter);
                    spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) activity);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                dateAndTime();
                iPAddress();
                if (checkPermission()){
                    getLocation();
                } else {
                    requestPermissions();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("123345","onFailure");

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Log.i("1212","text" +text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void withdrawal(String aadhaarNumber, String timeStamp, String fingerData, String nationalbankidentification, String requestremarks , String amount){
        Call<Object> objectCall = RetrofitClient.getApi().withdrawal("Bearer "+SplashActivity.prefManager.getToken(), "APP", aadhaarNumber, SplashActivity.prefManager.getPhone(),
                String.valueOf(latitude), String.valueOf(longitude), timeStamp, fingerData, ipAddress, "bank2", "submerchantid", nationalbankidentification, requestremarks, "CW", amount);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

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

    private void dateAndTime(){
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
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
}