package com.vigyos.vigyoscentercrm.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vigyos.vigyoscentercrm.R;

@BuildCompat.PrereleaseSdkCheck
public class SignUpActivity extends AppCompatActivity {

    private String url = "https://script.google.com/macros/s/AKfycbyr_bDgYGGglHTxHhbQsQr3-DMYb23A4Jl0Jbtdyv8icJpoi_RGWXasoGTZH10GMf7o9w/exec?";
    private EditText userFullName;
    private EditText emailAddress;
    private EditText phoneNumber;
    private EditText password;
    private EditText address;
    private RelativeLayout userNameLyt, emailLyt;
    private RelativeLayout phoneNumberLyt, passwordLyt;
    private RelativeLayout addressLyt;
    private ImageView passwordVisibility;
    private RelativeLayout signUpButton;
    private RelativeLayout signUpWithNumber;
    private TextView signUp;
    private CheckBox checkBox;
    private RelativeLayout checkBoxLyt;
    private TextView termsAndConditions;
    private boolean showVisibility = true;
    private Animation animation;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initialization();
        declaration();
    }

    private void initialization() {
        userFullName = findViewById(R.id.userFullName);
        emailAddress = findViewById(R.id.emailAddress);
        phoneNumber = findViewById(R.id.phoneNumber);
        password = findViewById(R.id.password);
        userNameLyt = findViewById(R.id.userNameLyt);
        emailLyt = findViewById(R.id.emailLyt);
        phoneNumberLyt = findViewById(R.id.phoneNumberLyt);
        passwordLyt = findViewById(R.id.passwordLyt);
        passwordVisibility = findViewById(R.id.passwordVisibility);
        signUpButton = findViewById(R.id.signUpButton);
        signUpWithNumber = findViewById(R.id.signUpWithNumber);
        signUp = findViewById(R.id.signUp);
        checkBox = findViewById(R.id.checkbox);
        checkBoxLyt = findViewById(R.id.checkBoxLyt);
        termsAndConditions = findViewById(R.id.termsAndConditions);
        address = findViewById(R.id.address);
        addressLyt = findViewById(R.id.addressLyt);
    }

    private void declaration() {
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_animation);
        userFullName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    userNameLyt.setBackgroundResource(R.drawable.credential_border);
                    emailLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    phoneNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    addressLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        emailAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    userNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    emailLyt.setBackgroundResource(R.drawable.credential_border);
                    phoneNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    addressLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        phoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    userNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    emailLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    phoneNumberLyt.setBackgroundResource(R.drawable.credential_border);
                    addressLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    userNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    emailLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    phoneNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    addressLyt.setBackgroundResource(R.drawable.credential_border);
                }
            }
        });

//        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    userNameLyt.setBackgroundResource(R.drawable.credential_border_fill);
//                    emailLyt.setBackgroundResource(R.drawable.credential_border_fill);
//                    phoneNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
//                    passwordLyt.setBackgroundResource(R.drawable.credential_border);
//                }
//            }
//        });
        passwordVisibility.setBackgroundResource(R.drawable.visibility_off_icon);
        passwordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showVisibility){
                    Log.i("12541"," if ");
                    passwordVisibility.setBackgroundResource(R.drawable.visibility_icon);
                    showVisibility = false;
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    password.setTextColor(getResources().getColor(R.color.dark_vigyos));
                } else {
                    Log.i("12541"," else ");
                    passwordVisibility.setBackgroundResource(R.drawable.visibility_off_icon);
                    showVisibility = true;
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
        termsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(SignUpActivity.this, R.anim.viewpush));
                startActivity(new Intent(SignUpActivity.this, TermsAndConditionsActivity.class));
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(SignUpActivity.this, R.anim.viewpush));
                if (TextUtils.isEmpty(userFullName.getText().toString())) {
                    userNameLyt.startAnimation(animation);
                    userFullName.requestFocus();
                    Toast.makeText(SignUpActivity.this, "Enter Your Full Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(emailAddress.getText().toString())) {
                    emailLyt.startAnimation(animation);
                    emailAddress.requestFocus();
                    Toast.makeText(SignUpActivity.this, "Enter Your Email ID", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber.getText().toString())) {
                    phoneNumberLyt.startAnimation(animation);
                    phoneNumber.requestFocus();
                    Toast.makeText(SignUpActivity.this, "Enter Your Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (TextUtils.isEmpty(password.getText().toString())) {
//                    passwordLyt.startAnimation(animation);
//                    password.requestFocus();
//                    Toast.makeText(SignUpActivity.this, "Enter Your Password", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (TextUtils.isEmpty(address.getText().toString())) {
                    addressLyt.startAnimation(animation);
                    address.requestFocus();
                    Toast.makeText(SignUpActivity.this, "Enter Your Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!checkBox.isChecked()) {
                    checkBoxLyt.startAnimation(animation);
                    Toast.makeText(SignUpActivity.this, "Accept our terms and conditions", Toast.LENGTH_SHORT).show();
                    return;
                }

                signup();
            }
        });
        signUpWithNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(SignUpActivity.this, R.anim.viewpush));
                startActivity(new Intent(SignUpActivity.this, SignUpWithPhoneActivity.class));
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(SignUpActivity.this, R.anim.viewpush));
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                    () -> {
                        Log.i("8522456","gfdgfdg");
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        finish();
                    }
            );
        }
    }

    private void signup() {
        pleaseWait();
        String URl = url + "action=create&name="+userFullName.getText().toString()+"&email="+emailAddress.getText().toString()+"&phone="+phoneNumber.getText().toString()+"&password="+address.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_SHORT).show();
                SplashActivity.prefManager.setRegister(true);
                SplashActivity.prefManager.setUserName(userFullName.getText().toString());
                startActivity(new Intent(SignUpActivity.this, AccountCreatedActivity.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialog();
                Toast.makeText(SignUpActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void pleaseWait() {
        dialog = new Dialog(SignUpActivity.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    }
}