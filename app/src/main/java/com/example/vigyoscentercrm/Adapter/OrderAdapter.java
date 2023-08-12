package com.example.vigyoscentercrm.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.vigyoscentercrm.Fragment.CancelledFragment;
import com.example.vigyoscentercrm.Fragment.CompletedFragment;
import com.example.vigyoscentercrm.Fragment.ProcessingFragment;

public class OrderAdapter extends FragmentStateAdapter {

    public OrderAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch(position){
            case 0: return new CompletedFragment();
            case 1: return new ProcessingFragment();
            case 2: return new CancelledFragment();
            default: return new CompletedFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
