package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vigyos.vigyoscentercrm.R;

@BuildCompat.PrereleaseSdkCheck
public class DocumentsActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView aadhaarNumber, panCardNumber;
    private RelativeLayout aadhaarView, panCardView;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);
        Initialization();
        Declaration();
    }
    private void Initialization() {
        ivBack = findViewById(R.id.ivBack);
        aadhaarNumber = findViewById(R.id.aadhaarNumber);
        panCardNumber = findViewById(R.id.panCardNumber);
        aadhaarView = findViewById(R.id.aadhaarView);
        panCardView = findViewById(R.id.panCardView);
    }

    private void Declaration() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        aadhaarNumber.setText(SplashActivity.prefManager.getAadhaarNumber());
        panCardNumber.setText(SplashActivity.prefManager.getPanCardNumber());
        aadhaarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(DocumentsActivity.this, R.anim.viewpush));
                viewImage(SplashActivity.prefManager.getAadhaarAttachment());
            }
        });
        panCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(DocumentsActivity.this, R.anim.viewpush));
                viewImage(SplashActivity.prefManager.getPanCardAttachment());
            }
        });
    }

    private void viewImage(String Attachment){
        dialog = new Dialog(DocumentsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_view_image);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.color.transparent));
        dialog.getWindow().setLayout(-1, -1);
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
}