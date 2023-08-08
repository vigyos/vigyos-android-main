package com.yoursuperidea.vigyos.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.yoursuperidea.vigyos.R;


public class TermsAndConditionsFragment extends Fragment {

    private WebView termsWebView;
    private ProgressBar progressBar;

    public TermsAndConditionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_terms_and_conditions, container, false);
        progressBar = rootView.findViewById(R.id.progress_bar);
        termsWebView = rootView.findViewById(R.id.termsWebView);
        termsWebView.getSettings().setJavaScriptEnabled(true);
        termsWebView.loadUrl("file:///android_asset/termsandconditions.html");
        termsWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                progressBar.show();
                view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                progressBar.setVisibility(View.GONE);
            }
        });
        return rootView;
    }
}