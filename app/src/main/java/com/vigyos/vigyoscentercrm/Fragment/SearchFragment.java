package com.vigyos.vigyoscentercrm.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vigyos.vigyoscentercrm.Activity.SearchServicesActivity;
import com.vigyos.vigyoscentercrm.Activity.ShowServicesActivity;
import com.vigyos.vigyoscentercrm.Model.SearchServicesModel;
import com.vigyos.vigyoscentercrm.R;

import java.util.ArrayList;

@BuildCompat.PrereleaseSdkCheck
public class SearchFragment extends Fragment {

    private AutoCompleteTextView autoCompleteTextView;
    private RecyclerView recyclerView;
    private ServiceItemListAdapter serviceItemListAdapter;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<SearchServicesModel> searchServicesModels;
    private Dialog dialog;
    private Activity activity;

    public SearchFragment(Activity activity, ArrayList<SearchServicesModel>  searchServicesModels) {
        // Required empty public constructor
        this.activity = activity;
        this.searchServicesModels = searchServicesModels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        initialization(view);
        serviceList();
        declaration(view);

        return view;
    }

    private void initialization(View view){
        autoCompleteTextView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.order_list);
    }

    private void declaration(View view){
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                serviceItemListAdapter.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void serviceList(){
        serviceItemListAdapter = new ServiceItemListAdapter(searchServicesModels, activity);
        gridLayoutManager = new GridLayoutManager(activity, 1 , GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(serviceItemListAdapter);
    }

    public class ServiceItemListAdapter extends RecyclerView.Adapter<ServiceItemListAdapter.MyViewHolder> implements Filterable {

        private ArrayList<SearchServicesModel> searchListArray;
        private ArrayList<SearchServicesModel> searchFilterList;
        private Activity activity;

        @Override
        public Filter getFilter() {
            return filterData;
        }

        private Filter filterData =new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<SearchServicesModel> filteredList = new ArrayList<>();
                if (charSequence == null || charSequence.length() ==0 ){
                    filteredList.addAll(searchFilterList);
                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();
                    for (SearchServicesModel item : searchFilterList){
                        if (item.getService_name().toLowerCase().contains(filterPattern)){
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                searchListArray.clear();
                searchListArray.addAll((ArrayList) filterResults.values);
                notifyDataSetChanged();
            }
        };

        public ServiceItemListAdapter(ArrayList<SearchServicesModel> searchServicesModels, Activity activity) {
            super();
            this.searchListArray = searchServicesModels;
            this.activity = activity;
            this.searchFilterList = new ArrayList<>(searchServicesModels);
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_search_services_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            SearchServicesModel model = searchListArray.get(position);
            holder.listName.setText(model.getService_name());
            holder.next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ShowServicesActivity.class);
                    intent.putExtra("details", model.getDescription());
                    intent.putExtra("id", model.getService_id());
                    intent.putExtra("service", model.getService());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return searchListArray.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            private TextView listName;
            private RelativeLayout next;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                listName = itemView.findViewById(R.id.listName);
                next = itemView.findViewById(R.id.next_ll);
            }
        }
    }
}