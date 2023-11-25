package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vigyos.vigyoscentercrm.Adapter.OrderAdapter;
import com.vigyos.vigyoscentercrm.Adapter.PanCardFragmentAdapter;
import com.vigyos.vigyoscentercrm.Fragment.PanCardCreateFragment;
import com.vigyos.vigyoscentercrm.Fragment.PanCardUpdateFragment;
import com.vigyos.vigyoscentercrm.Fragment.ProfileFragment;
import com.vigyos.vigyoscentercrm.R;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

@BuildCompat.PrereleaseSdkCheck
public class PanCardActivity extends AppCompatActivity {

    private ImageView ivBack;
    private RelativeLayout newPan, updatePan;
    private TextView newText, updateText;
    private View newLine, updateLine;
    private Typeface typefaceBold;
    private Typeface typefaceRegular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pan_card);
        initialization();
        declaration();
    }

    private void initialization() {
        ivBack = findViewById(R.id.ivBack);
        newPan  = findViewById(R.id.newPan);
        updatePan  = findViewById(R.id.updatePan);
        newText  = findViewById(R.id.newText);
        updateText  = findViewById(R.id.updateText);
        newLine  = findViewById(R.id.newLine);
        updateLine  = findViewById(R.id.updateLine);
    }

    private void declaration() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        loadFragment(new PanCardCreateFragment(PanCardActivity.this), false);
        tabLayout();
    }

    private void tabLayout () {
        typefaceBold = ResourcesCompat.getFont(PanCardActivity.this, R.font.poppins_semi_bold);
        typefaceRegular = ResourcesCompat.getFont(PanCardActivity.this, R.font.poppins_regular);
        newPan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newText.setTextColor(getColor(R.color.dark_vigyos));
                newText.setTypeface(typefaceBold);
                newLine.setBackgroundColor(getColor(R.color.dark_vigyos));
                updateText.setTextColor(getColor(R.color.not_click));
                updateText.setTypeface(typefaceRegular);
                updateLine.setBackgroundColor(getColor(R.color.not_click));

                loadFragment(new PanCardCreateFragment(PanCardActivity.this), false);
            }
        });
        updatePan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newText.setTextColor(getColor(R.color.not_click));
                newText.setTypeface(typefaceRegular);
                newLine.setBackgroundColor(getColor(R.color.not_click));
                updateText.setTextColor(getColor(R.color.dark_vigyos));
                updateText.setTypeface(typefaceBold);
                updateLine.setBackgroundColor(getColor(R.color.dark_vigyos));

                loadFragment(new PanCardUpdateFragment(PanCardActivity.this), false);
            }
        });
    }

    public void loadFragment(Fragment fragment, boolean flag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (!fragmentManager.isStateSaved()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}