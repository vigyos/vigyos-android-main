package com.vigyos.vigyoscentercrm.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class RazorPayActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    public EditText amount;
    public Button process;
    private Dialog dialog;
    private String id = null;

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Handle broadcast
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razor_pay);
        // Register the receiver only in onCreate
        Checkout.preload(getApplicationContext());

        amount = findViewById(R.id.amount);
        process = findViewById(R.id.process);
        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrder();
            }
        });

        // Register the receiver
        registerReceiver(myReceiver, new IntentFilter("yourAction"));
    }

    private void createOrder(){
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().razorpayCreateOrder(SplashActivity.prefManager.getToken(), Integer.parseInt(amount.getText().toString() + "00"), "INR",
                SplashActivity.prefManager.getUserID(), "Subscription Plan", SplashActivity.prefManager.getFirstName() + " " + SplashActivity.prefManager.getLastName(),
                SplashActivity.prefManager.getPhone(), SplashActivity.prefManager.getEmail(), "Jeevan Bima", "RECHARGE");
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("2016", "onResponse " + response);
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String amount_due = null, amount_paid = null;
                    String attempts = null, created_at = null, currency = null, entity = null;
                    String offer_id = null, receipt = null, status = null;

                    int amount = 0;

                    if (jsonObject.has("amount")) {
                        amount = jsonObject.getInt("amount");
                    }
                    if (jsonObject.has("amount_due")) {
                        amount_due = jsonObject.getString("amount_due");
                    }
                    if (jsonObject.has("amount_paid")) {
                        amount_paid = jsonObject.getString("amount_paid");
                    }
                    if (jsonObject.has("attempts")) {
                        attempts = jsonObject.getString("attempts");
                    }
                    if (jsonObject.has("created_at")) {
                        created_at = jsonObject.getString("created_at");
                    }
                    if (jsonObject.has("currency")) {
                        currency = jsonObject.getString("currency");
                    }
                    if (jsonObject.has("entity")) {
                        entity = jsonObject.getString("entity");
                    }
                    if (jsonObject.has("id")) {
                        id = jsonObject.getString("id");
                    }
//                    JSONArray jsonArray = jsonObject.getJSONArray("notes");
                    if (jsonObject.has("offer_id")) {
                        offer_id = jsonObject.getString("offer_id");
                    }
                    if (jsonObject.has("receipt")) {
                        receipt = jsonObject.getString("receipt");
                    }
                    if (jsonObject.has("status")) {
                        status = jsonObject.getString("status");
                    }

                    startPayment(id, amount);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("2016", "onFailure " + t);

            }
        });

    }

    public void startPayment(String id, int amount) {

        Log.i("2121212","id " + id );

        Checkout checkout = new Checkout();

//        checkout.setKeyID("rzp_test_uGEFDaUck79dOI");

        checkout.setImage(R.drawable.vigyos_logo);

        final Activity activity = this;

//        try {
//            JSONObject options = new JSONObject();
//
//            options.put("name", SplashActivity.prefManager.getFirstName() + " " +SplashActivity.prefManager.getLastName());
//            options.put("description", "Subscription Plan");
//            options.put("order_id", id);//from response of step 3.
//            options.put("currency", "INR");
//            options.put("amount", amount);//pass amount in currency subunits
//
////            options.put("prefill.email", SplashActivity.prefManager.getEmail());
////            options.put("prefill.contact", SplashActivity.prefManager.getPhone());
////            JSONObject retryObj = new JSONObject();
////            retryObj.put("enabled", true);
////            retryObj.put("max_count", 4);
////            options.put("retry", retryObj);
//
//            checkout.open(activity, options);
//
//        } catch(Exception e) {
//            Log.e(TAG, "Error in starting Razorpay Checkout", e);
//        }

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            options.put("send_sms_hash",true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("order_id", id);//from response of step 3.
            options.put("currency", "INR");
//            options.put("amount", "100");
            options.put("amount", amount);//pass amount in currency subunits

            JSONObject preFill = new JSONObject();
            preFill.put("email", "test@razorpay.com");
            preFill.put("contact", "9876543210");

            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void pleaseWait() {
        dialog = new Dialog(this);
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
    protected void onDestroy() {
        // Unregister the receiver only once in onDestroy
        unregisterReceiverIfNeeded();
        dismissDialog();  // Make sure to dismiss the dialog
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiverIfNeeded();
        dismissDialog();
    }

    private void unregisterReceiverIfNeeded() {
        try {
            unregisterReceiver(myReceiver);
        } catch (IllegalArgumentException e) {
            // Receiver was not registered, ignore
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
        Log.i("2121212", "Payment Success "+ s + "\n" + "Payment Data " + paymentData.getData());
        callBackOrder(id);
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(this, "Payment Error", Toast.LENGTH_SHORT).show();
    }

    private void callBackOrder(String orderID) {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().razorpayCallBackOrder(SplashActivity.prefManager.getToken(), orderID, "true", "", "","");
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("2121212", "onResponse " + response);

            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("2121212", "onFailure " + t);
            }
        });
    }

}