package com.example.vigyoscentercrm.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.vigyoscentercrm.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PanCardUpdateFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private Spinner spinner;
    private View view;
    private Activity activity1;
    RelativeLayout update;
    EditText firstName,middleName,lastName,mobile_number,email;

    public PanCardUpdateFragment() {}

    public PanCardUpdateFragment(Activity activity) {
        this.activity1 = activity;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pan_card_update, container, false);
        initialization();
        declaration();
        return view;
    }

    private void declaration() {
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(activity1, R.array.gender,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter2);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) activity1);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(firstName.getText().toString())) {
                    firstName.setError("This field is required");
                }
                if (TextUtils.isEmpty(lastName.getText().toString())) {
                    lastName.setError("This field is required");
                }
                if (spinner.getSelectedItem().toString().trim().equals("--Gender--")) {
                    Toast.makeText(activity1, "Please select your Gender", Toast.LENGTH_SHORT).show();
                }
                if ((TextUtils.isEmpty(mobile_number.getText().toString())) || (!isValidPhone(mobile_number.getText().toString()))) {
                mobile_number.setError("Invalid Number");
                }
                if ((TextUtils.isEmpty(email.getText().toString())) || (!isValidMail(mobile_number.getText().toString()))) {
                    email.setError("Invalid Email");
                }
            }});
    }

    private void initialization() {
        spinner=view.findViewById(R.id.gender);
        update=view.findViewById(R.id.button_update);
        firstName=view.findViewById(R.id.firstName);
        middleName=view.findViewById(R.id.middle_name);
        lastName=view.findViewById(R.id.last_name);
        mobile_number=view.findViewById(R.id.mobile_number);
        email=view.findViewById(R.id.email);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }
    public boolean isValidPhone(String num) {
        //long ph = Long.parseLong(num);
        Pattern ptrn = Pattern.compile("[6-9][0-9]{9}");
        //the matcher() method creates a matcher that will match the given input against this pattern
        Matcher match = ptrn.matcher(num);
        //returns a boolean value
        return (match.find() && match.group().equals(num));
    }

    private boolean isValidMail(String email) {
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//        return Pattern.compile(EMAIL_STRING).matcher(email).matches();
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}