package com.yoursuperidea.vigyoscentersrm.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yoursuperidea.vigyoscentersrm.Adapter.AdapterforUser;
import com.yoursuperidea.vigyoscentersrm.R;
import com.yoursuperidea.vigyoscentersrm.Utils.UserItemListener;

public class UserFragment extends Fragment  implements UserItemListener {

    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        String[] list = {"Account Information", "Wishlist", "Refund Policy", "Terms and Conditions", "Privacy Policy", "Feedback"};

        int[] theBitmapIds = { R.drawable.person_dark, R.drawable.wishlist_icon, R.drawable.refund_icon,
                 R.drawable.terms_icon, R.drawable.privacy_icon, R.drawable.feedback_icon };

        recyclerView = view.findViewById(R.id.profile_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new AdapterforUser(list, this, theBitmapIds));

        return view;
    }

    @Override
    public void onItemClick(int position) {
        switch (position){
            case 0:
                fragmentCall(new AccountFragment());
                break;
            case 1:
                fragmentCall(new WishlistFragment());
                break;
            case 2:
                fragmentCall(new RefundPolicyFragment());
                break;
            case 3:
                fragmentCall(new TermsAndConditionsFragment());
                break;
            case 4:
                fragmentCall(new PrivacyPolicyFragment());
                break;
            case 5:
                fragmentCall(new FeedBackFragment());
                break;
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