package com.vigyos.vigyoscentercrm.Activity;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.AppController;
import com.vigyos.vigyoscentercrm.Constant.LockScreenChecker;
import com.vigyos.vigyoscentercrm.Fragment.HistoryFragment;
import com.vigyos.vigyoscentercrm.Fragment.HomeFragment;
import com.vigyos.vigyoscentercrm.Fragment.OrderFragment;
import com.vigyos.vigyoscentercrm.Fragment.ProfileFragment;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "MainActivity";
    private Typeface typefaceBold;
    private Typeface typefaceRegular;
    public LinearLayout home, history, order, profile;
    private ImageView homeIcon, historyIcon, orderIcon, profileIcon;
    private TextView homeText, historyText, orderText, profileText;
    private Dialog dialog;
    boolean doubleBackToExitPressedOnce = false;
    private BiometricPrompt biometricPrompt;
    private boolean isAuthenticated = false;
    private boolean isFirstLaunch = true;
    private AlertDialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
        declaration();
    }

    private void initialization() {
        home = findViewById(R.id.home);
        history = findViewById(R.id.history);
        order = findViewById(R.id.order);
        profile = findViewById(R.id.profile);
        homeIcon = findViewById(R.id.homeIcon);
        historyIcon = findViewById(R.id.historyIcon);
        orderIcon = findViewById(R.id.orderIcon);
        profileIcon = findViewById(R.id.profileIcon);
        homeText = findViewById(R.id.homeText);
        historyText =  findViewById(R.id.historyText);
        orderText =  findViewById(R.id.orderText);
        profileText = findViewById(R.id.profileText);
        home.setOnClickListener(this);
        history.setOnClickListener(this);
        order.setOnClickListener(this);
        profile.setOnClickListener(this);
    }

    private void declaration() {
        checkBioMetricSupported();
        profileData();
        typefaceBold = ResourcesCompat.getFont(MainActivity.this, R.font.poppins_semi_bold);
        typefaceRegular = ResourcesCompat.getFont(MainActivity.this, R.font.poppins_regular);
        if (BuildCompat.isAtLeastT()) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                    () -> {
                        Log.i("852145", "onBack");
                        if (AppController.backCheck){
                            if (doubleBackToExitPressedOnce) {
                                finishAffinity();
                                return;
                            }
                            this.doubleBackToExitPressedOnce = true;
                            Toast.makeText(this, "Click BACK again to exit", Toast.LENGTH_SHORT).show();
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    doubleBackToExitPressedOnce=false;
                                }
                            }, 2000);
                        } else {
                            loadFragment(new HomeFragment(MainActivity.this), true);
                            AppController.backCheck = true;
                        }
                    }
            );
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                homeIcon.setImageResource(R.drawable.home_fill);
                historyIcon.setImageResource(R.drawable.history);
                orderIcon.setImageResource(R.drawable.order);
                profileIcon.setImageResource(R.drawable.profile);
                homeText.setTextColor(getColor(R.color.dark_vigyos));
                homeText.setTypeface(typefaceBold);
                historyText.setTextColor(getColor(R.color.light_black));
                historyText.setTypeface(typefaceRegular);
                orderText.setTextColor(getColor(R.color.light_black));
                orderText.setTypeface(typefaceRegular);
                profileText.setTextColor(getColor(R.color.light_black));
                profileText.setTypeface(typefaceRegular);
                loadFragment(new HomeFragment(MainActivity.this), true);
                AppController.backCheck = true;
                break;
            case R.id.history:
                homeIcon.setImageResource(R.drawable.home);
                historyIcon.setImageResource(R.drawable.history_fill);
                orderIcon.setImageResource(R.drawable.order);
                profileIcon.setImageResource(R.drawable.profile);
                homeText.setTextColor(getColor(R.color.light_black));
                homeText.setTypeface(typefaceRegular);
                historyText.setTextColor(getColor(R.color.dark_vigyos));
                historyText.setTypeface(typefaceBold);
                orderText.setTextColor(getColor(R.color.light_black));
                orderText.setTypeface(typefaceRegular);
                profileText.setTextColor(getColor(R.color.light_black));
                profileText.setTypeface(typefaceRegular);
                loadFragment(new HistoryFragment(MainActivity.this), false );
                AppController.backCheck = false;
                break;
            case R.id.order:
                homeIcon.setImageResource(R.drawable.home);
                historyIcon.setImageResource(R.drawable.history);
                orderIcon.setImageResource(R.drawable.order_fill);
                profileIcon.setImageResource(R.drawable.profile);
                homeText.setTextColor(getColor(R.color.light_black));
                homeText.setTypeface(typefaceRegular);
                historyText.setTextColor(getColor(R.color.light_black));
                historyText.setTypeface(typefaceRegular);
                orderText.setTextColor(getColor(R.color.dark_vigyos));
                orderText.setTypeface(typefaceBold);
                profileText.setTextColor(getColor(R.color.light_black));
                profileText.setTypeface(typefaceRegular);
                loadFragment(new OrderFragment(MainActivity.this), false);
                AppController.backCheck = false;
                break;
            case R.id.profile:
                homeIcon.setImageResource(R.drawable.home);
                historyIcon.setImageResource(R.drawable.history);
                orderIcon.setImageResource(R.drawable.order);
                profileIcon.setImageResource(R.drawable.profile_fill);
                homeText.setTextColor(getColor(R.color.light_black));
                homeText.setTypeface(typefaceRegular);
                historyText.setTextColor(getColor(R.color.light_black));
                historyText.setTypeface(typefaceRegular);
                orderText.setTextColor(getColor(R.color.light_black));
                orderText.setTypeface(typefaceRegular);
                profileText.setTextColor(getColor(R.color.dark_vigyos));
                profileText.setTypeface(typefaceBold);
                loadFragment(new ProfileFragment(MainActivity.this), false);
                AppController.backCheck = false;
                break;
        }
    }

    public void loadFragment(Fragment fragment, boolean flag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (!fragmentManager.isStateSaved()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.commit();
        }
    }

    private void checkBioMetricSupported() {
        BiometricManager manager = BiometricManager.from(this);
        switch (manager.canAuthenticate(BIOMETRIC_WEAK | BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d(TAG, "App can authenticate using biometrics.");
                SplashActivity.prefManager.setBiometricSensor(false);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.d(TAG, "This device does not have a biometric sensor");
                SplashActivity.prefManager.setBiometricSensor(true);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.d(TAG, "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.d(TAG, "Need register at least one finger print");
                break;
            default:
                Log.d(TAG, "Unknown cause");
                Toast.makeText(this, "Unknown cause", Toast.LENGTH_SHORT).show();
                break;
        }

        boolean isLockScreenEnabled = LockScreenChecker.isLockScreenEnabled(getApplicationContext());
        if (isLockScreenEnabled) {
            // Lock screen is enabled
            Log.i("2525","Lock is enabled");
            SplashActivity.prefManager.setBiometricSensor(false);
            if (SplashActivity.prefManager.getBiometricLock()) {
                fingerPrintLock();
                FingerPrintLockDialog();
            }
        } else {
            // Lock screen is not enabled
            SplashActivity.prefManager.setBiometricSensor(true);
            Log.i("2525","Lock is not enabled");
        }
    }

    private void fingerPrintLock() {
        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.d(TAG, "Authentication error: "+ errString);
                isAuthenticated = false;
                if (errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
                    // User canceled the authentication
                    // Show a dialog to the user explaining that authentication is required
                    handleCancelAuth();
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.d(TAG, "Authentication succeeded!");
                isAuthenticated = true;
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //attempt not regconized fingerprint
                Log.d(TAG, "Authentication failed");
                isAuthenticated = false;
            }
        });
    }

    private void FingerPrintLockDialog() {
        BiometricPrompt.PromptInfo.Builder promptInfo = dialogMetric();
        promptInfo.setDeviceCredentialAllowed(true);
        biometricPrompt.authenticate(promptInfo.build());
    }

    BiometricPrompt.PromptInfo.Builder dialogMetric() {
        //Show prompt dialog
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Vigyos")
                .setSubtitle("Log in using your biometric credential");
    }

    private void handleCancelAuth() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setTitle("Vigyos is locked")
                .setMessage("Authentication is required to access the Vigyos app")
                .setPositiveButton("Unlock now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        // User clicked the "Try Again" button, prompt for authentication again
                        if (SplashActivity.prefManager.getBiometricLock()) {
                            fingerPrintLock();
                            FingerPrintLockDialog();
                        }
                    }
                });
        dialog1 = builder.create();
        dialog1.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog1 != null && dialog1.isShowing()) {
            dialog1.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SplashActivity.prefManager.getBiometricLock()) {
            if (!isAuthenticated) {
                if (isFirstLaunch) {
                    isFirstLaunch = false;
                } else {
                    handleCancelAuth();
                }
            }
        }
    }

    private void profileData() {
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().profile(SplashActivity.prefManager.getToken());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                dismissDialog();
                Log.i("12121", "onResponse " + response);
                if (response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            if (jsonObject1.has("user_id")) {
                                SplashActivity.prefManager.setUserID(jsonObject1.getString("user_id"));
                            }
                            if (jsonObject1.has("first_name")) {
                                SplashActivity.prefManager.setFirstName(jsonObject1.getString("first_name"));
                            }
                            if (jsonObject1.has("last_name")) {
                                SplashActivity.prefManager.setLastName(jsonObject1.getString("last_name"));
                            }
                            if (jsonObject1.has("company")) {
                                SplashActivity.prefManager.setCompany(jsonObject1.getString("company"));
                            }
                            if (jsonObject1.has("plan")) {
                                SplashActivity.prefManager.setPlan(jsonObject1.getString("plan"));
                            }
                            if (jsonObject1.has("email")){
                                SplashActivity.prefManager.setEmail(jsonObject1.getString("email"));
                            }
                            if (jsonObject1.has("phone")) {
                                SplashActivity.prefManager.setPhone(jsonObject1.getString("phone"));
                            }
                            if (jsonObject1.has("aadhar_number")) {
                                SplashActivity.prefManager.setAadhaarNumber(jsonObject1.getString("aadhar_number"));
                            }
                            if (jsonObject1.has("aadhar_attachment")) {
                                SplashActivity.prefManager.setAadhaarAttachment(jsonObject1.getString("aadhar_attachment"));
                            }
                            if (jsonObject1.has("pan_card_number")) {
                                SplashActivity.prefManager.setPanCardNumber(jsonObject1.getString("pan_card_number"));
                            }
                            if (jsonObject1.has("pan_card_attachment")) {
                                SplashActivity.prefManager.setPanCardAttachment(jsonObject1.getString("pan_card_attachment"));
                            }
                            if (jsonObject1.has("is_deleted")) {
                                SplashActivity.prefManager.setIsDeleted(jsonObject1.getString("is_deleted"));
                            }
                            if (jsonObject1.has("user_type")) {
                                SplashActivity.prefManager.setUserType(jsonObject1.getString("user_type"));
                            }
                            if (jsonObject1.has("is_active")) {
                                SplashActivity.prefManager.setIsActive(jsonObject1.getString("is_active"));
                            }
                            if (jsonObject1.has("license_no")) {
                                SplashActivity.prefManager.setLicenseNumber(jsonObject1.getString("license_no"));
                            }
                            if (jsonObject1.has("city")) {
                                SplashActivity.prefManager.setCity(jsonObject1.getString("city"));
                            }
                            if (jsonObject1.has("state")) {
                                SplashActivity.prefManager.setState(jsonObject1.getString("state"));
                            }
                            if (jsonObject1.has("pincode")) {
                                SplashActivity.prefManager.setPinCode(jsonObject1.getString("pincode"));
                            }
                            if (jsonObject1.has("profile_picture")){
                                SplashActivity.prefManager.setProfilePicture(jsonObject1.getString("profile_picture"));
                            }
                            if (jsonObject1.has("other_document")){
                                SplashActivity.prefManager.setOtherDocument(jsonObject1.getString("other_document"));
                            }
                            if (jsonObject1.has("create_time")){
                                SplashActivity.prefManager.setJoinDate(jsonObject1.getInt("create_time"));
                            }
                            if (jsonObject1.has("update_time")){
                                SplashActivity.prefManager.setUpdateTime(jsonObject1.getInt("update_time"));
                            }
                            if (jsonObject1.has("time_stamp")){
                                SplashActivity.prefManager.setTimeStamp(jsonObject1.getInt("time_stamp"));
                            }
                            if (jsonObject1.has("createdby")){
                                SplashActivity.prefManager.setCreatedBy(jsonObject1.getString("createdby"));
                            }
                            if (jsonObject1.has("sales_person")){
                                SplashActivity.prefManager.setSalesPerson(jsonObject1.getString("sales_person"));
                            }
                            if (jsonObject1.has("updated_by")){
                                SplashActivity.prefManager.setUpdatedBy(jsonObject1.getString("updated_by"));
                            }
                            if (jsonObject1.has("wallet_id")) {
                                SplashActivity.prefManager.setWalletId(jsonObject1.getString("wallet_id"));
                            }
                            if (jsonObject1.has("amount")) {
                                SplashActivity.prefManager.setAmount(jsonObject1.getInt("amount"));
                            }
                            if (jsonObject1.has("permissions")) {
                                JSONObject objectPermissions = jsonObject1.getJSONObject("permissions");
                                if (objectPermissions.has("dashboard")) {
                                    SplashActivity.prefManager.setDashboard(objectPermissions.getBoolean("dashboard"));
                                }
                                if (objectPermissions.has("services")) {
                                    SplashActivity.prefManager.setServices(objectPermissions.getBoolean("services"));
                                }
                                if (objectPermissions.has("add_to_wallet")) {
                                    SplashActivity.prefManager.setAddToWallet(objectPermissions.getBoolean("add_to_wallet"));
                                }
                                if (objectPermissions.has("service_request")) {
                                    SplashActivity.prefManager.setServiceRequest(objectPermissions.getBoolean("service_request"));
                                }
                                if (objectPermissions.has("users")) {
                                    SplashActivity.prefManager.setUsers(objectPermissions.getBoolean("users"));
                                }
                                if (objectPermissions.has("buy_new_service")) {
                                    SplashActivity.prefManager.setBuyNewService(objectPermissions.getBoolean("buy_new_service"));
                                }
                                if (objectPermissions.has("pan")) {
                                    SplashActivity.prefManager.setPan(objectPermissions.getBoolean("pan"));
                                }
                                if (objectPermissions.has("bbps")) {
                                    SplashActivity.prefManager.setBBPS(objectPermissions.getBoolean("bbps"));
                                }
                                if (objectPermissions.has("aeps")) {
                                    SplashActivity.prefManager.setAEPS(objectPermissions.getBoolean("aeps"));
                                }
                                if (objectPermissions.has("wallet")) {
                                    SplashActivity.prefManager.setWallet(objectPermissions.getBoolean("wallet"));
                                }
                                if (objectPermissions.has("my_orders")) {
                                    SplashActivity.prefManager.setMyOrders(objectPermissions.getBoolean("my_orders"));
                                }
                                if (objectPermissions.has("my_profile")) {
                                    SplashActivity.prefManager.setMyProfile(objectPermissions.getBoolean("my_profile"));
                                }
                                if (objectPermissions.has("contact_us")) {
                                    SplashActivity.prefManager.setContactUs(objectPermissions.getBoolean("contact_us"));
                                }
                            }
                            if (jsonObject1.has("merchant_id")) {
                                SplashActivity.prefManager.setMerchantId(jsonObject1.getString("merchant_id"));
                            }
                            if (jsonObject1.has("is_verified")){
                                SplashActivity.prefManager.setIsVerified(jsonObject1.getString("is_verified"));
                            }
                            if (jsonObject1.has("bank_verified")) {
                                SplashActivity.prefManager.setBankVerified(jsonObject1.getString("bank_verified"));
                            }
                            if (jsonObject1.has("last_verify_timestamp_aeps")) {
                                SplashActivity.prefManager.setLastVerifyTimeStampAeps(jsonObject1.getLong("last_verify_timestamp_aeps"));
                            }
                            if (jsonObject1.has("payout_balance")) {
                                SplashActivity.prefManager.setPayoutBalance(jsonObject1.getInt("payout_balance"));
                            }
                            if (jsonObject1.has("plan")) {
                                JSONObject jsonObject2 = jsonObject1.getJSONObject("plan");
                                if (jsonObject2.has("user_plan_id")){
                                    SplashActivity.prefManager.setUserPlanId(jsonObject2.getString("user_plan_id"));
                                }
                                if (jsonObject2.has("plan_id")) {
                                    SplashActivity.prefManager.setPlanId(jsonObject2.getString("plan_id"));
                                }
                                if (jsonObject2.has("plan_start_date")) {
                                    SplashActivity.prefManager.setPlanStartDate(jsonObject2.getInt("plan_start_date"));
                                }
                                if (jsonObject2.has("plan_end_date")) {
                                    SplashActivity.prefManager.setPlanEndDate(jsonObject2.getInt("plan_end_date"));
                                }
                                if (jsonObject2.has("plan_details")) {
                                    JSONObject jsonObject3 = jsonObject2.getJSONObject("plan_details");
                                    if (jsonObject3.has("is_active")) {
                                        SplashActivity.prefManager.setPlanIsActive(jsonObject3.getBoolean("is_active"));
                                    }
                                    if (jsonObject3.has("plan_line")) {
                                        SplashActivity.prefManager.setPlanLine(jsonObject3.getString("plan_line"));
                                    }
                                    if (jsonObject3.has("plan_name")) {
                                        SplashActivity.prefManager.setPlanName(jsonObject3.getString("plan_name"));
                                    }
                                    if (jsonObject3.has("created_by")) {
                                        SplashActivity.prefManager.setPlanCreatedBy(jsonObject3.getString("created_by"));
                                    }
                                    if (jsonObject3.has("is_deleted")) {
                                        SplashActivity.prefManager.setPlanIsDeleted(jsonObject3.getBoolean("is_deleted"));
                                    }
                                    if (jsonObject3.has("plan_price")) {
                                        SplashActivity.prefManager.setPlanPrice(jsonObject3.getInt("plan_price"));
                                    }
                                    if (jsonObject3.has("updated_by")) {
                                        SplashActivity.prefManager.setPlanUpdatedBy(jsonObject3.getString("updated_by"));
                                    }
                                    if (jsonObject3.has("updated_time")) {
                                        SplashActivity.prefManager.setPlanUpdatedBy(jsonObject3.getString("updated_time"));
                                    }
                                    if (jsonObject3.has("plan_duration")) {
                                        SplashActivity.prefManager.setPlanDuration(jsonObject3.getInt("plan_duration"));
                                    }
//                                    if (jsonObject3.has("plan_features")) {
//                                        JSONArray jsonArray = jsonObject3.getJSONArray("plan_features");
//                                        for (int i = 0; i<jsonArray.length(); i++) {
//                                            JSONObject jsonObject4 = jsonArray.getJSONObject(i);
//                                            if (jsonObject4.has("feature_name")) {
//                                            }
//                                        }
//                                    }
                                    if (jsonObject3.has("plan_description")) {
                                        SplashActivity.prefManager.setPlanDescription(jsonObject3.getString("plan_description"));
                                    }
                                    if (jsonObject3.has("plan_duration_type")) {
                                        SplashActivity.prefManager.setPlanDurationType(jsonObject3.getString("plan_duration_type"));
                                    }
                                    if (jsonObject3.has("plan_discounted_price")) {
                                        SplashActivity.prefManager.setPlanDiscountedPrice(jsonObject3.getInt("plan_discounted_price"));
                                    }
                                }
                            }
                        } else {
                            if (jsonObject.has("message")) {
                                Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                SplashActivity.prefManager.setClear();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Maintenance underway. We'll be back soon.", Toast.LENGTH_SHORT).show();
                    SplashActivity.prefManager.setClear();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
                loadFragment(new HomeFragment(MainActivity.this), true);
                AppController.backCheck = true;
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("12121", "onFailure " + t);
                loadFragment(new HomeFragment(MainActivity.this), true);
                AppController.backCheck = true;
                Toast.makeText(MainActivity.this, "Maintenance underway. We'll be back soon.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pleaseWait() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
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
        dismissDialog();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (AppController.backCheck){
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finishAffinity();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        } else {
            loadFragment(new HomeFragment(MainActivity.this), true);
            AppController.backCheck = true;
        }
    }
}