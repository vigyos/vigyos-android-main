package com.vigyos.vigyoscentercrm.Adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vigyos.vigyoscentercrm.Fragment.EnquiryFragment;
import com.vigyos.vigyoscentercrm.Fragment.MiniStatementFragment;
import com.vigyos.vigyoscentercrm.Fragment.WithdrawlFragment;

public class AadhaarFragmentAdapter extends FragmentStateAdapter {

    private Activity activity;

    public AadhaarFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Activity activity) {
        super(fragmentManager, lifecycle);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position ==0 ){
            return new WithdrawlFragment(activity);
        } else if (position==1) {
            return new EnquiryFragment(activity);
        }
//        else if (position==1){
//            return new AadhaarPayFragment();
//        }
        else {
            return new MiniStatementFragment(activity);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}