package com.vigyos.vigyoscentercrm.Adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vigyos.vigyoscentercrm.Fragment.CancelledFragment;
import com.vigyos.vigyoscentercrm.Fragment.CompletedFragment;
import com.vigyos.vigyoscentercrm.Fragment.PendingFragment;
import com.vigyos.vigyoscentercrm.Fragment.ProcessingFragment;

public class OrderAdapter extends FragmentStateAdapter {

    private Activity activity;

    public OrderAdapter(@NonNull FragmentActivity fragmentActivity,  Activity activity) {
        super(fragmentActivity);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 1: return new ProcessingFragment();
            case 2: return new CompletedFragment(activity);
            case 3: return new CancelledFragment(activity);
            default: return new PendingFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}