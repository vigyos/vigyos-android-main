package com.vigyos.vigyoscentercrm.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.AppController;
import com.vigyos.vigyoscentercrm.Fragment.HistoryFragment;
import com.vigyos.vigyoscentercrm.Fragment.HomeFragment;
import com.vigyos.vigyoscentercrm.Fragment.OrderFragment;
import com.vigyos.vigyoscentercrm.Fragment.SearchFragment;
import com.vigyos.vigyoscentercrm.Fragment.WishlistFragment;
import com.vigyos.vigyoscentercrm.Model.SearchServicesModel;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BuildCompat.PrereleaseSdkCheck
public class MainActivity extends AppCompatActivity {

    public MeowBottomNavigation meowBottomNavigation;
    private Dialog dialog;
    boolean doubleBackToExitPressedOnce = false;
    private ArrayList<SearchServicesModel> searchServicesModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        profileData();
//        getServicesData();

        meowBottomNavigation = findViewById(R.id.bottomNav);
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
    }

    public void loadFragment(Fragment fragment, boolean flag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(flag) {
            ft.replace(R.id.frame_container, fragment);
        } else{
            ft.replace(R.id.frame_container, fragment);
        }
        ft.commit();
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

    private void getServicesData() {
//        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().getServiceName(SplashActivity.prefManager.getToken(), "1000");
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
//                dismissDialog();
                Log.i("12121", "onResponse " + response);
                if (response.code() == 200){
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        if (jsonObject.has("success") && jsonObject.getBoolean("success")){
                            JSONArray jsonObject1 = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonObject1.length(); i++ ){
                                JSONObject jsonObject2 = jsonObject1.getJSONObject(i);
                                SearchServicesModel servicesModel = new SearchServicesModel();
                                servicesModel.setService_id(jsonObject2.getString("service_id"));
                                servicesModel.setService(jsonObject2.toString());
                                servicesModel.setService_name(jsonObject2.getString("service_name"));
                                servicesModel.setDescription(jsonObject2.getString("description"));
                                searchServicesModels.add(servicesModel);
                            }
                        } else {
                            SplashActivity.prefManager.setClear();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                            Snackbar.make(findViewById(android.R.id.content), "Session expired please login again", Snackbar.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
//                    serviceList();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
//                dismissDialog();
                Log.i("12121", "onFailure " + t);
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