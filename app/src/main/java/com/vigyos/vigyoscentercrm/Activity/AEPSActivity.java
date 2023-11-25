package com.vigyos.vigyoscentercrm.Activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.vigyos.vigyoscentercrm.Adapter.AEPSFragmentAdapter;
import com.vigyos.vigyoscentercrm.Fragment.EnquiryFragment;
import com.vigyos.vigyoscentercrm.Fragment.MiniStatementFragment;
import com.vigyos.vigyoscentercrm.Fragment.PanCardCreateFragment;
import com.vigyos.vigyoscentercrm.Fragment.PanCardUpdateFragment;
import com.vigyos.vigyoscentercrm.Fragment.WishlistFragment;
import com.vigyos.vigyoscentercrm.Fragment.WithdrawalFragment;
import com.vigyos.vigyoscentercrm.R;
import com.google.android.material.tabs.TabLayout;

@BuildCompat.PrereleaseSdkCheck
public class AEPSActivity extends AppCompatActivity {

    private  ImageView ivBack;
    private RelativeLayout withdrawalLyt, enquiryLyt, miniStatementLyt;
    private TextView withdrawalText, enquiryText, miniStatementText;
    private View withdrawalLine, enquiryLine, miniStatementLine;
    private Typeface typefaceBold;
    private Typeface typefaceRegular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aeps);
        initialization();
        declaration();
    }

    private void initialization() {
        ivBack = findViewById(R.id.ivBack);
        withdrawalLyt  = findViewById(R.id.withdrawalLyt);
        enquiryLyt  = findViewById(R.id.enquiryLyt);
        miniStatementLyt  = findViewById(R.id.miniStatementLyt);
        withdrawalText  = findViewById(R.id.withdrawalText);
        enquiryText  = findViewById(R.id.enquiryText);
        miniStatementText  = findViewById(R.id.miniStatementText);
        withdrawalLine  = findViewById(R.id.withdrawalLine);
        enquiryLine  = findViewById(R.id.enquiryLine);
        miniStatementLine  = findViewById(R.id.miniStatementLine);
    }

    private void declaration() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();

            }
        });
        loadFragment(new WithdrawalFragment(AEPSActivity.this), false);
        tabLayout();
    }

    private void tabLayout () {
        typefaceBold = ResourcesCompat.getFont(AEPSActivity.this, R.font.poppins_semi_bold);
        typefaceRegular = ResourcesCompat.getFont(AEPSActivity.this, R.font.poppins_regular);
        withdrawalLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawalText.setTextColor(getColor(R.color.dark_vigyos));
                withdrawalText.setTypeface(typefaceBold);
                withdrawalLine.setBackgroundColor(getColor(R.color.dark_vigyos));

                enquiryText.setTextColor(getColor(R.color.not_click));
                enquiryText.setTypeface(typefaceRegular);
                enquiryLine.setBackgroundColor(getColor(R.color.not_click));

                miniStatementText.setTextColor(getColor(R.color.not_click));
                miniStatementText.setTypeface(typefaceRegular);
                miniStatementLine.setBackgroundColor(getColor(R.color.not_click));

                loadFragment(new WithdrawalFragment(AEPSActivity.this), false);
            }
        });
        enquiryLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawalText.setTextColor(getColor(R.color.not_click));
                withdrawalText.setTypeface(typefaceRegular);
                withdrawalLine.setBackgroundColor(getColor(R.color.not_click));

                enquiryText.setTextColor(getColor(R.color.dark_vigyos));
                enquiryText.setTypeface(typefaceBold);
                enquiryLine.setBackgroundColor(getColor(R.color.dark_vigyos));

                miniStatementText.setTextColor(getColor(R.color.not_click));
                miniStatementText.setTypeface(typefaceRegular);
                miniStatementLine.setBackgroundColor(getColor(R.color.not_click));

                loadFragment(new EnquiryFragment(AEPSActivity.this), false);
            }
        });
        miniStatementLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawalText.setTextColor(getColor(R.color.not_click));
                withdrawalText.setTypeface(typefaceRegular);
                withdrawalLine.setBackgroundColor(getColor(R.color.not_click));

                enquiryText.setTextColor(getColor(R.color.not_click));
                enquiryText.setTypeface(typefaceRegular);
                enquiryLine.setBackgroundColor(getColor(R.color.not_click));

                miniStatementText.setTextColor(getColor(R.color.dark_vigyos));
                miniStatementText.setTypeface(typefaceBold);
                miniStatementLine.setBackgroundColor(getColor(R.color.dark_vigyos));

                loadFragment(new MiniStatementFragment(AEPSActivity .this), false);
            }
        });
    }

    public void loadFragment(Fragment fragment, boolean flag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (!fragmentManager.isStateSaved()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.commit();
        }
    }
}