package com.vigyos.vigyoscentercrm.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.os.BuildCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.vigyos.vigyoscentercrm.R;

@BuildCompat.PrereleaseSdkCheck
public class RegisterHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_home);
        toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Drawable drawable = toolbar.getNavigationIcon();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);

        findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterHomeActivity.this, NotificationActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 16908332){
            if (this.drawerLayout.isDrawerOpen(GravityCompat.START)){
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
        if (item.isChecked()){
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }
        if (id == R.id.nav_home){
            Log.i("12121","nav_home");
        } else if (id == R.id.nav_termsAndCondition) {
            startActivity(new Intent(RegisterHomeActivity.this, TermsAndConditionsActivity.class));
        } else if (id == R.id.nav_privacyPolicy) {
            startActivity(new Intent(RegisterHomeActivity.this, PrivacyPolicyActivity.class));
        } else if (id == R.id.nav_shareApp) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out the App at: https://play.google.com/store/apps/details?id=" + getPackageName());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if(id == R.id.nav_helpAndSupport){
            startActivity(new Intent(RegisterHomeActivity.this, HelpAndSupportActivity.class));
        } else if(id == R.id.nav_logout){
            areYouSure();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void areYouSure(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(RegisterHomeActivity.this);
        builder1.setMessage("Are you sure, You want to logout ?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        SplashActivity.prefManager.setClear();
                        startActivity(new Intent(RegisterHomeActivity.this, LoginActivity.class));
                        finish();
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
}