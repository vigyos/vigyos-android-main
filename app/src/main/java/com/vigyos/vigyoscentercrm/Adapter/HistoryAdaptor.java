package com.vigyos.vigyoscentercrm.Adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vigyos.vigyoscentercrm.Fragment.AepsHistoryFragment;
import com.vigyos.vigyoscentercrm.Fragment.CancelledFragment;
import com.vigyos.vigyoscentercrm.Fragment.CompletedFragment;
import com.vigyos.vigyoscentercrm.Fragment.PayoutHistoryFragment;
import com.vigyos.vigyoscentercrm.Fragment.ProcessingFragment;
import com.vigyos.vigyoscentercrm.Fragment.WalletHistoryFragment;

public class HistoryAdaptor extends FragmentStateAdapter {

    private Activity activity;

    public HistoryAdaptor(@NonNull FragmentActivity fragmentActivity, Activity activity) {
        super(fragmentActivity);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 1: return new AepsHistoryFragment(activity);
            case 2: return new PayoutHistoryFragment(activity);
            default: return new WalletHistoryFragment(activity);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}