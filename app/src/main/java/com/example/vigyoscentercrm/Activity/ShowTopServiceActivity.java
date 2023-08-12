package com.example.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.vigyoscentercrm.R;

public class ShowTopServiceActivity extends AppCompatActivity {

    private Intent intent;
    private TextView serviceName, serviceDetails;
    private TextView priceText, document;
    private String name, details;
    private String price, documentRequired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_top_service);

        intent = getIntent();
        name = intent.getStringExtra("name");
        details = intent.getStringExtra("Description");
        price = intent.getStringExtra("price");
        documentRequired = intent.getStringExtra("RequiredDocument");

        serviceName = findViewById(R.id.serviceName);
        serviceDetails = findViewById(R.id.serviceDetails);
        priceText = findViewById(R.id.price);
        document = findViewById(R.id.document);

        serviceName.setText(name);
        serviceDetails.setText(details);
        priceText.setText(price);
        document.setText(documentRequired);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(ShowTopServiceActivity.this, MainActivity.class));
        finish();
    }
}