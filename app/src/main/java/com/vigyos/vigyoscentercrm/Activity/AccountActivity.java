package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class AccountActivity extends AppCompatActivity {

    private ImageView ivBack;
    private ImageView personIcon;
    private TextView userName;
    private TextView userPhone;
    private TextView userEmail;
    private TextView userAddress;
    private RelativeLayout logout;
    private CardView viewAadhaarCard;
    private TextView userAadhaar;
    private CardView viewPanCard;
    private TextView userPenCard;
    private TextView licenseNumber;
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
        logout = findViewById(R.id.logout);
        viewAadhaarCard = findViewById(R.id.viewAadhaarCard);
        userAadhaar = findViewById(R.id.userAadhaar);
        viewPanCard = findViewById(R.id.viewPanCard);
        userPenCard = findViewById(R.id.userPan);
        licenseNumber = findViewById(R.id.licenseNumber);
    }

    private void declaration(){
        userName.setText(SplashActivity.prefManager.getFirstName() + " " + SplashActivity.prefManager.getLastName());
        userPhone.setText(SplashActivity.prefManager.getPhone());
        userEmail.setText(SplashActivity.prefManager.getEmail());
        userAadhaar.setText(SplashActivity.prefManager.getAadhaarNumber());
        userPenCard.setText(SplashActivity.prefManager.getPanCardNumber());
        licenseNumber.setText(SplashActivity.prefManager.getLicenseNumber());
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
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(AccountActivity.this, R.anim.viewpush));
                areYouSure();
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

    private void viewImage(String Attachment){
        dialog = new Dialog(AccountActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_view_image);
        ImageView viewImage = dialog.findViewById(R.id.viewImage);
        Picasso.get().load(Attachment).into(viewImage);
        dialog.show();
    }

    private void areYouSure(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(AccountActivity.this);
        builder1.setMessage("Are you sure, You want to logout ?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        SplashActivity.prefManager.setClear();
                        startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                        finish();
                    }
                });
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
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