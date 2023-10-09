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
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.vigyos.vigyoscentercrm.Activity.LoginActivity;
import com.vigyos.vigyoscentercrm.Activity.PanWebViewActivity;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
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

public class PanCardCreateFragment extends Fragment {

    private View view;
    private final Activity activity;
    private RelativeLayout update;
    private EditText firstName, middleName, lastName, mobile_number;
    private EditText email, address, remark;
    private Spinner spinner, spinner1;
    private String gender, mode;
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
        mobile_number = view.findViewById(R.id.mobileNumberCreate);
        email = view.findViewById(R.id.emailCreate);
        address = view.findViewById(R.id.addressCreate);
        remark = view.findViewById(R.id.remarkCreate);
        update = view.findViewById(R.id.CreatePanCard);
    }

    private void declaration() {
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(activity, R.array.gender,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String genderSelect = (String) parent.getItemAtPosition(position);
                if(genderSelect.equalsIgnoreCase("Male")){
                    gender = "M";
                } else if (genderSelect.equalsIgnoreCase("Female")){
                    gender = "F";
                } else {
                    gender = "T";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(activity, R.array.mode,android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner1.setAdapter(adapter3);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String modeSelect = (String) parent.getItemAtPosition(position);
                if(modeSelect.equalsIgnoreCase("Physical Pan")){
                    mode = "P";
                } else {
                    mode = "E";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.viewpush));
                if (TextUtils.isEmpty(firstName.getText().toString())) {
                    firstName.setError("This field is required");
                    Toast.makeText(activity, "Fill all details", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(lastName.getText().toString())) {
                    lastName.setError("This field is required");
                    Toast.makeText(activity, "Fill all details", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (spinner.getSelectedItem().toString().trim().equals("--Gender--")) {
                    Toast.makeText(activity, "Please select your Gender", Toast.LENGTH_SHORT).show();
                    Toast.makeText(activity, "Fill all details", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((TextUtils.isEmpty(mobile_number.getText().toString())) ) {
                    mobile_number.setError("This field is required");
                    Toast.makeText(activity, "Fill all details", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(activity, "Fill all details", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!isValidMail(email.getText().toString())){
                        email.setError("Invalid Email");
                        Toast.makeText(activity, "Invalid Email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (TextUtils.isEmpty(address.getText().toString())) {
                    address.setError("This field is required");
                    Toast.makeText(activity, "Fill all details", Toast.LENGTH_SHORT).show();
                    return;
                }
                areYouSure();
            }});
    }

    private void areYouSure() {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle("Create Pan Card?");
        alert.setMessage("Are you sure you want to Create Card?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
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
        Call<Object> objectCall = RetrofitClient.getApi().panCardCreate(SplashActivity.prefManager.getToken(), "1", firstName.getText().toString(), middleName.getText().toString(), lastName.getText().toString(),
                mode, gender, "https://vigyos.com/", email.getText().toString(), SplashActivity.prefManager.getUserID(), remark.getText().toString(), "PENDING", mobile_number.getText().toString(), address.getText().toString());
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