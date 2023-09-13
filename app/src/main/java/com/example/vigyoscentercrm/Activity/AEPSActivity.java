package com.example.vigyoscentercrm.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.vigyoscentercrm.Adapter.AadhaarFragmentAdapter;
import com.example.vigyoscentercrm.R;
import com.example.vigyoscentercrm.Retrofit.RetrofitClient;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;

public class AEPSActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private AadhaarFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aeps);
        tabLayout=findViewById(R.id.tab_aadhaar_pay);
        viewPager2= findViewById(R.id.view_pager2);

        tabLayout.addTab(tabLayout.newTab().setText("Withdraw"));
//        tabLayout.addTab(tabLayout.newTab().setText("Aadhaar Pay"));
        tabLayout.addTab(tabLayout.newTab().setText("Enquiry"));
        tabLayout.addTab(tabLayout.newTab().setText("Mini statement"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new AadhaarFragmentAdapter(fragmentManager,getLifecycle(), AEPSActivity.this);
        viewPager2.setAdapter(adapter);
        ViewGroup tabs = (ViewGroup) tabLayout.getChildAt(0);

        for (int i = 0; i < tabs.getChildCount() - 1; i++) {
            View tab = tabs.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tab.getLayoutParams();
            layoutParams.weight = 0;
            layoutParams.setMarginEnd(12);
            layoutParams.setMarginEnd(12);
            layoutParams.width = 10;
            tab.setLayoutParams(layoutParams);
            tabLayout.requestLayout();
        }

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
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }
}