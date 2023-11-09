package com.vigyos.vigyoscentercrm.Activity;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.AppController;
import com.vigyos.vigyoscentercrm.Constant.LockScreenChecker;
import com.vigyos.vigyoscentercrm.Fragment.HistoryFragment;
import com.vigyos.vigyoscentercrm.Fragment.HomeFragment;
import com.vigyos.vigyoscentercrm.Fragment.OrderFragment;
import com.vigyos.vigyoscentercrm.Fragment.WishlistFragment;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    public MeowBottomNavigation meowBottomNavigation;
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
        meowBottomNavigation = findViewById(R.id.bottomNav);
    }

    private void declaration() {
        checkBioMetricSupported();

        profileData();

        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.nav_wishlist_icon));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.nav_search_icon));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.nav_home_icon));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.nav_order_icon));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.nav_history_icon));
        meowBottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                // YOUR CODES
                Log.i("4545","model " + model);
                switch (model.getId()){
                    case 1:
                        loadFragment(new WishlistFragment(), false);
                        AppController.backCheck = false;
                        break;
                    case 2:
//                        loadFragment(new SearchFragment(MainActivity.this, searchServicesModels), false);
                        startActivity(new Intent(MainActivity.this, SearchServicesActivity.class));
//                        AppController.backCheck = false;
//                        finish();
                        break;
                    case 3:
                        loadFragment(new HomeFragment(MainActivity.this), true);
                        AppController.backCheck = true;
                        break;
                    case 4:
                        loadFragment(new OrderFragment(MainActivity.this), false);
                        AppController.backCheck = false;
                        break;
                    case 5:
                        loadFragment(new HistoryFragment(MainActivity.this), false );
                        AppController.backCheck = false;
                        break;
                    default:
                        break;
                }
                return null;
            }
        });
        meowBottomNavigation.setOnReselectListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()){
                    case 1:
                        Log.i("4545","setOnReselectListener ");
                        loadFragment(new WishlistFragment(), false);
                        AppController.backCheck = false;
                        break;
                    case 2:
//                        loadFragment(new SearchFragment(MainActivity.this, searchServicesModels), false);
//                        startActivity(new Intent(MainActivity.this, SearchServicesActivity.class));
                        break;
                    case 3:
                        loadFragment(new HomeFragment(MainActivity.this), true);
                        AppController.backCheck = true;
                        break;
                    case 4:
                        loadFragment(new OrderFragment(MainActivity.this), false);
                        AppController.backCheck = false;
                        break;
                    case 5:
                        loadFragment(new HistoryFragment(MainActivity.this), false );
                        AppController.backCheck = false;
                        break;
                    default:
                        break;
                }
                return null;
            }
        });
        meowBottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                // YOUR CODES
                switch (model.getId()){
                    case 1:
                        Log.i("4545","setOnShowListener ");
                        loadFragment(new WishlistFragment(), false);
                        AppController.backCheck = false;
                        break;
                    case 2:
//                        loadFragment(new SearchFragment(MainActivity.this , searchServicesModels), false);
//                        startActivity(new Intent(MainActivity.this, SearchServicesActivity.class));
                        break;
                    case 3:
                        loadFragment(new HomeFragment(MainActivity.this), true);
                        AppController.backCheck = true;
                        break;
                    case 4:
                        loadFragment(new OrderFragment(MainActivity.this), false);
                        AppController.backCheck = false;
                        break;
                    case 5:
                        loadFragment(new HistoryFragment(MainActivity.this), false );
                        AppController.backCheck = false;
                        break;
                    default:
                        break;
                }
                return null;
            }
        });
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
                            meowBottomNavigation.show(3,true);
                        }
                    }
            );
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
//                Toast.makeText(this, "App can authenticate using biometrics.", Toast.LENGTH_SHORT).show();
                SplashActivity.prefManager.setBiometricSensor(false);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.d(TAG, "This device does not have a biometric sensor");
//                Toast.makeText(this, "This device does not have a biometric sensor", Toast.LENGTH_SHORT).show();
                SplashActivity.prefManager.setBiometricSensor(true);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.d(TAG, "Biometric features are currently unavailable.");
//                Toast.makeText(this, "Biometric features are currently unavailable.", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.d(TAG, "Need register at least one finger print");
//                Toast.makeText(this, "Need register at least one finger print", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(getApplicationContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(getApplicationContext(), "Authentication succeeded!" , Toast.LENGTH_SHORT).show();
                isAuthenticated = true;
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //attempt not regconized fingerprint
                Log.d(TAG, "Authentication failed");
//                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
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
        Log.i("4587456", "onPause");
        if (dialog1 != null && dialog1.isShowing()) {
            dialog1.dismiss();
            Log.i("4587456", "dialog1.dismiss");
        }
//        isAuthenticated = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("4587456", "onResume");
        if (SplashActivity.prefManager.getBiometricLock()) {
            if (!isAuthenticated) {
                if (isFirstLaunch) {
                    isFirstLaunch = false;
                } else {
                    handleCancelAuth();
                }
            }
        }

//        // Check if the user has authenticated
//        // Check if the app is being launched for the first time
//        if (isFirstLaunch) {
//            // This is the first launch, do not show the authentication prompt
//            isFirstLaunch = false;
//        } else {
//            // This is not the first launch, check if the user has authenticated
//            if (!isAuthenticated) {
//                // User has not authenticated, show the authentication prompt again
//                handleCancelAuth();
//            }
//        }
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
                meowBottomNavigation.show(3,true);
                AppController.backCheck = true;
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                dismissDialog();
                Log.i("12121", "onFailure " + t);
                meowBottomNavigation.show(3,true);
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
            meowBottomNavigation.show(3,true);
        }
    }
}