package com.vigyos.vigyoscentercrm.Fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.vigyos.vigyoscentercrm.Adapter.OrderAdapter;
import com.vigyos.vigyoscentercrm.R;

public class OrderFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private OrderAdapter adapter;
    private Activity activity;

    public OrderFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_order, container, false);
         tabLayout = view.findViewById(R.id.tablayout);
         viewPager2 = view.findViewById(R.id.viewpager);
         adapter = new OrderAdapter(getActivity(), activity);
         viewPager2.setAdapter(adapter);
         tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
             @Override
             public void onTabSelected(TabLayout.Tab tab) {
                    viewPager2.setCurrentItem(tab.getPosition());
             }
             @Override
             public void onTabUnselected(TabLayout.Tab tab) { }
             @Override
             public void onTabReselected(TabLayout.Tab tab) { }
         });

         viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
             @Override
             public void onPageSelected(int position) {
                 super.onPageSelected(position);
                 tabLayout.getTabAt(position).select();
             }
         });
        return view;
    }
}