package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.vigyos.vigyoscentercrm.Adapter.AdapterForHistory;
import com.vigyos.vigyoscentercrm.R;

public class WalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        String[] service = {"Add Money in Wallet", "Aadhaar card update", "PanCard service", "Add Money in Wallet", "Gem Registration", "Advertisement","Add Money in Wallet", "Add Money in Wallet", "PanCard service", "Add Money in Wallet", "Gem Registration", "Advertisement"};
        String[] money = {"+200 Rs","-50 Rs","-100 Rs","+520 Rs","-200 Rs","-100 Rs","+200 Rs","+50 Rs","-100 Rs","+520 Rs","-200 Rs","-100 Rs"};

        RecyclerView newrecyclerView = findViewById(R.id.wallet_recycler);
        newrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newrecyclerView.setAdapter(new AdapterForHistory(service, money));

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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}