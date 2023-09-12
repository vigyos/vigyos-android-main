package com.example.vigyoscentercrm.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.example.vigyoscentercrm.Activity.LoginActivity;
import com.example.vigyoscentercrm.Activity.SplashActivity;
import com.example.vigyoscentercrm.R;

public class AccountFragment extends Fragment {

    private TextView userName;
    private TextView userPhone;
    private TextView userEmail;
    private TextView userAddress;
    private RelativeLayout logout;
    private View view;
    private CardView viewAadhaarCard;
    private TextView userAadhaar;
    private CardView viewPanCard;
    private TextView userPenCard;
    private TextView licenseNumber;
    private Dialog dialog;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);
        initialization();
        declaration();
        return view;
    }
     private void initialization(){
         userName = view.findViewById(R.id.userName);
         userPhone = view.findViewById(R.id.userPhone);
         userEmail = view.findViewById(R.id.emailTextView);
         userAddress = view.findViewById(R.id.userAddress);
         logout = view.findViewById(R.id.logout);
         viewAadhaarCard = view.findViewById(R.id.viewAadhaarCard);
         userAadhaar = view.findViewById(R.id.userAadhaar);
         viewPanCard = view.findViewById(R.id.viewPanCard);
         userPenCard = view.findViewById(R.id.userPan);
         licenseNumber = view.findViewById(R.id.licenseNumber);
     }

     private void declaration(){
         userName.setText(SplashActivity.prefManager.getFirstName() + " " + SplashActivity.prefManager.getLastName());
         userPhone.setText(SplashActivity.prefManager.getPhone());
         userEmail.setText(SplashActivity.prefManager.getEmail());
         userAadhaar.setText(SplashActivity.prefManager.getAadhaarNumber());
         userPenCard.setText(SplashActivity.prefManager.getPanCardNumber());
         licenseNumber.setText(SplashActivity.prefManager.getLicenseNumber());

         logout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewpush));
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
         dialog = new Dialog(getActivity());
         dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
         dialog.setCancelable(true);
         dialog.setContentView(R.layout.dialog_view_image);
         ImageView viewImage = dialog.findViewById(R.id.viewImage);
         Picasso.get().load(Attachment).into(viewImage);
         dialog.show();
     }

    private void areYouSure(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure, You want to logout ?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        SplashActivity.prefManager.setClear();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
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
}