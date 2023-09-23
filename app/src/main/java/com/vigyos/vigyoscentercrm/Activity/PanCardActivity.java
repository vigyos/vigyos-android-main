package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.vigyos.vigyoscentercrm.Adapter.PanCardFragmentAdapter;
import com.vigyos.vigyoscentercrm.R;
import com.google.android.material.tabs.TabLayout;

public class PanCardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private TabLayout panCard_Tab;
    private ViewPager2 viewPager2;
    private PanCardFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pan_card);
        panCard_Tab = findViewById(R.id.tab_panCard);
        viewPager2 = findViewById(R.id.view_pager2);
        panCard_Tab.addTab(panCard_Tab.newTab().setText("Create PAN"));
        panCard_Tab.addTab(panCard_Tab.newTab().setText("Update PAN"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new PanCardFragmentAdapter(fragmentManager, getLifecycle(), PanCardActivity.this);
        viewPager2.setAdapter(adapter);
        ViewGroup tabs = (ViewGroup) panCard_Tab.getChildAt(0);

        for (int i = 0; i < tabs.getChildCount() - 1; i++) {
            View tab = tabs.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tab.getLayoutParams();
            layoutParams.weight = 0;
            layoutParams.setMarginEnd(12);
            layoutParams.setMarginEnd(12);
            layoutParams.width = 10;
            tab.setLayoutParams(layoutParams);
            panCard_Tab.requestLayout();
        }

        panCard_Tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
                panCard_Tab.selectTab(panCard_Tab.getTabAt(position));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }
}