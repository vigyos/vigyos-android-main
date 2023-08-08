package com.yoursuperidea.vigyos.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yoursuperidea.vigyos.R;

public class NumberRegisterActivity extends AppCompatActivity {

    Button btn;
    EditText phn_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_register);

        btn = findViewById(R.id.btn_next);

        phn_number = findViewById(R.id.phone_number);
        phn_number.addTextChangedListener(loginTextWatcher);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("121212","click");
                Intent iName = new Intent(NumberRegisterActivity.this, NameRegisterActivity.class);
                startActivity(iName);
                finish();
            }
        });

    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String num  = phn_number.getText().toString();
            char[] num_array = num.toCharArray();

            btn.setEnabled(num_array.length==1);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}