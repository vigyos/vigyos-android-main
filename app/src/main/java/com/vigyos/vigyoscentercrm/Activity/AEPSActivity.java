package com.vigyos.vigyoscentercrm.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.vigyos.vigyoscentercrm.Adapter.AEPSFragmentAdapter;
import com.vigyos.vigyoscentercrm.R;
import com.google.android.material.tabs.TabLayout;

public class AEPSActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aeps);
        ImageView ivBack = findViewById(R.id.ivBack);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewpager);
        AEPSFragmentAdapter adapter = new AEPSFragmentAdapter(AEPSActivity.this, AEPSActivity.this);
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
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }
}