package com.vigyos.vigyoscentercrm.Fragment;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.vigyos.vigyoscentercrm.Fragment.AEPS.FinoBank.FinoAepsHistoryFragment;
import com.vigyos.vigyoscentercrm.Fragment.AEPS.FinoBank.FinoPayoutHistoryFragment;
import com.vigyos.vigyoscentercrm.R;

@BuildCompat.PrereleaseSdkCheck
public class HistoryFragment extends Fragment {

    private Activity activity;
    private RelativeLayout walletLyt, aepsLyt, payoutLyt, bbpsLyt;
    private TextView walletText, aepsText, payoutText, bbpsText;
    private View walletLine, aepsLine, payoutLine, bbpsLine;
    private Typeface typefaceBold;
    private Typeface typefaceRegular;

    public HistoryFragment(Activity activity) {
        this.activity =  activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        initialization(view);
        declaration();
        return view;
    }

    private void initialization(View view) {
        walletLyt = view.findViewById(R.id.walletLyt);
        aepsLyt = view.findViewById(R.id.aepsLyt);
        payoutLyt = view.findViewById(R.id.payoutLyt);
        walletText = view.findViewById(R.id.walletText);
        aepsText = view.findViewById(R.id.aepsText);
        payoutText = view.findViewById(R.id.payoutText);
        walletLine = view.findViewById(R.id.walletLine);
        aepsLine = view.findViewById(R.id.aepsLine);
        payoutLine = view.findViewById(R.id.payoutLine);
        bbpsLyt = view.findViewById(R.id.bbpsLyt);
        bbpsText = view.findViewById(R.id.bbpsText);
        bbpsLine = view.findViewById(R.id.bbpsLine);
    }

    private void declaration() {
        loadFragment(new WalletHistoryFragment(activity));
        tabLayout();
    }

    private void tabLayout () {
        typefaceBold = ResourcesCompat.getFont(activity, R.font.poppins_semi_bold);
        typefaceRegular = ResourcesCompat.getFont(activity, R.font.poppins_regular);
        walletLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletText.setTextColor(activity.getColor(R.color.dark_vigyos));
                walletText.setTypeface(typefaceBold);
                walletLine.setBackgroundColor(activity.getColor(R.color.dark_vigyos));
                aepsText.setTextColor(activity.getColor(R.color.not_click));
                aepsText.setTypeface(typefaceRegular);
                aepsLine.setBackgroundColor(activity.getColor(R.color.not_click));
                payoutText.setTextColor(activity.getColor(R.color.not_click));
                payoutText.setTypeface(typefaceRegular);
                payoutLine.setBackgroundColor(activity.getColor(R.color.not_click));
                bbpsText.setTextColor(activity.getColor(R.color.not_click));
                bbpsText.setTypeface(typefaceRegular);
                bbpsLine.setBackgroundColor(activity.getColor(R.color.not_click));

                loadFragment(new WalletHistoryFragment(activity));
            }
        });
        aepsLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletText.setTextColor(activity.getColor(R.color.not_click));
                walletText.setTypeface(typefaceRegular);
                walletLine.setBackgroundColor(activity.getColor(R.color.not_click));
                aepsText.setTextColor(activity.getColor(R.color.dark_vigyos));
                aepsText.setTypeface(typefaceBold);
                aepsLine.setBackgroundColor(activity.getColor(R.color.dark_vigyos));
                payoutText.setTextColor(activity.getColor(R.color.not_click));
                payoutText.setTypeface(typefaceRegular);
                payoutLine.setBackgroundColor(activity.getColor(R.color.not_click));
                bbpsText.setTextColor(activity.getColor(R.color.not_click));
                bbpsText.setTypeface(typefaceRegular);
                bbpsLine.setBackgroundColor(activity.getColor(R.color.not_click));

                loadFragment(new FinoAepsHistoryFragment(activity));
            }
        });
        payoutLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletText.setTextColor(activity.getColor(R.color.not_click));
                walletText.setTypeface(typefaceRegular);
                walletLine.setBackgroundColor(activity.getColor(R.color.not_click));
                aepsText.setTextColor(activity.getColor(R.color.not_click));
                aepsText.setTypeface(typefaceRegular);
                aepsLine.setBackgroundColor(activity.getColor(R.color.not_click));
                payoutText.setTextColor(activity.getColor(R.color.dark_vigyos));
                payoutText.setTypeface(typefaceBold);
                payoutLine.setBackgroundColor(activity.getColor(R.color.dark_vigyos));
                bbpsText.setTextColor(activity.getColor(R.color.not_click));
                bbpsText.setTypeface(typefaceRegular);
                bbpsLine.setBackgroundColor(activity.getColor(R.color.not_click));

                loadFragment(new FinoPayoutHistoryFragment(activity));
            }
        });
        bbpsLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletText.setTextColor(activity.getColor(R.color.not_click));
                walletText.setTypeface(typefaceRegular);
                walletLine.setBackgroundColor(activity.getColor(R.color.not_click));
                aepsText.setTextColor(activity.getColor(R.color.not_click));
                aepsText.setTypeface(typefaceRegular);
                aepsLine.setBackgroundColor(activity.getColor(R.color.not_click));
                payoutText.setTextColor(activity.getColor(R.color.not_click));
                payoutText.setTypeface(typefaceRegular);
                payoutLine.setBackgroundColor(activity.getColor(R.color.not_click));
                bbpsText.setTextColor(activity.getColor(R.color.dark_vigyos));
                bbpsText.setTypeface(typefaceBold);
                bbpsLine.setBackgroundColor(activity.getColor(R.color.dark_vigyos));

                loadFragment(new BBPSHistoryFragment(activity));
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        if (!fragmentManager.isStateSaved()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frameContainer, fragment);
            transaction.commit();
        }
    }
}