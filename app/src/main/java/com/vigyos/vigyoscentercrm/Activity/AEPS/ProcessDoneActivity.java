package com.vigyos.vigyoscentercrm.Activity.AEPS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vigyos.vigyoscentercrm.R;

public class ProcessDoneActivity extends AppCompatActivity {

    public String messageStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_done);
        Intent intent = getIntent();
        messageStatus = intent.getStringExtra("messageStatus");
        String message = intent.getStringExtra("message");
        String bankName = intent.getStringExtra("bankName");
        String ackno = intent.getStringExtra("ackno");
        String amount = intent.getStringExtra("amount");
        String balanceamount = intent.getStringExtra("balanceamount");
        String aadhaarNumber = intent.getStringExtra("aadhaarNumber");
        String bankrrn = intent.getStringExtra("bankrrn");
        String bankiin = intent.getStringExtra("bankiin");
        String clientrefno = intent.getStringExtra("clientrefno");

        TextView title = findViewById(R.id.title);
        TextView bankName1 = findViewById(R.id.backName);
        TextView amountT = findViewById(R.id.amount);
        TextView balance = findViewById(R.id.balance);
        TextView aadhaarNumber1 = findViewById(R.id.aadhaarNumber);
        TextView ackNo = findViewById(R.id.ackNo);
        TextView reference = findViewById(R.id.reference);

        LinearLayout transactionLyt = findViewById(R.id.transactionLyt);

        if (messageStatus.equalsIgnoreCase("Enquiry")){
            transactionLyt.setVisibility(View.GONE);
        } else {
            amountT.setText("-₹"+amount);
        }

        title.setText(messageStatus);
        bankName1.setText(bankName);
        balance.setText("₹"+balanceamount);
        aadhaarNumber1.setText(aadhaarNumber);
        ackNo.setText(ackno);
        reference.setText(clientrefno);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        super.onBackPressed();
        finish();
    }
}