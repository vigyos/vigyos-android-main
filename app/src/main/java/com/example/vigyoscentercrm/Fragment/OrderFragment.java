package com.example.vigyoscentercrm.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.example.vigyoscentercrm.Adapter.OrderAdapter;
import com.example.vigyoscentercrm.R;


public class OrderFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private OrderAdapter adapter;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_order, container, false);
         tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
         viewPager2 = (ViewPager2) view.findViewById(R.id.viewpager);
         adapter = new OrderAdapter(getActivity());
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