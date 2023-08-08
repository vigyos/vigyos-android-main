package com.yoursuperidea.vigyos.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoursuperidea.vigyos.Adapter.AdapterForRecyclerview;
import com.yoursuperidea.vigyos.R;


public class SearchFragment extends Fragment {


    private SearchView searchView;
    private RecyclerView recyclerView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search, container, false);


        String arr[] = {"GST Registration", "GST Return", "GST Return Nil", "GST Return Regular", "Income tax", "FSSAI Registration", "LOGO design","GST Registration", "GST Return", "GST Return Nil", "GST Return Regular", "Income tax"};

        searchView = (SearchView) view.findViewById(R.id.searchview);
        recyclerView = (RecyclerView) view.findViewById(R.id.order_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new AdapterForRecyclerview(arr));

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return view;
    }
}