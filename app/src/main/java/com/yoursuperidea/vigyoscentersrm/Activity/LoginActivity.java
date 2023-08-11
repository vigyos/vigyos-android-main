package com.yoursuperidea.vigyoscentersrm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yoursuperidea.vigyoscentersrm.R;
import com.yoursuperidea.vigyoscentersrm.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  LoginActivity extends AppCompatActivity {

    private EditText userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.viewpush));
                if (userName.getText().toString().isEmpty() && password.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter both the values", Toast.LENGTH_SHORT).show();
                    return;
                }
                // calling a method to post the data and passing our name and job.
                login(userName.getText().toString(), password.getText().toString());
            }
        });
    }

    private void login(String name, String password){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        Call<Object> objectCall = RetrofitClient.getApi().login(name, password);
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                progressDialog.dismiss();
                Log.i("12121", "onResponse " + response);
                if (response.code() == 200){
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        if (jsonObject.getString("success").equalsIgnoreCase("true")){
                            JSONObject jsonObjectData = jsonObject.getJSONObject("data");

                            SplashActivity.prefManager.setUserID(jsonObjectData.getString("userId"));
                            SplashActivity.prefManager.setToken(jsonObjectData.getString("token"));
                            SplashActivity.prefManager.setFirstName(jsonObjectData.getString("first_name"));
                            SplashActivity.prefManager.setLastName(jsonObjectData.getString("last_name"));


                            SplashActivity.prefManager.setLogin(true);

                            Toast.makeText(LoginActivity.this, "Login Successfully" , Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }else {
                            Toast.makeText(LoginActivity.this, "Please enter valid email id & password", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("12121", "onFailure " + t);
            }
        });
    }
}