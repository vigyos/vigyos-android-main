package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vigyos.vigyoscentercrm.R;

import de.hdodenhof.circleimageview.CircleImageView;

@BuildCompat.PrereleaseSdkCheck
public class PersonalProfileActivity extends AppCompatActivity {

    private ImageView ivBack;
    private CircleImageView userIcon;
    private TextView firstName, lastName;
    private TextView phoneNumber, emailID;
    private TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_prolife);
        Initialization();
        Declaration();
    }

    private void Initialization() {
        ivBack = findViewById(R.id.ivBack);
        userIcon = findViewById(R.id.userIcon);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        phoneNumber = findViewById(R.id.phoneNumber);
        emailID = findViewById(R.id.emailID);
        address = findViewById(R.id.address);
    }

    private void Declaration() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (SplashActivity.prefManager.getProfilePicture().equalsIgnoreCase("null") || SplashActivity.prefManager.getProfilePicture().equalsIgnoreCase("")) {
            userIcon.setBackgroundResource(R.drawable.user_icon);
        } else {
            Picasso.get().load(SplashActivity.prefManager.getProfilePicture()).into(userIcon);
        }
        firstName.setText(SplashActivity.prefManager.getFirstName());
        lastName.setText(SplashActivity.prefManager.getLastName());
        phoneNumber.setText("+91-"+SplashActivity.prefManager.getPhone());
        emailID.setText(SplashActivity.prefManager.getEmail());
        address.setText(SplashActivity.prefManager.getCity()+" "+SplashActivity.prefManager.getState()+", India");
    }
}