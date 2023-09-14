package com.example.vigyoscentercrm.Adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.vigyoscentercrm.Fragment.PanCardUpdateFragment;
import com.example.vigyoscentercrm.Fragment.PancardCreateFragment;

public class PanCardFragmentAdapter extends FragmentStateAdapter {

    private Activity activity;

    public PanCardFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Activity activity) {
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