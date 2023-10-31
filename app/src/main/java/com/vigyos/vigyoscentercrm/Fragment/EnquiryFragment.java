package com.vigyos.vigyoscentercrm.Fragment;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;

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
import com.vigyos.vigyoscentercrm.Activity.ProcessDoneActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.FingerPrintModel.Opts;
import com.vigyos.vigyoscentercrm.FingerPrintModel.PidData;
import com.vigyos.vigyoscentercrm.FingerPrintModel.PidOptions;
import com.vigyos.vigyoscentercrm.Model.BankListModel;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class EnquiryFragment extends Fragment {

    private View view;
    private Spinner spinner;
    private final Activity activity;
    private EditText aadhaar_num, mobile_number, remark;
    private RelativeLayout button_done;
    private CardView captureFingerPrint;
    private ArrayList<BankListModel> bankListModels = new ArrayList<>();
    private ArrayList<String> bankListArray = new ArrayList<>();
    private String bankName;
    private int iinno;
    private Dialog dialog;
    private String ipAddress;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude;
    private double longitude;
    private String currentDateAndTime;
    public PidData pidData = null;
    private String fingerData = null;
    private Serializer serializer = null;
    public ArrayList<String> positions;
    private boolean fingerCapture = false;
    private ImageView fingerPrintDone;

    public EnquiryFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_enquiry, container, false);
        initialization();
        declaration();
        bankList();
        return view;
    }

    private void initialization() {
        aadhaar_num = view.findViewById(R.id.aadhaarNumber);
        spinner = view.findViewById(R.id.bankName);
        mobile_number = view.findViewById(R.id.mobileNumber);
        remark = view.findViewById(R.id.remarkEnquiry);
        captureFingerPrint = view.findViewById(R.id.captureFingerPrint);
        fingerPrintDone = view.findViewById(R.id.captureData);
        button_done = view.findViewById(R.id.buttonDone);
    }

    private void declaration() {
        serializer = new Persister();
        positions = new ArrayList<>();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        captureFingerPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(aadhaar_num.getText().toString())  ){
                    aadhaar_num.setError("This field is required");
                    Toast.makeText(activity, "Enter Aadhaar number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!isValidAadhaarNumber(aadhaar_num.getText().toString())){
                        aadhaar_num.setError("Enter a valid Aadhaar number");
                        Toast.makeText(activity, "Enter a valid Aadhaar number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (spinner.getSelectedItem().toString().trim().equals("Select your bank")) {
                    Toast.makeText(activity,"Select your bank",Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((TextUtils.isEmpty(mobile_number.getText().toString())) ) {
                    mobile_number.setError("This field is required");
                    Toast.makeText(activity, "Enter a Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!isValidPhone(mobile_number.getText().toString())){
                        mobile_number.setError("Invalid Mobile Number");
                        Toast.makeText(activity, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                aadhaar_num.clearFocus();
                mobile_number.clearFocus();
                remark.clearFocus();

                scanFingerPrint();
            }
        });
        button_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(aadhaar_num.getText().toString())  ){
                    aadhaar_num.setError("This field is required");
                    Toast.makeText(activity, "Enter Aadhaar number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!isValidAadhaarNumber(aadhaar_num.getText().toString())){
                        aadhaar_num.setError("Enter a valid Aadhaar number");
                        Toast.makeText(activity, "Enter a valid Aadhaar number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (spinner.getSelectedItem().toString().trim().equals("Select your bank")) {
                    Toast.makeText(activity,"Select your bank",Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((TextUtils.isEmpty(mobile_number.getText().toString())) ) {
                    mobile_number.setError("This field is required");
                    Toast.makeText(activity, "Enter a Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!isValidPhone(mobile_number.getText().toString())){
                        mobile_number.setError("Invalid Mobile Number");
                        Toast.makeText(activity, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                mobile_number.clearFocus();
                remark.clearFocus();
                aadhaar_num.clearFocus();

                if(fingerCapture){
                    if (fingerData != null){
                        enquiry(aadhaar_num.getText().toString(), currentDateAndTime, fingerData, iinno, remark.getText().toString(), mobile_number.getText().toString() );
                    } else {
                        Toast.makeText(activity, "FingerPrint Data Null", Toast.LENGTH_SHORT).show();
                    }
                    fingerCapture = false;
                    fingerPrintDone.setVisibility(View.GONE);
                } else {
                    Toast.makeText(activity, "Capture FingerPrint", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void scanFingerPrint(){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_fingerprint_scan);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RelativeLayout cancelDialog = dialog.findViewById(R.id.cancelDialog);
        RelativeLayout scanFingerPrint = dialog.findViewById(R.id.scanFingerPrint);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
        scanFingerPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Toast.makeText(activity, "Device not found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    public boolean isValidPhone(String num) {
        Pattern ptrn = Pattern.compile("[6-9][0-9]{9}");
        Matcher match = ptrn.matcher(num);
        return (match.find() && match.group().equals(num));
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
        Call<Object> objectCall = RetrofitClient.getApi().bankList(SplashActivity.prefManager.getToken());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("123345","onResponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("banklist");
                        JSONArray jsonArray = jsonObject1.getJSONArray("data");
                        bankListArray.add(0, "Select your bank");
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
                                bankListArray.add(jsonObject2.getString("bankName"));
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
                    } else {
                        if (jsonObject.has("message")){
                            Snackbar.make(activity.findViewById(android.R.id.content), jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();
                        }
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, bankListArray); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(spinnerArrayAdapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (parent.getItemAtPosition(position).equals("Select your bank")) {
                                Log.i("12121","Select your bank");
                            } else {
                                String selectedItem = (String) parent.getItemAtPosition(position);
                                for (BankListModel bankListModel: bankListModels){
                                    if(bankListModel.getBankName().equalsIgnoreCase(selectedItem)){
                                        bankName = bankListModel.getBankName();
                                        iinno  = bankListModel.getIinno();
                                        Log.i("789654", "Bank Name: - " + bankName + " " + iinno);
                                    }
                                }
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) { }
                    });
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

    private void enquiry(String aadhaarNumber, String timeStamp, String fingerData, int nationalbankidentification, String requestremarks , String mobile) {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().enquiry(SplashActivity.prefManager.getToken(), "APP", aadhaarNumber, mobile,
                String.valueOf(latitude), String.valueOf(longitude), timeStamp, fingerData, ipAddress, "bank2", SplashActivity.prefManager.getMerchantId(), String.valueOf(nationalbankidentification), requestremarks, "BE");
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("2016", "onResponse " + response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if(jsonObject.has("status") && jsonObject.getBoolean("status")) {
                        String message = jsonObject.getString("message");
                        String ackno = jsonObject.getString("ackno");
//                            String amount = jsonObject.getString("amount");
                        String balanceamount = jsonObject.getString("balanceamount");
                        String bankrrn = jsonObject.getString("bankrrn");
                        String bankiin = jsonObject.getString("bankiin");
                        String clientrefno = jsonObject.getString("clientrefno");

                        Intent intent = new Intent(activity, ProcessDoneActivity.class);
                        intent.putExtra("fragmentName", "Enquiry");
                        intent.putExtra("messageStatus", "Enquiry Successful!");
                        intent.putExtra("message", message);
                        intent.putExtra("bankName", bankName);
                        intent.putExtra("ackno", ackno);
                        intent.putExtra("amount", "amount");
                        intent.putExtra("balanceamount", balanceamount);
                        intent.putExtra("aadhaarNumber", aadhaarNumber);
                        intent.putExtra("bankrrn", bankrrn);
                        intent.putExtra("bankiin", bankiin);
                        intent.putExtra("clientrefno", clientrefno);
                        startActivity(intent);
                        activity.finish();
                    } else {
                        if (jsonObject.has("message")){
                            Snackbar.make(activity.findViewById(android.R.id.content), jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();
                            fingerCapture = false;
                            fingerPrintDone.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("2016", "onFailure " + t);
                fingerCapture = false;
                fingerPrintDone.setVisibility(View.GONE);
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
                                if (!pidData._Resp.errCode.equals("0")) {
                                    Toast.makeText(activity, "Device Not Found!", Toast.LENGTH_SHORT).show();
                                } else {
                                    fingerData = result;
                                    fingerCapture = true;
                                    fingerPrintDone.setVisibility(View.VISIBLE);
                                    Log.i("78954","pidData " + result);
                                }
                                dismissDialog();
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Error while deserialize pid data", e);
                        Toast.makeText(activity, "Failed to Capture FingerPrint", Toast.LENGTH_SHORT).show();
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
}