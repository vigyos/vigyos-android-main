package com.vigyos.vigyoscentercrm.Adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vigyos.vigyoscentercrm.Fragment.PanCardUpdateFragment;
import com.vigyos.vigyoscentercrm.Fragment.PancardCreateFragment;

public class PayOutFragmentAdapter extends FragmentStateAdapter {

    private Activity activity;

    public PayOutFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Activity activity) {
        super(fragmentManager, lifecycle);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position ==0 ){
            return new PancardCreateFragment(activity);
        } else {
            return new PanCardUpdateFragment(activity);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}