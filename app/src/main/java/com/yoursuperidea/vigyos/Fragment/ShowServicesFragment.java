package com.yoursuperidea.vigyos.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yoursuperidea.vigyos.R;


public class ShowServicesFragment extends Fragment {

    private View rootView;
    private TextView serviceName;
    private String serviceDetails;

    public ShowServicesFragment(String service) {
        // Required empty public constructor
        this.serviceDetails = service;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_show_services, container, false);
        serviceName = rootView.findViewById(R.id.serviceDetails);
        serviceName.setText(serviceDetails);
        return rootView;
    }
}