package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vigyos.vigyoscentercrm.Fragment.AEPS.FinoBank.EnquiryFragment;
import com.vigyos.vigyoscentercrm.Fragment.AEPS.FinoBank.MiniStatementFragment;
import com.vigyos.vigyoscentercrm.Fragment.AEPS.FinoBank.WithdrawalFragment;
import com.vigyos.vigyoscentercrm.Fragment.AEPS.Paytm.PtmEnquiryFragment;
import com.vigyos.vigyoscentercrm.Fragment.AEPS.Paytm.PtmMiniStatementFragment;
import com.vigyos.vigyoscentercrm.Fragment.AEPS.Paytm.PtmWithdrawalFragment;
import com.vigyos.vigyoscentercrm.R;

@BuildCompat.PrereleaseSdkCheck
public class PaytmAEPSActivity extends AppCompatActivity {

    private ImageView ivBack;
    private RelativeLayout ptmWithdrawalLyt, ptmEnquiryLyt, ptmMiniStatementLyt;
    private TextView ptmWithdrawalText, ptmEnquiryText, ptmMiniStatementText;
    private View ptmWithdrawalLine, ptmEnquiryLine, ptmMiniStatementLine;
    private Typeface typefaceBold;
    private Typeface typefaceRegular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_aeps);
        initialization();
        declaration();
    }

    private void initialization() {
        ivBack = findViewById(R.id.ivBack);
        ptmWithdrawalLyt = findViewById(R.id.ptmWithdrawalLyt);
        ptmEnquiryLyt = findViewById(R.id.ptmEnquiryLyt);
        ptmMiniStatementLyt = findViewById(R.id.ptmMiniStatementLyt);
        ptmWithdrawalText = findViewById(R.id.ptmWithdrawalText);
        ptmEnquiryText = findViewById(R.id.ptmEnquiryText);
        ptmMiniStatementText = findViewById(R.id.ptmMiniStatementText);
        ptmWithdrawalLine = findViewById(R.id.ptmWithdrawalLine);
        ptmEnquiryLine = findViewById(R.id.ptmEnquiryLine);
        ptmMiniStatementLine = findViewById(R.id.ptmMiniStatementLine);
    }

    private void declaration() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        loadFragment(new PtmWithdrawalFragment(PaytmAEPSActivity.this));
        tabLayout();
    }

    private void tabLayout () {
        typefaceBold = ResourcesCompat.getFont(PaytmAEPSActivity.this, R.font.poppins_semi_bold);
        typefaceRegular = ResourcesCompat.getFont(PaytmAEPSActivity.this, R.font.poppins_regular);
        ptmWithdrawalLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ptmWithdrawalText.setTextColor(getColor(R.color.dark_vigyos));
                ptmWithdrawalText.setTypeface(typefaceBold);
                ptmWithdrawalLine.setBackgroundColor(getColor(R.color.dark_vigyos));
                ptmEnquiryText.setTextColor(getColor(R.color.not_click));
                ptmEnquiryText.setTypeface(typefaceRegular);
                ptmEnquiryLine.setBackgroundColor(getColor(R.color.not_click));
                ptmMiniStatementText.setTextColor(getColor(R.color.not_click));
                ptmMiniStatementText.setTypeface(typefaceRegular);
                ptmMiniStatementLine.setBackgroundColor(getColor(R.color.not_click));

                loadFragment(new PtmWithdrawalFragment(PaytmAEPSActivity.this));
            }
        });
        ptmEnquiryLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ptmWithdrawalText.setTextColor(getColor(R.color.not_click));
                ptmWithdrawalText.setTypeface(typefaceRegular);
                ptmWithdrawalLine.setBackgroundColor(getColor(R.color.not_click));
                ptmEnquiryText.setTextColor(getColor(R.color.dark_vigyos));
                ptmEnquiryText.setTypeface(typefaceBold);
                ptmEnquiryLine.setBackgroundColor(getColor(R.color.dark_vigyos));
                ptmMiniStatementText.setTextColor(getColor(R.color.not_click));
                ptmMiniStatementText.setTypeface(typefaceRegular);
                ptmMiniStatementLine.setBackgroundColor(getColor(R.color.not_click));

                loadFragment(new PtmEnquiryFragment(PaytmAEPSActivity.this));
            }
        });
        ptmMiniStatementLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ptmWithdrawalText.setTextColor(getColor(R.color.not_click));
                ptmWithdrawalText.setTypeface(typefaceRegular);
                ptmWithdrawalLine.setBackgroundColor(getColor(R.color.not_click));
                ptmEnquiryText.setTextColor(getColor(R.color.not_click));
                ptmEnquiryText.setTypeface(typefaceRegular);
                ptmEnquiryLine.setBackgroundColor(getColor(R.color.not_click));
                ptmMiniStatementText.setTextColor(getColor(R.color.dark_vigyos));
                ptmMiniStatementText.setTypeface(typefaceBold);
                ptmMiniStatementLine.setBackgroundColor(getColor(R.color.dark_vigyos));

                loadFragment(new PtmMiniStatementFragment(PaytmAEPSActivity .this));
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (!fragmentManager.isStateSaved()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.ptmFrameContainer, fragment);
            transaction.commit();
        }
    }
}