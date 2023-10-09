package com.vigyos.vigyoscentercrm.Adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vigyos.vigyoscentercrm.Fragment.EnquiryFragment;
import com.vigyos.vigyoscentercrm.Fragment.MiniStatementFragment;
import com.vigyos.vigyoscentercrm.Fragment.WithdrawalFragment;

public class AEPSFragmentAdapter extends FragmentStateAdapter {

    public Activity activity;

    public AEPSFragmentAdapter(@NonNull FragmentActivity fragmentActivity, Activity activity) {
        super(fragmentActivity);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 1: return new EnquiryFragment(activity);
            case 2: return new MiniStatementFragment(activity);
            default: return new WithdrawalFragment(activity) ;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}