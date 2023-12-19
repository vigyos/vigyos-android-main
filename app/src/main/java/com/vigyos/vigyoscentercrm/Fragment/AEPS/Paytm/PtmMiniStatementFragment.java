package com.vigyos.vigyoscentercrm.Fragment.AEPS.Paytm;

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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.vigyos.vigyoscentercrm.Activity.AEPS.MiniStatementDoneActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Constant.DialogCustom;
import com.vigyos.vigyoscentercrm.FingerPrintModel.Opts;
import com.vigyos.vigyoscentercrm.FingerPrintModel.PidData;
import com.vigyos.vigyoscentercrm.FingerPrintModel.PidOptions;
import com.vigyos.vigyoscentercrm.Model.BankListModel;
import com.vigyos.vigyoscentercrm.Model.MiniStatementModel;
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

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class PtmMiniStatementFragment extends Fragment {

    private View view;
    private LinearLayout ptmAadhaarNumberFocus, ptmBankNameFocus;
    private LinearLayout ptmMobileNumberFocus, ptmRemarkFocus;
    private LinearLayout ptmCaptureFingerPrintMiniFocus;
    private RelativeLayout ptmCaptureFingerPrintMiniLyt;
    private RelativeLayout ptmAadhaarNumberLyt, ptmBankNameLyt;
    private RelativeLayout ptmMobileNumberLyt, ptmRemarkLyt;
    private EditText ptmAadhaarNumberMini, ptmMobileNumberMini;
    private EditText ptmRemarkMini;
    private Spinner ptmBankNameMini;
    private TextView captureFingerPrintMiniText;
    private RelativeLayout ptmProcess;
    private final Activity activity;
    private ArrayList<BankListModel> bankListModels = new ArrayList<>();
    private ArrayList<String> bankListArray = new ArrayList<>();
    private ArrayList<MiniStatementModel> miniStatementModels = new ArrayList<>();
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
    private ImageView ptmFingerPrintDone;
    private Animation animation;

    public PtmMiniStatementFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ptm_mini_statement, container, false);
        initialization();
        bankList();
        declaration();
        return view;
    }

    private void initialization() {
        ptmAadhaarNumberFocus = view.findViewById(R.id.ptmAadhaarNumberFocus);
        ptmBankNameFocus = view.findViewById(R.id.ptmBankNameFocus);
        ptmMobileNumberFocus = view.findViewById(R.id.ptmMobileNumberFocus);
        ptmRemarkFocus = view.findViewById(R.id.ptmRemarkFocus);
        ptmAadhaarNumberLyt = view.findViewById(R.id.ptmAadhaarNumberLyt);
        ptmBankNameLyt = view.findViewById(R.id.ptmBankNameLyt);
        ptmMobileNumberLyt = view.findViewById(R.id.ptmMobileNumberLyt);
        ptmRemarkLyt = view.findViewById(R.id.ptmRemarkLyt);
        ptmAadhaarNumberMini = view.findViewById(R.id.ptmAadhaarNumberMini);
        ptmMobileNumberMini = view.findViewById(R.id.ptmMobileNumberMini);
        ptmRemarkMini = view.findViewById(R.id.ptmRemarkMini);
        ptmBankNameMini = view.findViewById(R.id.ptmBankNameMini);
        ptmProcess = view.findViewById(R.id.ptmProcess);
        ptmCaptureFingerPrintMiniFocus = view.findViewById(R.id.ptmCaptureFingerPrintMiniFocus);
        ptmCaptureFingerPrintMiniLyt = view.findViewById(R.id.ptmCaptureFingerPrintMiniLyt);
        ptmFingerPrintDone = view.findViewById(R.id.ptmCaptureDataMini);
        captureFingerPrintMiniText = view.findViewById(R.id.captureFingerPrintMiniText);
    }

    private void declaration() {
        serializer = new Persister();
        positions = new ArrayList<>();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        animation = AnimationUtils.loadAnimation(activity, R.anim.shake_animation);
        ptmAadhaarNumberMini.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ptmAadhaarNumberLyt.setBackgroundResource(R.drawable.credential_border);
                    ptmMobileNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    ptmRemarkLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        ptmMobileNumberMini.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ptmAadhaarNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    ptmMobileNumberLyt.setBackgroundResource(R.drawable.credential_border);
                    ptmRemarkLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        ptmRemarkMini.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ptmAadhaarNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    ptmMobileNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    ptmRemarkLyt.setBackgroundResource(R.drawable.credential_border);
                }
            }
        });
        ptmCaptureFingerPrintMiniFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(ptmAadhaarNumberMini.getText().toString())  ){
                    ptmAadhaarNumberMini.setError("This field is required");
                    ptmAadhaarNumberMini.requestFocus();
                    ptmAadhaarNumberLyt.startAnimation(animation);
                    ptmAadhaarNumberFocus.getParent().requestChildFocus(ptmAadhaarNumberFocus, ptmAadhaarNumberFocus);
                    StyleableToast.makeText(activity, "Enter Aadhaar number", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                } else {
                    if (!isValidAadhaarNumber(ptmAadhaarNumberMini.getText().toString())){
                        ptmAadhaarNumberMini.setError("Enter a valid Aadhaar number");
                        ptmAadhaarNumberMini.requestFocus();
                        ptmAadhaarNumberLyt.startAnimation(animation);
                        ptmAadhaarNumberFocus.getParent().requestChildFocus(ptmAadhaarNumberFocus, ptmAadhaarNumberFocus);
                        return;
                    }
                }
                if (ptmBankNameMini.getSelectedItem().toString().trim().equals("Select your bank")) {
                    ptmBankNameLyt.startAnimation(animation);
                    ptmBankNameFocus.getParent().requestChildFocus(ptmBankNameFocus, ptmBankNameFocus);
                    StyleableToast.makeText(activity, "Select your bank", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if ((TextUtils.isEmpty(ptmMobileNumberMini.getText().toString())) ) {
                    ptmMobileNumberMini.setError("This field is required");
                    ptmMobileNumberMini.requestFocus();
                    ptmMobileNumberLyt.startAnimation(animation);
                    ptmMobileNumberFocus.getParent().requestChildFocus(ptmMobileNumberFocus, ptmMobileNumberFocus);
                    StyleableToast.makeText(activity, "Enter Mobile Number", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                } else {
                    if (!isValidPhone(ptmMobileNumberMini.getText().toString())){
                        ptmMobileNumberMini.setError("Invalid Mobile Number");
                        ptmMobileNumberMini.requestFocus();
                        ptmMobileNumberLyt.startAnimation(animation);
                        ptmMobileNumberFocus.getParent().requestChildFocus(ptmMobileNumberFocus, ptmMobileNumberFocus);
                        return;
                    }
                }
                if ((TextUtils.isEmpty(ptmRemarkMini.getText().toString())) ) {
                    ptmRemarkMini.setError("This field is required");
                    ptmRemarkMini.requestFocus();
                    ptmRemarkLyt.startAnimation(animation);
                    ptmRemarkFocus.getParent().requestChildFocus(ptmRemarkFocus, ptmRemarkFocus);
                    StyleableToast.makeText(activity, "Enter Remark", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                ptmAadhaarNumberMini.clearFocus();
                ptmMobileNumberMini.clearFocus();
                ptmRemarkMini.clearFocus();

                scanFingerPrint();
            }
        });
        ptmProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(ptmAadhaarNumberMini.getText().toString())  ){
                    ptmAadhaarNumberMini.setError("This field is required");
                    ptmAadhaarNumberMini.requestFocus();
                    ptmAadhaarNumberLyt.startAnimation(animation);
                    ptmAadhaarNumberFocus.getParent().requestChildFocus(ptmAadhaarNumberFocus, ptmAadhaarNumberFocus);
                    StyleableToast.makeText(activity, "Enter Aadhaar number", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                } else {
                    if (!isValidAadhaarNumber(ptmAadhaarNumberMini.getText().toString())){
                        ptmAadhaarNumberMini.setError("Enter a valid Aadhaar number");
                        ptmAadhaarNumberMini.requestFocus();
                        ptmAadhaarNumberLyt.startAnimation(animation);
                        ptmAadhaarNumberFocus.getParent().requestChildFocus(ptmAadhaarNumberFocus, ptmAadhaarNumberFocus);
                        return;
                    }
                }
                if (ptmBankNameMini.getSelectedItem().toString().trim().equals("Select your bank")) {
                    ptmBankNameLyt.startAnimation(animation);
                    ptmBankNameFocus.getParent().requestChildFocus(ptmBankNameFocus, ptmBankNameFocus);
                    StyleableToast.makeText(activity, "Select your bank", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if ((TextUtils.isEmpty(ptmMobileNumberMini.getText().toString())) ) {
                    ptmMobileNumberMini.setError("This field is required");
                    ptmMobileNumberMini.requestFocus();
                    ptmMobileNumberLyt.startAnimation(animation);
                    ptmMobileNumberFocus.getParent().requestChildFocus(ptmMobileNumberFocus, ptmMobileNumberFocus);
                    StyleableToast.makeText(activity, "Enter Mobile Number", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                } else {
                    if (!isValidPhone(ptmMobileNumberMini.getText().toString())){
                        ptmMobileNumberMini.setError("Invalid Mobile Number");
                        ptmMobileNumberMini.requestFocus();
                        ptmMobileNumberLyt.startAnimation(animation);
                        ptmMobileNumberFocus.getParent().requestChildFocus(ptmMobileNumberFocus, ptmMobileNumberFocus);
                        return;
                    }
                }
                ptmAadhaarNumberMini.clearFocus();
                ptmMobileNumberMini.clearFocus();
                ptmRemarkMini.clearFocus();

                if(fingerCapture){
                    if(fingerData != null){
                        miniStatement(ptmAadhaarNumberMini.getText().toString(), currentDateAndTime, fingerData, iinno, ptmRemarkMini.getText().toString(), ptmMobileNumberMini.getText().toString() );
                    } else {
                        StyleableToast.makeText(activity, "FingerPrint Data Null", Toast.LENGTH_LONG, R.style.myToastError).show();
                    }
                    fingerCapture = false;
                    ptmFingerPrintDone.setVisibility(View.GONE);
                    ptmCaptureFingerPrintMiniLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    captureFingerPrintMiniText.setTextColor(getResources().getColor(R.color.light_black));
                } else {
                    ptmCaptureFingerPrintMiniFocus.getParent().requestChildFocus(ptmCaptureFingerPrintMiniFocus, ptmCaptureFingerPrintMiniFocus);
                    ptmCaptureFingerPrintMiniLyt.startAnimation(animation);
                    StyleableToast.makeText(activity, "Capture fingerprint first!", Toast.LENGTH_LONG, R.style.myToastWarning).show();
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
                        startActivityForResult(intent2, 100);
                    }
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                    DialogCustom.showAlertDialog(activity, "Warning!", "Finger Print device not found...", "OK", true, () -> {});
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
        Call<Object> objectCall = RetrofitClient.getApi().paytmBankList(SplashActivity.prefManager.getToken());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("123345","onResponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                        if (jsonObject.has("banklist")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("banklist");
                            if (jsonObject1.has("data")) {
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
                            }
                        }
                    } else {
                        if (jsonObject.has("message")){
                            DialogCustom.showAlertDialog(activity, "Alert!", jsonObject.getString("message"), "OK", true, () -> {});
                        }
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(activity, R.layout.layout_spinner_item, bankListArray); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ptmBankNameMini.setAdapter(spinnerArrayAdapter);
                    ptmBankNameMini.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (parent.getItemAtPosition(position).equals("Select your bank")) {
                                Log.i("12121", "Select your bank");
                            } else {
                                ((TextView)parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_vigyos));
                                String selectedItem = (String) parent.getItemAtPosition(position);
                                for (BankListModel bankListModel : bankListModels) {
                                    if (bankListModel.getBankName().equalsIgnoreCase(selectedItem)) {
                                        bankName = bankListModel.getBankName();
                                        iinno = bankListModel.getIinno();
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

    private void miniStatement(String aadhaarNumber, String timeStamp, String fingerData, int nationalbankidentification, String requestremarks , String mobile){
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().paytmMiniStatement(SplashActivity.prefManager.getToken(), SplashActivity.prefManager.getFirstName()+" "+SplashActivity.prefManager.getLastName(),
                SplashActivity.prefManager.getPanCardNumber(), SplashActivity.prefManager.getCity()+" "+SplashActivity.prefManager.getState()+" India" , SplashActivity.prefManager.getCity(), SplashActivity.prefManager.getPinCode(),
                SplashActivity.prefManager.getStateCode(), mobile, "APP", ipAddress, aadhaarNumber, SplashActivity.prefManager.getPhone(), String.valueOf(latitude), String.valueOf(longitude), String.valueOf(nationalbankidentification),
                "bank5", "MS", requestremarks, SplashActivity.prefManager.getPaytmMerchantId(), fingerData, timeStamp);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("2016", "onResponse " + response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String  ackno = null, bankrrn = null, bankiin = null, message = null;
                    double  balanceamount = 0;
                    if (jsonObject.has("status") && jsonObject.getBoolean("status")){
                        if (jsonObject.has("ackno")) {
                            ackno = jsonObject.getString("ackno");
                        }
                        if (jsonObject.has("balanceamount")) {
                            balanceamount = jsonObject.getDouble("balanceamount");
                        }
                        if (jsonObject.has("bankrrn")) {
                            bankrrn = jsonObject.getString("bankrrn");
                        }
                        if (jsonObject.has("bankiin")) {
                            bankiin = jsonObject.getString("bankiin");
                        }
                        if (jsonObject.has("message")) {
                            message = jsonObject.getString("message");
                        }
                        if (jsonObject.has("ministatement")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("ministatement");
                            for (int i = 0; i <jsonArray.length(); i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                MiniStatementModel statementModel = new MiniStatementModel();
                                if (jsonObject1.has("date")) {
                                    statementModel.setDate(jsonObject1.getString("date"));
                                }
                                if (jsonObject1.has("txnType")) {
                                    statementModel.setTxnType(jsonObject1.getString("txnType"));
                                }
                                if (jsonObject1.has("amount")) {
                                    statementModel.setAmount(jsonObject1.getDouble("amount"));
                                }
                                if (jsonObject1.has("narration")) {
                                    statementModel.setNarration(jsonObject1.getString("narration"));
                                }
                                miniStatementModels.add(statementModel);
                            }
                        }

                        Intent intent = new Intent(activity, MiniStatementDoneActivity.class);
                        intent.putExtra("messageStatus", "Mini Statement!");
                        intent.putExtra("bankName", bankName);
                        intent.putExtra("aadhaarNumber", aadhaarNumber);
                        intent.putExtra("ackno", ackno);
                        intent.putExtra("balanceamount", balanceamount);
                        intent.putExtra("bankrrn", bankrrn);
                        intent.putExtra("bankiin", bankiin);
                        intent.putExtra("message", message);
                        intent.putParcelableArrayListExtra("miniStatementModels", miniStatementModels);
                        startActivity(intent);
                    } else {
                        if (jsonObject.has("message")){
                            DialogCustom.showAlertDialog(activity, "Alert!", jsonObject.getString("message"), "OK", true, () -> {});
                            fingerCapture = false;
                            ptmFingerPrintDone.setVisibility(View.GONE);
                            ptmCaptureFingerPrintMiniLyt.setBackgroundResource(R.drawable.credential_border_fill);
                            captureFingerPrintMiniText.setTextColor(getResources().getColor(R.color.light_black));
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
                ptmFingerPrintDone.setVisibility(View.GONE);
                ptmCaptureFingerPrintMiniLyt.setBackgroundResource(R.drawable.credential_border_fill);
                captureFingerPrintMiniText.setTextColor(getResources().getColor(R.color.light_black));
            }
        });
    }

    private void pleaseWait(){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
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
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    if (data != null) {
                        String result = data.getStringExtra("PID_DATA");
                        if (result != null) {
                            pidData = serializer.read(PidData.class, result);
                            if (!pidData._Resp.errCode.equals("0")) {
                                DialogCustom.showAlertDialog(activity, "Warning!", "Finger Print device not found...", "OK", true, () -> {});
                            } else {
                                fingerData = result;
                                fingerCapture = true;
                                ptmFingerPrintDone.setVisibility(View.VISIBLE);
                                StyleableToast.makeText(activity, "Capture FingerPrint Successfully", Toast.LENGTH_LONG, R.style.myToastSuccess).show();
                                ptmCaptureFingerPrintMiniLyt.setBackgroundResource(R.drawable.credential_border_fill_color);
                                captureFingerPrintMiniText.setTextColor(getResources().getColor(R.color.white));
                                Log.i("78954","pidData " + result);
                            }
                            dismissDialog();
                        }
                    }
                } catch (Exception e) {
                    Log.e("Error", "Error while deserialize pid data", e);
                    StyleableToast.makeText(activity, "Failed to Capture FingerPrint", Toast.LENGTH_LONG, R.style.myToastError).show();
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