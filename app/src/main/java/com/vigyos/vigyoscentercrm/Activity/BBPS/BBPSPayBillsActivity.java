package com.vigyos.vigyoscentercrm.Activity.BBPS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Activity.SplashActivity;
import com.vigyos.vigyoscentercrm.Constant.DialogCustom;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class BBPSPayBillsActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView titleName;
    private TextView operatorName;
    private TextView displayName, billAmountText;
    private LinearLayout billAmountFocus;
    private RelativeLayout billNumberLyt, billAmountLyt;
    private EditText billNumber, billAmount;
    private TextView payBillText;
    private RelativeLayout payBillButton;
    private Dialog dialog;
//    private String billAmount,

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbpspay_bills);
        Initialization();
        Declaration();
    }

    private void Initialization() {
        ivBack = findViewById(R.id.ivBack);
        titleName = findViewById(R.id.titleName);
        operatorName = findViewById(R.id.operatorName);
        displayName = findViewById(R.id.displayName);
        billAmountText = findViewById(R.id.billAmountText);
        billAmountFocus = findViewById(R.id.billAmountFocus);
        billNumberLyt = findViewById(R.id.billNumberLyt);
        billAmountLyt = findViewById(R.id.billAmountLyt);
        billNumber = findViewById(R.id.billNumber);
        billAmount = findViewById(R.id.billAmount);
        payBillText = findViewById(R.id.payBillText);
        payBillButton = findViewById(R.id.payBillButton);
    }

    private void Declaration() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String category = intent.getStringExtra("category");
        String viewbill = intent.getStringExtra("viewbill");
        String regex = intent.getStringExtra("regex");
        String displayname = intent.getStringExtra("displayname");
        String ad1_d_name = intent.getStringExtra("ad1_d_name");
        String ad1_name = intent.getStringExtra("ad1_name");
        String ad1_regex = intent.getStringExtra("ad1_regex");
        String ad2_name = intent.getStringExtra("ad2_name");
        String ad3_name = intent.getStringExtra("ad3_name");
        String ad3_regex = intent.getStringExtra("ad3_regex");

        titleName.setText(category);
        operatorName.setText(name);
        displayName.setText(displayname);

        if (viewbill.equalsIgnoreCase("0")) {
            payBillText.setText("Pay Bill");
            billAmountFocus.setVisibility(View.VISIBLE);
        } else {
            payBillText.setText("Get Bill");
            billAmountFocus.setVisibility(View.GONE);
        }
        billNumber.requestFocus();
        billNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    billNumberLyt.setBackgroundResource(R.drawable.credential_border);
                    billAmountLyt.setBackgroundResource(R.drawable.credential_border_fill);
                }
            }
        });
        billAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    billNumberLyt.setBackgroundResource(R.drawable.credential_border_fill);
                    billAmountLyt.setBackgroundResource(R.drawable.credential_border);
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        payBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(BBPSPayBillsActivity.this, R.anim.viewpush));
                if (TextUtils.isEmpty(billNumber.getText().toString())) {
                    billNumber.setError("This field is required");
                    billNumber.requestFocus();
                    billNumberLyt.startAnimation(AnimationUtils.loadAnimation(BBPSPayBillsActivity.this, R.anim.shake_animation));
                    Toast.makeText(BBPSPayBillsActivity.this, "Enter " +displayname, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (viewbill.equalsIgnoreCase("0")) {
                    if (TextUtils.isEmpty(billAmount.getText().toString())) {
                        billAmount.setError("This field is required");
                        billAmount.requestFocus();
                        billAmountLyt.startAnimation(AnimationUtils.loadAnimation(BBPSPayBillsActivity.this, R.anim.shake_animation));
                        Toast.makeText(BBPSPayBillsActivity.this, "Enter Amount", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    api();
                } else {
                    fetchBill(id, billNumber.getText().toString(), name);
                }
            }
        });
    }

    private void api() {

    }

    private void fetchBill(String id, String canumber, String name) {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().fetchBill(SplashActivity.prefManager.getToken(), id, canumber, "online");
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("2016", "onResponse " + response);
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                        String billAmount = null, billdate = null, dueDate = null, userName = null;
                        String cellNumber = null, billnetamount = null, minBillAmount = null;
                        boolean acceptPayment = false, acceptPartPay = false;

                        if (jsonObject.has("bill_fetch")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("bill_fetch");
                            if (jsonObject1.has("billAmount")) {
                                billAmount = jsonObject1.getString("billAmount");
                            }
                            if (jsonObject1.has("billnetamount")) {
                                billnetamount = jsonObject1.getString("billnetamount");
                            }
                            if (jsonObject1.has("billdate")) {
                                billdate = jsonObject1.getString("billdate");
                            }
                            if (jsonObject1.has("dueDate")) {
                                dueDate = jsonObject1.getString("dueDate");
                            }
                            if (jsonObject1.has("minBillAmount")) {
                                minBillAmount = jsonObject1.getString("minBillAmount");
                            }
                            if (jsonObject1.has("acceptPayment")) {
                                acceptPayment = jsonObject1.getBoolean("acceptPayment");
                            }
                            if (jsonObject1.has("acceptPartPay")) {
                                acceptPartPay = jsonObject1.getBoolean("acceptPartPay");
                            }
                            if (jsonObject1.has("cellNumber")) {
                                cellNumber = jsonObject1.getString("cellNumber");
                            }
                            if (jsonObject1.has("userName")) {
                                userName = jsonObject1.getString("userName");
                            }
                        }
                        Intent intent = new Intent(BBPSPayBillsActivity.this, BBPSPayBills2Activity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("id", id);
                        intent.putExtra("billAmount", billAmount);
                        intent.putExtra("billnetamount", billnetamount);
                        intent.putExtra("billdate", billdate);
                        intent.putExtra("dueDate", dueDate);
                        intent.putExtra("minBillAmount", minBillAmount);
                        intent.putExtra("acceptPayment", acceptPayment);
                        intent.putExtra("acceptPartPay", acceptPartPay);
                        intent.putExtra("cellNumber", cellNumber);
                        intent.putExtra("userName", userName);
                        startActivity(intent);
                    } else {
                        if (jsonObject.has("message")) {
                            DialogCustom.showAlertDialog(BBPSPayBillsActivity.this, "Alert!", jsonObject.getString("message"), "OK", () -> {});
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.i("2016", "onFailure " + t);
                dismissDialog();
                Toast.makeText(BBPSPayBillsActivity.this, "Maintenance underway. We'll be back soon.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pleaseWait() {
        dialog = new Dialog(BBPSPayBillsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_loader);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void dismissDialog() {
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