package com.yoursuperidea.vigyos.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.yoursuperidea.vigyos.R;

public class RefundPolicyFragment extends Fragment {

    private WebView refundWebView;

    public RefundPolicyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_refund_policy, container, false);

        refundWebView = rootView.findViewById(R.id.refundWebView);
        refundWebView.getSettings().setJavaScriptEnabled(true);
        refundWebView.loadUrl("https://vigyos.com/");

        return rootView;
    }
}