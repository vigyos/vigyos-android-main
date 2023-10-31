package com.vigyos.vigyoscentercrm.Adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vigyos.vigyoscentercrm.Fragment.PanCardUpdateFragment;
import com.vigyos.vigyoscentercrm.Fragment.PanCardCreateFragment;

@BuildCompat.PrereleaseSdkCheck
public class PanCardFragmentAdapter extends FragmentStateAdapter {

    public Activity activity;

    public PanCardFragmentAdapter(@NonNull FragmentActivity fragmentActivity, Activity activity) {
        super(fragmentActivity);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new PanCardUpdateFragment(activity);
        }
        return new PanCardCreateFragment(activity);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}