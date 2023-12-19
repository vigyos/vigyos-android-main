package com.vigyos.vigyoscentercrm.Fragment.AEPS.FinoBank;

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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;

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

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class FinoMiniStatementFragment extends Fragment {

    private View view;
    private LinearLayout aadhaarNumberFocus, bankNameFocus;
    private LinearLayout mobileNumberFocus, remarkFocus;
    private LinearLayout captureFingerPrintMiniFocus;
    private RelativeLayout aadhaarNumberLyt, bankNameLyt;
    private RelativeLayout mobileNumberLyt, remarkLyt;
    private RelativeLayout captureFingerPrintMiniLyt;
    private EditText aadhaarNumberMini, mobileNumberMini;
    private EditText remarkEnquiryMini;
    private Spinner bankNameMini;
    private TextView captureFingerPrintMiniText;
    private RelativeLayout process;
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
    private ImageView fingerPrintDone;
    private Animation animation;

    public FinoMiniStatementFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mini_statement, container, false);
        initialization();
        bankList();
        declaration();
        return view;
    }

    private void initialization() {
        aadhaarNumberFocus = view.findViewById(R.id.aadhaarNumberFocus);
        bankNameFocus = view.findViewById(R.id.bankNameFocus);
        mobileNumberFocus = view.findViewById(R.id.mobileNumberFocus);
        remarkFocus = view.findViewById(R.id.remarkFocus);
        aadhaarNumberLyt = view.findViewById(R.id.aadhaarNumberLyt);
        bankNameLyt = view.findViewById(R.id.bankNameLyt);
        mobileNumberLyt = view.findViewById(R.id.mobileNumberLyt);
        remarkLyt = view.findViewById(R.id.remarkLyt);
        aadhaarNumberMini = view.findViewById(R.id.aadhaarNumberMini);
        mobileNumberMini = view.findViewById(R.id.mobileNumberMini);
        remarkEnquiryMini = view.findViewById(R.id.remarkEnquiryMini);
        bankNameMini = view.findViewById(R.id.bankNameMini);
        process = view.findViewById(R.id.process);
        captureFingerPrintMiniFocus = view.findViewById(R.id.captureFingerPrintMiniFocus);
        fingerPrintDone = view.findViewById(R.id.captureDataMini);
        captureFingerPrintMiniLyt = view.findViewById(R.id.captureFingerPrintMiniLyt);
        captureFingerPrintMiniText = view.findViewById(R.id.captureFingerPrintMiniText);
    }

    private void declaration() {
        serializer = new Persister();
        positions = new ArrayList<>();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        animation = AnimationUtils.loadAnimation(activity, R.anim.shake_animation);
        aadhaarNumberMini.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    aadhaarNumberLyt.setBackgroundResource(R.drawable.credential_border);
                    mobileNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    remarkLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        mobileNumberMini.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    aadhaarNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    mobileNumberLyt.setBackgroundResource(R.drawable.credential_border);
                    remarkLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        remarkEnquiryMini.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    aadhaarNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    mobileNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    remarkLyt.setBackgroundResource(R.drawable.credential_border);
                }
            }
        });
        captureFingerPrintMiniLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(aadhaarNumberMini.getText().toString())  ){
                    aadhaarNumberMini.setError("This field is required");
                    aadhaarNumberMini.requestFocus();
                    aadhaarNumberLyt.startAnimation(animation);
                    aadhaarNumberFocus.getParent().requestChildFocus(aadhaarNumberFocus, aadhaarNumberFocus);
                    StyleableToast.makeText(activity, "Enter Aadhaar number", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                } else {
                    if (!isValidAadhaarNumber(aadhaarNumberMini.getText().toString())){
                        aadhaarNumberMini.setError("Enter a valid Aadhaar number");
                        aadhaarNumberMini.requestFocus();
                        aadhaarNumberLyt.startAnimation(animation);
                        aadhaarNumberFocus.getParent().requestChildFocus(aadhaarNumberFocus, aadhaarNumberFocus);
                        return;
                    }
                }
                if (bankNameMini.getSelectedItem().toString().trim().equals("Select your bank")) {
                    bankNameLyt.startAnimation(animation);
                    bankNameFocus.getParent().requestChildFocus(bankNameFocus, bankNameFocus);
                    StyleableToast.makeText(activity, "Select your bank", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                if ((TextUtils.isEmpty(mobileNumberMini.getText().toString())) ) {
                    mobileNumberMini.setError("This field is required");
                    mobileNumberMini.requestFocus();
                    mobileNumberLyt.startAnimation(animation);
                    mobileNumberFocus.getParent().requestChildFocus(mobileNumberFocus, mobileNumberFocus);
                    StyleableToast.makeText(activity, "Enter Mobile Number", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                } else {
                    if (!isValidPhone(mobileNumberMini.getText().toString())){
                        mobileNumberMini.setError("Invalid Mobile Number");
                        mobileNumberMini.requestFocus();
                        mobileNumberLyt.startAnimation(animation);
                        mobileNumberFocus.getParent().requestChildFocus(mobileNumberFocus, mobileNumberFocus);
                        return;
                    }
                }
                if ((TextUtils.isEmpty(remarkEnquiryMini.getText().toString())) ) {
                    remarkEnquiryMini.setError("This field is required");
                    remarkEnquiryMini.requestFocus();
                    remarkLyt.startAnimation(animation);
                    remarkFocus.getParent().requestChildFocus(remarkFocus, remarkFocus);
                    StyleableToast.makeText(activity, "Enter Remark", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                }
                aadhaarNumberMini.clearFocus();
                mobileNumberMini.clearFocus();
                remarkEnquiryMini.clearFocus();

                scanFingerPrint();
            }
        });
        process.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(TextUtils.isEmpty(aadhaarNumberMini.getText().toString())  ){
                        aadhaarNumberMini.setError("This field is required");
                        aadhaarNumberMini.requestFocus();
                        aadhaarNumberLyt.startAnimation(animation);
                        aadhaarNumberFocus.getParent().requestChildFocus(aadhaarNumberFocus, aadhaarNumberFocus);
                        StyleableToast.makeText(activity, "Enter Aadhaar number", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                        return;
                    } else {
                        if (!isValidAadhaarNumber(aadhaarNumberMini.getText().toString())){
                            aadhaarNumberMini.setError("Enter a valid Aadhaar number");
                            aadhaarNumberMini.requestFocus();
                            aadhaarNumberLyt.startAnimation(animation);
                            aadhaarNumberFocus.getParent().requestChildFocus(aadhaarNumberFocus, aadhaarNumberFocus);
                            return;
                        }
                    }
                    if (bankNameMini.getSelectedItem().toString().trim().equals("Select your bank")) {
                        bankNameLyt.startAnimation(animation);
                        bankNameFocus.getParent().requestChildFocus(bankNameFocus, bankNameFocus);
                        StyleableToast.makeText(activity, "Select your bank", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                        return;
                    }
                    if ((TextUtils.isEmpty(mobileNumberMini.getText().toString())) ) {
                        mobileNumberMini.setError("This field is required");
                        mobileNumberMini.requestFocus();
                        mobileNumberLyt.startAnimation(animation);
                        mobileNumberFocus.getParent().requestChildFocus(mobileNumberFocus, mobileNumberFocus);
                        StyleableToast.makeText(activity, "Enter Mobile Number", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                        return;
                    } else {
                        if (!isValidPhone(mobileNumberMini.getText().toString())){
                            mobileNumberMini.setError("Invalid Mobile Number");
                            mobileNumberMini.requestFocus();
                            mobileNumberLyt.startAnimation(animation);
                            mobileNumberFocus.getParent().requestChildFocus(mobileNumberFocus, mobileNumberFocus);
                            return;
                        }
                    }
                    if ((TextUtils.isEmpty(remarkEnquiryMini.getText().toString())) ) {
                        remarkEnquiryMini.setError("This field is required");
                        remarkEnquiryMini.requestFocus();
                        remarkLyt.startAnimation(animation);
                        remarkFocus.getParent().requestChildFocus(remarkFocus, remarkFocus);
                        StyleableToast.makeText(activity, "Enter Remark", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                        return;
                    }
                    aadhaarNumberMini.clearFocus();
                    mobileNumberMini.clearFocus();
                    remarkEnquiryMini.clearFocus();

                if(fingerCapture){
                    if(fingerData != null){
                        miniStatement(aadhaarNumberMini.getText().toString(), currentDateAndTime, fingerData, iinno, remarkEnquiryMini.getText().toString(), mobileNumberMini.getText().toString() );
                    } else {
                        StyleableToast.makeText(activity, "FingerPrint Data Null", Toast.LENGTH_LONG, R.style.myToastError).show();
                    }
                    fingerCapture = false;
                    fingerPrintDone.setVisibility(View.GONE);
                    captureFingerPrintMiniLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    captureFingerPrintMiniText.setTextColor(getResources().getColor(R.color.light_black));
                } else {
                    captureFingerPrintMiniLyt.startAnimation(animation);
                    captureFingerPrintMiniFocus.getParent().requestChildFocus(captureFingerPrintMiniFocus, captureFingerPrintMiniFocus);
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
        Call<Object> objectCall = RetrofitClient.getApi().bankList(SplashActivity.prefManager.getToken());
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
                        if (jsonObject.has("message")) {
                            DialogCustom.showAlertDialog(activity, "Alert!", jsonObject.getString("message"), "OK", true, () -> {});
                        }
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(activity, R.layout.layout_spinner_item, bankListArray); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    bankNameMini.setAdapter(spinnerArrayAdapter);
                    bankNameMini.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        Call<Object> objectCall = RetrofitClient.getApi().miniStatement(SplashActivity.prefManager.getToken(), "APP", aadhaarNumber, mobile,
                String.valueOf(latitude), String.valueOf(longitude), timeStamp, fingerData, ipAddress, "bank2", SplashActivity.prefManager.getFinoMerchantId(), String.valueOf(nationalbankidentification), requestremarks, "MS");
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("2016", "onResponse " + response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                        String  ackno = null, bankrrn = null,bankiin = null, message = null;
                        double balanceamount = 0;
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
                            fingerPrintDone.setVisibility(View.GONE);
                            captureFingerPrintMiniLyt.setBackgroundResource(R.drawable.credential_border_fill);
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
                fingerPrintDone.setVisibility(View.GONE);
                captureFingerPrintMiniLyt.setBackgroundResource(R.drawable.credential_border_fill);
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
                                DialogCustom.showAlertDialog(activity, "Warning!", "Finger Print device not found...", "OK", true,() -> {});
                            } else {
                                fingerData = result;
                                fingerCapture = true;
                                fingerPrintDone.setVisibility(View.VISIBLE);
                                StyleableToast.makeText(activity, "Capture FingerPrint Successfully", Toast.LENGTH_LONG, R.style.myToastSuccess).show();
                                captureFingerPrintMiniLyt.setBackgroundResource(R.drawable.credential_border_fill_color);
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