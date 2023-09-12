package com.example.vigyoscentercrm.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vigyoscentercrm.Adapter.AdapterForHistory;
import com.example.vigyoscentercrm.R;

public class WalletFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        String service[] ={"Add Money in Wallet", "Adhar card update", "Pancard service", "Add Money in Wallet", "Gem Registration", "Advertisement","Add Money in Wallet", "Add Money in Wallet", "Pancard service", "Add Money in Wallet", "Gem Registration", "Advertisement"};
        String money[] = {"+200 Rs","-50 Rs","-100 Rs","+520 Rs","-200 Rs","-100 Rs","+200 Rs","+50 Rs","-100 Rs","+520 Rs","-200 Rs","-100 Rs"};

        RecyclerView newrecyclerView = (RecyclerView) view.findViewById(R.id.wallet_recycler);
        newrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newrecyclerView.setAdapter(new AdapterForHistory(service, money));
        return view;
    }
}