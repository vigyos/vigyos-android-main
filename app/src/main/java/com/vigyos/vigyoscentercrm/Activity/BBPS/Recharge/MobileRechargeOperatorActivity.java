package com.vigyos.vigyoscentercrm.Activity.BBPS.Recharge;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import com.vigyos.vigyoscentercrm.R;

import io.github.muddz.styleabletoast.StyleableToast;

@BuildCompat.PrereleaseSdkCheck
public class MobileRechargeOperatorActivity extends AppCompatActivity {

    private ImageView ivBack;
    private EditText mobileNumber;
    private RelativeLayout confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_recharge_operator);
        Initialization();
        Declaration();
    }

    private void Initialization() {
        ivBack = findViewById(R.id.ivBack);
        mobileNumber = findViewById(R.id.mobileNumber);
        confirmButton = findViewById(R.id.confirmButton);
    }

    private void Declaration() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mobileNumber.requestFocus();
        mobileNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        int maxLength = 10;
        mobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(MobileRechargeOperatorActivity.this, R.anim.viewpush));
                if (TextUtils.isEmpty(mobileNumber.getText().toString())) {
                    StyleableToast.makeText(MobileRechargeOperatorActivity.this, "Enter mobile number to Recharge", Toast.LENGTH_LONG, R.style.myToastWarning).show();
                    return;
                } else {
                    if (!isValidMobileNumber(mobileNumber.getText().toString())){
                        mobileNumber.setError("Invalid Mobile Number");
                        Toast.makeText(MobileRechargeOperatorActivity.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Intent intent = new Intent(MobileRechargeOperatorActivity.this, MobileRechargeSelectPlanActivity.class);
                intent.putExtra("mobileNumber", mobileNumber.getText().toString());
                startActivity(intent);
            }
        });
    }

    public boolean isValidMobileNumber(String phoneNumber) {
        // Use a regular expression to match 10 digits
        String regex = "^[6-9][0-9]{9}$";
        return phoneNumber.matches(regex);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}