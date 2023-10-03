package com.vigyos.vigyoscentercrm.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.vigyos.vigyoscentercrm.AppController;
import com.vigyos.vigyoscentercrm.Fragment.HistoryFragment;
import com.vigyos.vigyoscentercrm.Fragment.HomeFragment;
import com.vigyos.vigyoscentercrm.Fragment.OrderFragment;
import com.vigyos.vigyoscentercrm.Fragment.WishlistFragment;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public MeowBottomNavigation meowBottomNavigation;
    private Dialog dialog;
    boolean doubleBackToExitPressedOnce = false;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileData();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

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
                        AppController.backCheck = false;
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, SearchServicesActivity.class));
                        AppController.backCheck = false;
                        finish();
                        break;
                    case 3:
                        loadFragment(new HomeFragment(MainActivity.this), true);
                        AppController.backCheck = true;
                        break;
                    case 4:
                        loadFragment(new OrderFragment(), false);
                        AppController.backCheck = false;
                        break;
                    case 5:
                        loadFragment(new HistoryFragment(), false );
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
//                        startActivity(new Intent(MainActivity.this, SearchServicesActivity.class));
                        break;
                    case 3:
                        loadFragment(new HomeFragment(MainActivity.this), true);
                        AppController.backCheck = true;
                        break;
                    case 4:
                        loadFragment(new OrderFragment(), false);
                        AppController.backCheck = false;
                        break;
                    case 5:
                        loadFragment(new HistoryFragment(), false );
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
//                        startActivity(new Intent(MainActivity.this, SearchServicesActivity.class));
                        break;
                    case 3:
                        loadFragment(new HomeFragment(MainActivity.this), true);
                        AppController.backCheck = true;
                        break;
                    case 4:
                        loadFragment(new OrderFragment(), false);
                        AppController.backCheck = false;
                        break;
                    case 5:
                        loadFragment(new HistoryFragment(), false );
                        AppController.backCheck = false;
                        break;
                    default:
                        break;
                }
                return null;
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 16908332) {
            if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                this.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                this.drawerLayout.openDrawer(GravityCompat.START);
            }
        } else {
            this.drawerLayout.openDrawer(GravityCompat.START);
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Log.d("222222", "onNavigationItemSelected: " + id + item);
        if (item.isChecked()) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }
        if (id == R.id.nav_editProfile) {
//            Intent intent = new Intent(MainActivity.this, EditActivity.class);
//            intent.putExtra("edit", "Edit");
//            startActivity(intent);
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.easetap.com/pages/about")));
        } else if (id == R.id.nav_rating) {
            Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
            startActivity(rateIntent);
        } else if (id == R.id.nav_invite) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey!! Heard about the NFC cards? Your dynamic profile is one tap away. You can personalize your own card." +
                    "Check out the App at: https://play.google.com/store/apps/details?id=" + getPackageName());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.nav_help) {
//            startActivity(new Intent(MainActivity.this, HelpActivity.class));
        } else if (id == R.id.nav_buy) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.easetap.com/collections/all")));
        } else if (id == R.id.nav_privacy) {
//            startActivity(new Intent(MainActivity.this, PrivacyActivity.class));
        } else if (id == R.id.nav_logout) {
//            Logout();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void profileData(){
        pleaseWait();
        Call<Object> objectCall = RetrofitClient.getApi().profile(SplashActivity.prefManager.getToken());
        objectCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                dismissDialog();
                Log.i("12121", "onResponse " + response);
                if (response.code() == 200){
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        if (jsonObject.has("success") && jsonObject.getBoolean("success")){
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
                            if (jsonObject1.has("payout_balance")){
                                SplashActivity.prefManager.setPayoutBalance(jsonObject1.getInt("payout_balance"));
                            }

                            meowBottomNavigation.show(3,true);
                        } else {
                            Toast.makeText(MainActivity.this, "Please Login Again", Toast.LENGTH_SHORT).show();
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