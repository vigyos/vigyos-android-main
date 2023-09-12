package com.example.vigyoscentercrm.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.vigyoscentercrm.Activity.AEPSActivity;
import com.example.vigyoscentercrm.Activity.SearchServicesActivity;
import com.example.vigyoscentercrm.Activity.ShowTopServiceActivity;
import com.example.vigyoscentercrm.Activity.SplashActivity;
import com.example.vigyoscentercrm.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {

    public View view;
    private TextView userName;
    private ImageView userAccount;
    private ImageSlider imageSlider;
    private LinearLayout walletView;
    private LinearLayout seeMore;
    private LinearLayout gstRegistration, tdsReturn;
    private LinearLayout eWay, itr1;
    private LinearLayout udyamRegistration;
    private LinearLayout aeps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initialization();
        declaration();
        return view;
    }

    private void initialization(){
        userName = view.findViewById(R.id.userNameHome);
        userAccount = view.findViewById(R.id.profile_image);
        imageSlider = view.findViewById(R.id.image_slider);
        walletView = view.findViewById(R.id.walletView);
        gstRegistration = view.findViewById(R.id.gstRegistration);
        tdsReturn = view.findViewById(R.id.tdsReturn);
        eWay = view.findViewById(R.id.eWay);
        itr1 = view.findViewById(R.id.itr1);
        udyamRegistration = view.findViewById(R.id.udyamRegistration);
        seeMore = view.findViewById(R.id.seeMore);
        aeps = view.findViewById(R.id.aeps);
    }

    private void declaration(){
        //SetListeners
        userAccount.setOnClickListener(this);
        imageSlider.setOnClickListener(this);
        walletView.setOnClickListener(this);
        gstRegistration.setOnClickListener(this);
        tdsReturn.setOnClickListener(this);
        eWay.setOnClickListener(this);
        itr1.setOnClickListener(this);
        udyamRegistration.setOnClickListener(this);
        seeMore.setOnClickListener(this);
        aeps.setOnClickListener(this);

        userName.setText(SplashActivity.prefManager.getFirstName()+" "+SplashActivity.prefManager.getLastName());
        ArrayList<SlideModel> img = new ArrayList<>();
        img.add(new SlideModel(R.drawable.banner_1,  ScaleTypes.CENTER_INSIDE));
        img.add(new SlideModel(R.drawable.banner_2, ScaleTypes.FIT));
        img.add(new SlideModel(R.drawable.banner_3, ScaleTypes.CENTER_INSIDE));
        imageSlider.setImageList(img);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.aeps:
                startActivity(new Intent(getActivity(), AEPSActivity.class));
                break;
            case R.id.profile_image:
                replaceFragment(new UserFragment());
                break;
            case R.id.walletView:
                replaceFragment(new WalletFragment());
                break;
            case R.id.gstRegistration:
                Intent intent = new Intent(getActivity(), ShowTopServiceActivity.class);
                intent.putExtra("name", getString(R.string.gst_service));
                intent.putExtra("Description", getString(R.string.gst_Description));
                intent.putExtra("price", getString(R.string.gst_price));
                intent.putExtra("RequiredDocument", getString(R.string.gst_Required_Document));
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.tdsReturn:
                Intent intent1 = new Intent(getActivity(), ShowTopServiceActivity.class);
                intent1.putExtra("name", getString(R.string.TDS_Return));
                intent1.putExtra("Description", getString(R.string.TDS_Description));
                intent1.putExtra("price", getString(R.string.TDS_Price));
                intent1.putExtra("RequiredDocument", getString(R.string.TDS_RequiredDocument));
                startActivity(intent1);
                getActivity().finish();
                break;
            case R.id.eWay:
                Intent intent2 = new Intent(getActivity(), ShowTopServiceActivity.class);
                intent2.putExtra("name", getString(R.string.Eway));
                intent2.putExtra("Description", getString(R.string.Eway_Description));
                intent2.putExtra("price", getString(R.string.Eway_Price));
                intent2.putExtra("RequiredDocument", getString(R.string.Eway_RequiredDocument));
                startActivity(intent2);
                getActivity().finish();
                break;
            case R.id.itr1:
                Intent intent3 = new Intent(getActivity(), ShowTopServiceActivity.class);
                intent3.putExtra("name", getString(R.string.ITR1));
                intent3.putExtra("Description", getString(R.string.ITR1_Description));
                intent3.putExtra("price", getString(R.string.ITR1_Price));
                intent3.putExtra("RequiredDocument", getString(R.string.TDS_RequiredDocument));
                startActivity(intent3);
                getActivity().finish();
                break;
            case R.id.udyamRegistration:
                Intent intent4 = new Intent(getActivity(), ShowTopServiceActivity.class);
                intent4.putExtra("name", getString(R.string.Udyam_Registration));
                intent4.putExtra("Description", getString(R.string.Udyam_RegistrationDescription));
                intent4.putExtra("price", getString(R.string.Udyam_RegistrationPrice));
                intent4.putExtra("RequiredDocument", getString(R.string.Udyam_RegistrationRequiredDocument));
                startActivity(intent4);
                getActivity().finish();
                break;
            case R.id.seeMore:
                startActivity(new Intent(getActivity(), SearchServicesActivity.class));
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment); // R.id.fragment_container is the container in your activity layout where fragments are placed
        fragmentTransaction.addToBackStack(null); // This allows the user to navigate back to FragmentA when they press the back button
        fragmentTransaction.commit();
    }
}