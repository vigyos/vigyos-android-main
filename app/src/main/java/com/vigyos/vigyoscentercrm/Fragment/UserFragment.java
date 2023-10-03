package com.vigyos.vigyoscentercrm.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vigyos.vigyoscentercrm.Activity.PayOutActivity;
import com.vigyos.vigyoscentercrm.Adapter.AdapterForUser;
import com.vigyos.vigyoscentercrm.AppController;
import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Utils.UserItemListener;

public class UserFragment extends Fragment  implements UserItemListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        String[] list = {"Account Information", "Payout Balance", "Refund Policy", "Terms and Conditions", "Privacy Policy"};
        int[] theBitmapIds = { R.drawable.person_dark, R.drawable.payout_icon, R.drawable.refund_icon, R.drawable.terms_icon, R.drawable.privacy_icon};
        RecyclerView recyclerView = view.findViewById(R.id.profile_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new AdapterForUser(list, this, theBitmapIds));
        return view;
    }

    @Override
    public void onItemClick(int position) {
        switch (position){
            case 0:
                fragmentCall(new AccountFragment());
                AppController.backCheck = false;
                break;
            case 1:
                startActivity(new Intent(getActivity(), PayOutActivity.class));
                AppController.backCheck = false;
                break;
            case 2:
                fragmentCall(new RefundPolicyFragment());
                AppController.backCheck = false;
                break;
            case 3:
                fragmentCall(new TermsAndConditionsFragment());
                AppController.backCheck = false;
                break;
            case 4:
                fragmentCall(new PrivacyPolicyFragment());
                AppController.backCheck = false;
                break;
//            case 5:
//                fragmentCall(new FeedBackFragment());
//                break;
            default:
                break;
        }
    }

    private void fragmentCall(Fragment fragment){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment); // R.id.fragment_container is the container in your activity layout where fragments are placed
        fragmentTransaction.addToBackStack(null); // This allows the user to navigate back to FragmentA when they press the back button
        fragmentTransaction.commit();
    }
}