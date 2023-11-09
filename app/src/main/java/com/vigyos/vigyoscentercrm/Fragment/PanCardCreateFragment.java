package com.vigyos.vigyoscentercrm.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.vigyos.vigyoscentercrm.Activity.LoginActivity;
import com.vigyos.vigyoscentercrm.Activity.PanWebViewActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Activity.WalletActivity;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class PanCardCreateFragment extends Fragment {

    private View view;
    private final Activity activity;
    private RelativeLayout update;
    private EditText firstName, middleName, lastName, mobile_number;
    private EditText email, address, remark;
    private Spinner spinner, spinner1, spinner2, spinner3;
    private String gender, mode, kycType, titleType;
    private Dialog dialog;

    public PanCardCreateFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pan_card_create, container, false);
        initialization();
        declaration();
        return view;
    }

    private void initialization() {
        firstName = view.findViewById(R.id.FirstNameCreate);
        middleName = view.findViewById(R.id.middleNameCreate);
        lastName = view.findViewById(R.id.lastNameCreate);
        spinner = view.findViewById(R.id.genderCreate);
        spinner1 = view.findViewById(R.id.cardTypeCreate);
        spinner2 = view.findViewById(R.id.kycTypeSpinner);
        spinner3 = view.findViewById(R.id.TitleCreate);
        mobile_number = view.findViewById(R.id.mobileNumberCreate);
        email = view.findViewById(R.id.emailCreate);
        address = view.findViewById(R.id.addressCreate);
        remark = view.findViewById(R.id.remarkCreate);
        update = view.findViewById(R.id.CreatePanCard);
    }

    private void declaration() {
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(activity, R.array.gender,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select your Gender")) {
                    Log.i("12121","Select your Gender");
                } else {
                    String genderSelect = (String) parent.getItemAtPosition(position);
                    if(genderSelect.equalsIgnoreCase("Male")) {
                        gender = "M";
                    } else if (genderSelect.equalsIgnoreCase("Female")) {
                        gender = "F";
                    } else {
                        gender = "T";
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(activity, R.array.mode, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter3);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Pan Type")) {
                    Log.i("12121","Select Pan Type");
                } else {
                    String modeSelect = (String) parent.getItemAtPosition(position);
                    if(modeSelect.equalsIgnoreCase("Physical Pan")){
                        mode = "P";
                    } else {
                        mode = "E";
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(activity, R.array.kycType,android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter4);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select KYC Type")) {
                    Log.i("12121","Select KYC Type");
                } else {
                    String modeSelect = (String) parent.getItemAtPosition(position);
                    if(modeSelect.equalsIgnoreCase("E - Kyc")){
                        kycType = "K";
                    } else {
                        kycType = "E";
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(activity, R.array.TitleType,android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter5);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Title Type")) {
                    Log.i("12121","Select Title Type");
                } else {
                    String modeSelect = (String) parent.getItemAtPosition(position);
                    if(modeSelect.equalsIgnoreCase("Shri")){
                        titleType = "1";
                    } else if (modeSelect.equalsIgnoreCase("Smt.")){
                        titleType = "2";
                    } else {
                        titleType = "3";
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
                if (spinner3.getSelectedItem().toString().trim().equals("Select Title Type")) {
                    Toast.makeText(activity, "Select your Title", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(firstName.getText().toString())) {
                    firstName.setError("This field is required");
                    Toast.makeText(activity, "Enter First Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(lastName.getText().toString())) {
                    lastName.setError("This field is required");
                    Toast.makeText(activity, "Enter Last Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (spinner.getSelectedItem().toString().trim().equals("Select your Gender")) {
                    Toast.makeText(activity, "Select your Gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (spinner1.getSelectedItem().toString().trim().equals("Select Pan Type")) {
                    Toast.makeText(activity, "Select Pan Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (spinner2.getSelectedItem().toString().trim().equals("Select Kyc Type")) {
                    Toast.makeText(activity, "Select Kyc Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((TextUtils.isEmpty(mobile_number.getText().toString())) ) {
                    mobile_number.setError("This field is required");
                    Toast.makeText(activity, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!isValidPhone(mobile_number.getText().toString())){
                        mobile_number.setError("Invalid Number");
                        Toast.makeText(activity, "Invalid Number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if ((TextUtils.isEmpty(email.getText().toString()))) {
                    email.setError("This field is required");
                    Toast.makeText(activity, "Enter Email ID", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!isValidMail(email.getText().toString())){
                        email.setError("Invalid Email ID");
                        Toast.makeText(activity, "Invalid Email ID", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (TextUtils.isEmpty(address.getText().toString())) {
                    address.setError("This field is required");
                    Toast.makeText(activity, "Enter Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                areYouSure();
            }});
    }

    private void areYouSure() {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle("Create Pan Card?");
        alert.setMessage("Are you sure, You want to Create Card?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                createPanCard();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    public boolean isValidPhone(String num) {
        Pattern ptrn = Pattern.compile("[6-9][0-9]{9}");
        Matcher match = ptrn.matcher(num);
        return (match.find() && match.group().equals(num));
    }

    private boolean isValidMail(String email) {
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void createPanCard(){
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().panCardCreate(SplashActivity.prefManager.getToken(), titleType, firstName.getText().toString(), middleName.getText().toString(), lastName.getText().toString(),
                mode, kycType, gender, "https://vigyos.com/", email.getText().toString(), SplashActivity.prefManager.getUserID(), remark.getText().toString(), "PENDING", mobile_number.getText().toString(), address.getText().toString());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("85214","onResponse"+response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String url = jsonObject1.getString("url");
                        String encData = jsonObject1.getString("encdata");
                        Intent intent = new Intent(activity, PanWebViewActivity.class);
                        intent.putExtra("url", url);
                        intent.putExtra("encData", encData);
                        startActivity(intent);
                    } else {
                        if (jsonObject.has("message")) {
                            Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("85214","onFailure" + t);
                Toast.makeText(activity, "Maintenance underway. We'll be back soon.", Toast.LENGTH_SHORT).show();
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