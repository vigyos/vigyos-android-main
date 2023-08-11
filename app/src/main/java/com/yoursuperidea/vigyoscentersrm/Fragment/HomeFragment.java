package com.yoursuperidea.vigyoscentersrm.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.yoursuperidea.vigyoscentersrm.Activity.SplashActivity;
import com.yoursuperidea.vigyoscentersrm.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private CardView cardView, offer_card, see_more_btn;
    private ImageView img;
    private TextView userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cardView = view.findViewById(R.id.Total_cardview);
        offer_card = view.findViewById(R.id.offer_card);
        see_more_btn = view.findViewById(R.id.seemore_topservices);
        userName = view.findViewById(R.id.userNameHome);
        userName.setText(SplashActivity.prefManager.getFirstName()+" "+SplashActivity.prefManager.getLastName());

        img = view.findViewById(R.id.person);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new UserFragment());
            }
        });

        ArrayList<SlideModel> img = new ArrayList<>();
//        img.add(new SlideModel("https://bit.ly/2YoJ77H", "", ScaleTypes.CENTER_CROP));
//        img.add(new SlideModel("https://bit.ly/2BteuF2", "", ScaleTypes.CENTER_CROP));
//        img.add(new SlideModel("https://bit.ly/3fLJf72", "", ScaleTypes.CENTER_CROP));
        img.add(new SlideModel(R.drawable.vigyos_banner1, "22", ScaleTypes.CENTER_INSIDE));
        img.add(new SlideModel(R.drawable.vigyos_banner1, "33", ScaleTypes.CENTER_INSIDE));
        img.add(new SlideModel(R.drawable.vigyos_banner1, "11", ScaleTypes.CENTER_INSIDE));


        ImageSlider imageSlider = view.findViewById(R.id.image_slider);
        imageSlider.setImageList(img);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new WalletFragment());
            }
        });

        offer_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        see_more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new SeeMore_TopServicesFragment());
            }
        });

        return view;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment); // R.id.fragment_container is the container in your activity layout where fragments are placed
        fragmentTransaction.addToBackStack(null); // This allows the user to navigate back to FragmentA when they press the back button
        fragmentTransaction.commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}