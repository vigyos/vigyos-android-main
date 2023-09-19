package com.vigyos.vigyoscentercrm.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.Fragment.HistoryFragment;
import com.vigyos.vigyoscentercrm.Fragment.HomeFragment;
import com.vigyos.vigyoscentercrm.Fragment.MessageFragment;
import com.vigyos.vigyoscentercrm.Fragment.OrderFragment;
import com.vigyos.vigyoscentercrm.Fragment.WishlistFragment;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public MeowBottomNavigation meowBottomNavigation;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileData();

        meowBottomNavigation = findViewById(R.id.bottomNav);
        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.wishlist_icon));
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
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, SearchServicesActivity.class));
                        finish();
                        break;
                    case 3:
                        loadFragment(new HomeFragment(MainActivity.this), true);
                        break;
                    case 4:
                        loadFragment(new OrderFragment(), false);
                        break;
                    case 5:
                        loadFragment(new HistoryFragment(), false );
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
                        loadFragment(new WishlistFragment(), false);
                        break;
                    case 2:
//                        startActivity(new Intent(MainActivity.this, SearchServicesActivity.class));
                        break;
                    case 3:
                        loadFragment(new HomeFragment(MainActivity.this), true);
                        break;
                    case 4:
                        loadFragment(new OrderFragment(), false);
                        break;
                    case 5:
                        loadFragment(new HistoryFragment(), false );
                        break;
                    default:
                        break;
                }
                return null;
            }
        });
        meowBottomNavigation.show(3,true);
    }

    public void loadFragment(Fragment fragment, boolean flag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(flag) {
            ft.replace(R.id.frame_container, fragment);
        } else{
            ft.replace(R.id.frame_container, fragment);
        }
        ft.commit();
    }

    private void profileData(){
        pleaseWait();
        Log.i("2525","token " +SplashActivity.prefManager.getToken() );
        Call<Object> objectCall = RetrofitClient.getApi().profile(SplashActivity.prefManager.getToken());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                dismissDialog();
                Log.i("12121", "onResponse " + response);
                if (response.code() == 200){
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                        SplashActivity.prefManager.setFirstName(jsonObject1.getString("first_name"));
                        SplashActivity.prefManager.setLastName(jsonObject1.getString("last_name"));
                        SplashActivity.prefManager.setEmail(jsonObject1.getString("email"));
                        SplashActivity.prefManager.setPhone(jsonObject1.getString("phone"));
                        SplashActivity.prefManager.setAadhaarNumber(jsonObject1.getString("aadhar_number"));
                        SplashActivity.prefManager.setAadhaarAttachment(jsonObject1.getString("aadhar_attachment"));
                        SplashActivity.prefManager.setPanCardNumber(jsonObject1.getString("pan_card_number"));
                        SplashActivity.prefManager.setPanCardAttachment(jsonObject1.getString("pan_card_attachment"));
                        if (jsonObject1.has("license_no")){
                            SplashActivity.prefManager.setLicenseNumber(jsonObject1.getString("license_no"));
                        } else {
                            SplashActivity.prefManager.setLicenseNumber("null");
                        }
                        if (jsonObject1.has("merchant_id")){
                            SplashActivity.prefManager.setMerchantId(jsonObject1.getString("merchant_id"));
                        } else {
                            SplashActivity.prefManager.setMerchantId("null");
                        }
                        if (jsonObject1.has("amount")){
                            SplashActivity.prefManager.setAmount(jsonObject1.getInt("amount"));
                        }
                        if (jsonObject1.has("profile_picture")){
                            SplashActivity.prefManager.setProfilePicture(jsonObject1.getString("profile_picture"));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                dismissDialog();
                Log.i("12121", "onFailure " + t);
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
}