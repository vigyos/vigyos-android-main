package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vigyos.vigyoscentercrm.R;

public class ProcessDoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_done);
        Intent intent = getIntent();
        String messageStatus = intent.getStringExtra("messageStatus");
        String message = intent.getStringExtra("message");
        String bankName = intent.getStringExtra("bankName");
        String ackno = intent.getStringExtra("ackno");
        String amount = intent.getStringExtra("amount");
        String balanceamount = intent.getStringExtra("balanceamount");
        String aadhaarNumber = intent.getStringExtra("aadhaarNumber");
        String bankrrn = intent.getStringExtra("bankrrn");
        String bankiin = intent.getStringExtra("bankiin");
        String clientrefno = intent.getStringExtra("clientrefno");

        TextView paySuccess = findViewById(R.id.paySuccess);
        TextView bankName1 = findViewById(R.id.backName);
        TextView amountT = findViewById(R.id.amount);
        TextView balance = findViewById(R.id.balance);
        TextView aadhaarNumber1 = findViewById(R.id.aadhaarNumber);
        TextView ackNo = findViewById(R.id.ackNo);
        TextView reference = findViewById(R.id.reference);
        TextView message1 = findViewById(R.id.message);
        LinearLayout transactionLyt = findViewById(R.id.transactionLyt);

        if (messageStatus.equalsIgnoreCase("Enquiry Successful!")){
            transactionLyt.setVisibility(View.GONE);
        }

        paySuccess.setText(messageStatus);
        bankName1.setText(bankName);
//        amountT.setText(amount);
        balance.setText(balanceamount);
        aadhaarNumber1.setText(aadhaarNumber);
        ackNo.setText(ackno);
        reference.setText(clientrefno);
        message1.setText(message);

        findViewById(R.id.Continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(ProcessDoneActivity.this, R.anim.viewpush));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}