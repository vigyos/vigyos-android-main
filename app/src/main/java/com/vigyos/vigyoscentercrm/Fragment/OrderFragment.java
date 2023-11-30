package com.vigyos.vigyoscentercrm.Fragment;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vigyos.vigyoscentercrm.R;

@BuildCompat.PrereleaseSdkCheck
public class OrderFragment extends Fragment {

    private Activity activity;
    private RelativeLayout pendingLyt, processingLyt;
    private RelativeLayout completedLyt, rejectedLyt;
    private TextView pendingText, processingText;
    private TextView completedText, rejectedText;
    private View pendingLine, processingLine;
    private View completedLine, rejectedLine;
    private Typeface typefaceBold;
    private Typeface typefaceRegular;

    public OrderFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        Initialization(view);
        Declaration();
        return view;
    }

    private void Initialization(View view) {
        pendingLyt = view.findViewById(R.id.pendingLyt);
        processingLyt = view.findViewById(R.id.processingLyt);
        completedLyt = view.findViewById(R.id.completedLyt);
        rejectedLyt = view.findViewById(R.id.rejectedLyt);
        pendingText = view.findViewById(R.id.pendingText);
        processingText = view.findViewById(R.id.processingText);
        completedText = view.findViewById(R.id.completedText);
        rejectedText = view.findViewById(R.id.rejectedText);
        pendingLine = view.findViewById(R.id.pendingLine);
        processingLine = view.findViewById(R.id.processingLine);
        completedLine = view.findViewById(R.id.completedLine);
        rejectedLine = view.findViewById(R.id.rejectedLine);
    }

    private void Declaration() {
        loadFragment(new PendingFragment());
        tabLayout();
    }

    private void tabLayout () {
        typefaceBold = ResourcesCompat.getFont(activity, R.font.poppins_semi_bold);
        typefaceRegular = ResourcesCompat.getFont(activity, R.font.poppins_regular);
        pendingLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pendingText.setTextColor(activity.getColor(R.color.dark_vigyos));
                pendingText.setTypeface(typefaceBold);
                pendingLine.setBackgroundColor(activity.getColor(R.color.dark_vigyos));
                processingText.setTextColor(activity.getColor(R.color.not_click));
                processingText.setTypeface(typefaceRegular);
                processingLine.setBackgroundColor(activity.getColor(R.color.not_click));
                completedText.setTextColor(activity.getColor(R.color.not_click));
                completedText.setTypeface(typefaceRegular);
                completedLine.setBackgroundColor(activity.getColor(R.color.not_click));
                rejectedText.setTextColor(activity.getColor(R.color.not_click));
                rejectedText.setTypeface(typefaceRegular);
                rejectedLine.setBackgroundColor(activity.getColor(R.color.not_click));

                loadFragment(new PendingFragment());
            }
        });
        processingLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pendingText.setTextColor(activity.getColor(R.color.not_click));
                pendingText.setTypeface(typefaceRegular);
                pendingLine.setBackgroundColor(activity.getColor(R.color.not_click));
                processingText.setTextColor(activity.getColor(R.color.dark_vigyos));
                processingText.setTypeface(typefaceBold);
                processingLine.setBackgroundColor(activity.getColor(R.color.dark_vigyos));
                completedText.setTextColor(activity.getColor(R.color.not_click));
                completedText.setTypeface(typefaceRegular);
                completedLine.setBackgroundColor(activity.getColor(R.color.not_click));
                rejectedText.setTextColor(activity.getColor(R.color.not_click));
                rejectedText.setTypeface(typefaceRegular);
                rejectedLine.setBackgroundColor(activity.getColor(R.color.not_click));

                loadFragment(new ProcessingFragment());
            }
        });
        completedLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pendingText.setTextColor(activity.getColor(R.color.not_click));
                pendingText.setTypeface(typefaceRegular);
                pendingLine.setBackgroundColor(activity.getColor(R.color.not_click));
                processingText.setTextColor(activity.getColor(R.color.not_click));
                processingText.setTypeface(typefaceRegular);
                processingLine.setBackgroundColor(activity.getColor(R.color.not_click));
                completedText.setTextColor(activity.getColor(R.color.dark_vigyos));
                completedText.setTypeface(typefaceBold);
                completedLine.setBackgroundColor(activity.getColor(R.color.dark_vigyos));
                rejectedText.setTextColor(activity.getColor(R.color.not_click));
                rejectedText.setTypeface(typefaceRegular);
                rejectedLine.setBackgroundColor(activity.getColor(R.color.not_click));

                loadFragment(new CompletedFragment(activity));
            }
        });
        rejectedLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pendingText.setTextColor(activity.getColor(R.color.not_click));
                pendingText.setTypeface(typefaceRegular);
                pendingLine.setBackgroundColor(activity.getColor(R.color.not_click));
                processingText.setTextColor(activity.getColor(R.color.not_click));
                processingText.setTypeface(typefaceRegular);
                processingLine.setBackgroundColor(activity.getColor(R.color.not_click));
                completedText.setTextColor(activity.getColor(R.color.not_click));
                completedText.setTypeface(typefaceRegular);
                completedLine.setBackgroundColor(activity.getColor(R.color.not_click));
                rejectedText.setTextColor(activity.getColor(R.color.dark_vigyos));
                rejectedText.setTypeface(typefaceBold);
                rejectedLine.setBackgroundColor(activity.getColor(R.color.dark_vigyos));

                loadFragment(new CancelledFragment(activity));
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        if (!fragmentManager.isStateSaved()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frameContainerOrder, fragment);
            transaction.commit();
        }
    }
}