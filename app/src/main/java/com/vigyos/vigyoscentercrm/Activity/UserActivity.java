package com.vigyos.vigyoscentercrm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vigyos.vigyoscentercrm.Adapter.AdapterForUser;
import com.vigyos.vigyoscentercrm.AppController;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Utils.UserItemListener;

public class UserActivity extends AppCompatActivity implements UserItemListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        String[] list = {"Account Information", "Payout Balance", "Refund Policy", "Terms and Conditions", "Privacy Policy"};
        int[] theBitmapIds = { R.drawable.person_dark, R.drawable.payout_icon, R.drawable.refund_icon, R.drawable.terms_icon, R.drawable.privacy_icon};
        RecyclerView recyclerView = findViewById(R.id.profile_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserActivity.this));
        recyclerView.setAdapter(new AdapterForUser(list, UserActivity.this, theBitmapIds));
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        switch (position){
            case 0:
                startActivity(new Intent(UserActivity.this, AccountActivity.class));
                break;
            case 1:
                startActivity(new Intent(UserActivity.this, PayOutActivity.class));
                break;
            case 2:
                startActivity(new Intent(UserActivity.this, RefundPolicyActivity.class));
                break;
            case 3:
                startActivity(new Intent(UserActivity.this, TermsAndConditionsActivity.class));
                break;
            case 4:
                startActivity(new Intent(UserActivity.this, PrivacyPolicyActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}