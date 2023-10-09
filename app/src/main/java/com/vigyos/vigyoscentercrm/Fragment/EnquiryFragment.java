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
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.vigyos.vigyoscentercrm.Activity.LoginActivity;
import com.vigyos.vigyoscentercrm.Activity.ProcessDoneActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.FingerPrintModel.Opts;
import com.vigyos.vigyoscentercrm.FingerPrintModel.PidData;
import com.vigyos.vigyoscentercrm.FingerPrintModel.PidOptions;
import com.vigyos.vigyoscentercrm.Model.BankListModel;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;
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
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, bankListArray); //selected item will look like a spinner set from XML
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

//                                String fingerPrint = "<?xml version=\"1.0\"?>\r\n<PidData>\r\n  <Resp errCode=\"0\" errInfo=\"Success.\" fCount=\"1\" fType=\"2\" nmPoints=\"33\" qScore=\"74\" />\r\n  <DeviceInfo dpId=\"MANTRA.MSIPL\" rdsId=\"MANTRA.WIN.001\" rdsVer=\"1.0.8\" mi=\"MFS100\" mc=\"MIIEGDCCAwCgAwIBAgIEAoDegDANBgkqhkiG9w0BAQsFADCB6jEqMCgGA1UEAxMhRFMgTUFOVFJBIFNPRlRFQ0ggSU5ESUEgUFZUIExURCAzMVUwUwYDVQQzE0xCLTIwMyBTaGFwYXRoIEhleGEgT3Bwb3NpdGUgR3VqYXJhdCBIaWdoIENvdXJ0IFMuRyBIaWdod2F5IEFobWVkYWJhZCAtMzgwMDYwMRIwEAYDVQQJEwlBSE1FREFCQUQxEDAOBgNVBAgTB0dVSkFSQVQxCzAJBgNVBAsTAklUMSUwIwYDVQQKExxNQU5UUkEgU09GVEVDSCBJTkRJQSBQVlQgTFREMQswCQYDVQQGEwJJTjAeFw0yMzEwMDkwNjU3MTRaFw0yMzEwMjkwNzA4NDJaMIGwMSUwIwYDVQQDExxNYW50cmEgU29mdGVjaCBJbmRpYSBQdnQgTHRkMR4wHAYDVQQLExVCaW9tZXRyaWMgTWFudWZhY3R1cmUxDjAMBgNVBAoTBU1TSVBMMRIwEAYDVQQHEwlBSE1FREFCQUQxEDAOBgNVBAgTB0dVSkFSQVQxCzAJBgNVBAYTAklOMSQwIgYJKoZIhvcNAQkBFhVzdXBwb3J0QG1hbnRyYXRlYy5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDbxcCiUP5jBz2UPgoLX6b5xiPaJOEVX3roatokc6WyCF24tqEB7L+tI3FhJ4buI0TI5BiGqflCBCRAhWe54EmH1x/XQM5GvZeJronuIPQZcr9gAMoYIJ4qRTjIqH7qBjd9Yv/niZ+HpCq90JOV9Is/bEvWonIS25WDcHggipNQbXW+a/e2C3WIhAnQxc06e6nazKN1wiweBqELH7C23awPXmOYyHi98yMkoWqIeSD3Xoj/g/OQTXroiuM/nQQArpc4Odhr+FvCFY/tuVOAJ/PDfXztqzyx87CBq+avASN7YcZiOMwnb46ChJwEDnm8uRf5CmNha5I8xLuZ5Gw4mzWnAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAAVJlCW4YR7U9Ou1N9HM6MpqUhbw7ZdZBIZG4EGWtXwYmENrFKuLiR/MmZwObeX9OkqQAnCxWcnG6d7xiAE2EmIfQoCfk8ifgp577AiT3uficJguEFqL14a1iDXYNWbPMNoexIS0WoK1Po+F65pUEZpSwM/RCPEv2NSJ6LK1n/3QeNg00w+K5zE4m6Yz5caw3t5yqSHWB45+3Kv2rmtEnR5DEL3nzdo31mTt3kU2qbJHMo7V/w0ljwLd1qivhynmz5E8dN5uKUfc59anNkFK1HtatJdlOUZazIF12D8dl+ynuhHHn6MM+jF+bjgp2bZRJus6Ynaob0H3ANU8cGQV4k0=\" dc=\"b22eb081-2c82-489b-b7fe-76612c89473b\">\r\n    <additional_info>\r\n      <Param name=\"srno\" value=\"6456562\" />\r\n      <Param name=\"sysid\" value=\"6ECFEBF9E9A3658FBFF0\" />\r\n      <Param name=\"ts\" value=\"2023-10-09T15:21:25+05:30\" />\r\n    </additional_info>\r\n  </DeviceInfo>\r\n  <Skey ci=\"20250923\">mVpH2yvvWPjHSvs+PrvOOYL+dces/nRvndzuynKx6N2KGWltepk9eRJnw9yWlUDJYj0yAQuZo+DwKV5zTjzr4PgKzqxO0t9dQMMNiLTGIto1/dzpxSoQgk8Szp07cx3p5qaf+Uy0VqD+ld8cjIU5k4L4dO938unxdjGPCUca77f8ypKJtsFrW2jTuXZx8T4Hi6Ni6Rbml1NuknKG2rKcv5XT3swPu2KpsL7XM7y3uEtgx1vgKvCRE4tprgOMvV29y4+C92HO81elZ/kyW84NBaXIo2RNNO/6BfLWx+D5Mv+jyV2IUEf1rFzYfxakrybwgWeBmHDF452hg2yQp5R73w==</Skey>\r\n  <Hmac>qoenItDef+DK3GSP+W1WEW6EVtBcah9Nunckl6qrz2QhxHXKH0gfv4E563PH7VR0</Hmac>\r\n  <Data type=\"X\">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</Data>\r\n</PidData>";

                                fingerData = result;
                                fingerCapture = true;
                                fingerPrintDone.setVisibility(View.VISIBLE);
                                dismissDialog();
                                Log.i("78954","pidData " + result);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Error while deserialze pid data", e);
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