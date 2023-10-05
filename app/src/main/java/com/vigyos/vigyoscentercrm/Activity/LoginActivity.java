package com.vigyos.vigyoscentercrm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout loginButton;
    private EditText userName, password;
    private TextView forgotPassword;
    private ImageView visibility;
    private TextView termsAndConditions;
    private TextView privacyPolicy;
    private boolean showVisibility = true;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialization();
        declaration();
    }

    private void initialization() {
        loginButton = findViewById(R.id.loginButton);
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        forgotPassword = findViewById(R.id.forgotPassword);
        visibility = findViewById(R.id.passwordVisibility);
        termsAndConditions = findViewById(R.id.TermsAndConditions);
        privacyPolicy = findViewById(R.id.privacyPolicy);
    }

    private void declaration() {
        visibility.setBackgroundResource(R.drawable.visibility_off_icon);
        visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showVisibility){
                    Log.i("12541"," if ");
                    visibility.setBackgroundResource(R.drawable.visibility_icon);
                    showVisibility = false;
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    Log.i("12541"," else ");
                    visibility.setBackgroundResource(R.drawable.visibility_off_icon);
                    showVisibility = true;
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.viewpush));
                startActivity(new Intent(LoginActivity.this, HelpAndSupportActivity.class));
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.viewpush));
                if (userName.getText().toString().isEmpty() && password.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }
                // calling a method to post the data and passing our name and job.
                login(userName.getText().toString(), password.getText().toString());
            }
        });
        termsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.viewpush));
                startActivity(new Intent(LoginActivity.this, TermsAndConditionsActivity.class));
            }
        });
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.viewpush));
                startActivity(new Intent(LoginActivity.this, PrivacyPolicyActivity.class));
            }
        });
    }

    private void login(String name, String password){
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().login(name, password,"APP");
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("12121", "onResponse " + response);
                if (response.code() == 200){
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        if (jsonObject.has("success") && jsonObject.getBoolean("success")){
                            JSONObject jsonObjectData = jsonObject.getJSONObject("data");

                            SplashActivity.prefManager.setUserID(jsonObjectData.getString("userId"));
                            SplashActivity.prefManager.setToken("Bearer "+ jsonObjectData.getString("token"));
                            SplashActivity.prefManager.setFirstName(jsonObjectData.getString("first_name"));
                            SplashActivity.prefManager.setLastName(jsonObjectData.getString("last_name"));

                            SplashActivity.prefManager.setLogin(true);

                            Toast.makeText(LoginActivity.this, "Login Successfully" , Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Please enter valid email id & password", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("12121", "onFailure " + t);
                Toast.makeText(LoginActivity.this, "Server is on Maintenance", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pleaseWait(){
        dialog = new Dialog(this);
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
    protected void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}