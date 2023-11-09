package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vigyos.vigyoscentercrm.R;

@BuildCompat.PrereleaseSdkCheck
public class RegisterActivity extends AppCompatActivity {

    private String url = "https://script.google.com/macros/s/AKfycbzrldy_ko-B13WAPOgU_k_3m5QcdzHKKricqe6ff3ddOt_lA_vtgZHEnh1zvxLbyBWA/exec?";
    private ImageView ivBack;
    private EditText userName;
    private EditText phoneNumber;
    private EditText emailID;
    private EditText address;
    private EditText city;
    private EditText state;
    private RelativeLayout registerButton;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialization();
        declaration();
    }

    private void initialization() {
        ivBack = findViewById(R.id.ivBack);
        userName = findViewById(R.id.userName);
        phoneNumber = findViewById(R.id.phoneNumber);
        emailID = findViewById(R.id.emailID);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        registerButton = findViewById(R.id.registerButton);
    }

    private void declaration() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.viewpush));
                if (TextUtils.isEmpty(userName.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Enter Your Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Enter Your Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(city.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Enter Your City", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (TextUtils.isEmpty(state.getText().toString())) {
//                    Toast.makeText(RegisterActivity.this, "Enter Your State", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                areYouSure();
            }
        });
    }

    private void areYouSure(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(RegisterActivity.this);
        builder1.setMessage("Are you sure, You want to Register ?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        submitDataToSheet();
                    }
                });
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void submitDataToSheet() {
        pleaseWait();
        String URl = url + "action=create&name="+userName.getText().toString()+"&phone="+phoneNumber.getText().toString()+"&city="+city.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                SplashActivity.prefManager.setRegister(true);
                startActivity(new Intent(RegisterActivity.this, RegisterHomeActivity.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialog();
                Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void pleaseWait(){
        dialog = new Dialog(RegisterActivity.this);
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