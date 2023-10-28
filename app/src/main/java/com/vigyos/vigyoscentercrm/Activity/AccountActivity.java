package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vigyos.vigyoscentercrm.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AccountActivity extends AppCompatActivity {

    private ImageView ivBack;
    private ImageView personIcon;
    private TextView userName;
    private TextView userPhone;
    private TextView userEmail;
    private TextView userAddress;
    private CardView viewAadhaarCard;
    private TextView userAadhaar;
    private CardView viewPanCard;
    private TextView userPenCard;
    private TextView licenseNumber;
    private TextView joinDate;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initialization();
        declaration();
    }

    private void initialization(){
        ivBack = findViewById(R.id.ivBack);
        personIcon = findViewById(R.id.person_icon);
        userName = findViewById(R.id.userName);
        userPhone = findViewById(R.id.userPhone);
        userEmail = findViewById(R.id.emailTextView);
        userAddress = findViewById(R.id.userAddress);
        viewAadhaarCard = findViewById(R.id.viewAadhaarCard);
        userAadhaar = findViewById(R.id.userAadhaar);
        viewPanCard = findViewById(R.id.viewPanCard);
        userPenCard = findViewById(R.id.userPan);
        licenseNumber = findViewById(R.id.licenseNumber);
        joinDate = findViewById(R.id.joinDate);
    }

    private void declaration(){
        userName.setText(SplashActivity.prefManager.getFirstName() + " " + SplashActivity.prefManager.getLastName());
        userPhone.setText(SplashActivity.prefManager.getPhone());
        userEmail.setText(SplashActivity.prefManager.getEmail());
        userAddress.setText(SplashActivity.prefManager.getCity() + ", "+ SplashActivity.prefManager.getState());
        userAadhaar.setText(SplashActivity.prefManager.getAadhaarNumber());
        userPenCard.setText(SplashActivity.prefManager.getPanCardNumber());
        licenseNumber.setText(SplashActivity.prefManager.getLicenseNumber());
        joinDate.setText(formatTimestamp(SplashActivity.prefManager.getJoinDate()));

        if (SplashActivity.prefManager.getProfilePicture().equalsIgnoreCase("null") || SplashActivity.prefManager.getProfilePicture().equalsIgnoreCase("")) {
            personIcon.setBackgroundResource(R.drawable.user_icon);
        } else {
            Picasso.get().load(SplashActivity.prefManager.getProfilePicture()).into(personIcon);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        viewAadhaarCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewImage(SplashActivity.prefManager.getAadhaarAttachment());
            }
        });
        viewPanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewImage(SplashActivity.prefManager.getPanCardAttachment());
            }
        });
    }

    private String formatTimestamp(long timestamp) {
        Date date = new Date(timestamp * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return formatter.format(date);
    }

    private void viewImage(String Attachment){
        dialog = new Dialog(AccountActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_view_image);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.color.transparent));
        ImageView viewImage = dialog.findViewById(R.id.viewImage);
        Picasso.get().load(Attachment).into(viewImage);
        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}