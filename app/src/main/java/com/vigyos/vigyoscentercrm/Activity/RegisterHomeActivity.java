package com.vigyos.vigyoscentercrm.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

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
    private TextView userName, userName2;
    private Dialog dialog;

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

        userName = findViewById(R.id.userName);
        userName2 = findViewById(R.id.userName2);
        userName.setText(SplashActivity.prefManager.getUserName());
        userName2.setText(SplashActivity.prefManager.getUserName());
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

    private void areYouSure() {
        dialog = new Dialog(RegisterHomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_yes_or_no);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setLayout(-1, -1);
        TextView title = dialog.findViewById(R.id.title);
        title.setText("Logout!");
        TextView details = dialog.findViewById(R.id.details);
        details.setText("Are you sure, You want to logout ?");
        details.setMovementMethod(LinkMovementMethod.getInstance());
        dialog.findViewById(R.id.noLyt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the "GRANT!" button is clicked
                v.startAnimation(AnimationUtils.loadAnimation(RegisterHomeActivity.this, R.anim.viewpush));
                dismissDialog();
            }
        });
        dialog.findViewById(R.id.yesLyt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the "GRANT!" button is clicked
                v.startAnimation(AnimationUtils.loadAnimation(RegisterHomeActivity.this, R.anim.viewpush));
                dismissDialog();
                SplashActivity.prefManager.setClear();
                startActivity(new Intent(RegisterHomeActivity.this, LoginActivity.class));
                finish();
            }
        });
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