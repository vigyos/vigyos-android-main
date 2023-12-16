package com.vigyos.vigyoscentercrm.Fragment.AEPS;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vigyos.vigyoscentercrm.R;

public class AepsHistoryFragment extends Fragment {

    private Activity activity;
    private RelativeLayout finoLyt, paytmLyt;
    private TextView finoText, paytmText;
    private View finoLine, paytmLine;
    private Typeface typefaceBold;
    private Typeface typefaceRegular;

    public AepsHistoryFragment(Activity activity) {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_aeps_history2, container, false);
        Initialization(view);
        Declaration();
        return view;
    }

    private void Initialization(View view) {
        finoLyt = view.findViewById(R.id.finoLyt);
        finoText = view.findViewById(R.id.finoText);
        finoLine = view.findViewById(R.id.finoLine);
        paytmLyt = view.findViewById(R.id.paytmLyt);
        paytmText = view.findViewById(R.id.paytmText);
        paytmLine = view.findViewById(R.id.paytmLine);
    }

    private void Declaration() {
        typefaceBold = ResourcesCompat.getFont(activity, R.font.poppins_semi_bold);
        typefaceRegular = ResourcesCompat.getFont(activity, R.font.poppins_regular);
        finoLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finoLyt.setBackgroundResource(R.color.dark_vigyos);
                finoText.setTextColor(activity.getColor(R.color.white));
                finoText.setTypeface(typefaceBold);
                finoLine.setBackgroundColor(activity.getColor(R.color.dark_vigyos));


                paytmLyt.setBackgroundResource(R.color.white);




//                walletLine.setBackgroundColor(activity.getColor(R.color.not_click));
//
//                aepsText.setTextColor(activity.getColor(R.color.not_click));
//                aepsText.setTypeface(typefaceRegular);
//                aepsLine.setBackgroundColor(activity.getColor(R.color.not_click));
//
//                payoutText.setTextColor(activity.getColor(R.color.dark_vigyos));
//                payoutText.setTypeface(typefaceBold);
//                payoutLine.setBackgroundColor(activity.getColor(R.color.dark_vigyos));

            }
        });


    }
}