package com.yoursuperidea.vigyoscentersrm.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoursuperidea.vigyoscentersrm.Adapter.AdapterWishlist;
import com.yoursuperidea.vigyoscentersrm.R;

import java.util.ArrayList;

public class WishlistFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<String > arrayName =  new ArrayList<>();
    private ArrayList<String > arrayPrice = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_wishlist, container, false);
        recyclerView = view.findViewById(R.id.wishlist_recycler);

        //Name
        arrayName.add("kamal");
        arrayName.add("shaym");
        arrayName.add("Mohan");
        arrayName.add("Priyam");

        //Price
        arrayPrice.add("120");
        arrayPrice.add("130");
        arrayPrice.add("140");
        arrayPrice.add("150");

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new AdapterWishlist(arrayName,arrayPrice));
        return view;
    }



}