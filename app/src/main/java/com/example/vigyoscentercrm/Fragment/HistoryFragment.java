package com.example.vigyoscentercrm.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.vigyoscentercrm.Adapter.AdapterForHistory;
import com.example.vigyoscentercrm.R;

public class HistoryFragment extends Fragment {

    private String item [] = {"All","GST Registration","Adhar Service", "Pancard Service", "Domain Name", "ITR services"};
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapter;
    private RecyclerView historyrecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.auto_complete_txt);
        adapter = new ArrayAdapter<>(getContext(), R.layout.category_item_list, item);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getContext(),"item:"+item,Toast.LENGTH_SHORT).show();
            }
        });

        String service[] ={"Add Money in Wallet", "Adhar card update", "Pancard service", "Add Money in Wallet", "Gem Registration", "Advertisement","Add Money in Wallet", "Add Money in Wallet", "Pancard service", "Add Money in Wallet", "Gem Registration", "Advertisement"};
        String money[] = {"+200 Rs","-50 Rs","-100 Rs","+520 Rs","-200 Rs","-100 Rs","+200 Rs","+50 Rs","-100 Rs","+520 Rs","-200 Rs","-100 Rs"};

        historyrecyclerView = (RecyclerView) view.findViewById(R.id.history_recycler);
        historyrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        historyrecyclerView.setAdapter(new AdapterForHistory(service, money));
        return view;
    }
}