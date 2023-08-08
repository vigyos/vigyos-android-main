package com.yoursuperidea.vigyos.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yoursuperidea.vigyos.R;

public class NameRegisterActivity extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_register);

        btn = findViewById(R.id.btn_name_next);

       btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent iHome = new Intent(NameRegisterActivity.this, MainActivity.class);
               startActivity(iHome);
               finish();
           }
       });


    }
}