package com.vigyos.vigyoscentercrm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vigyos.vigyoscentercrm.Activity.ShowServicesActivity;
import com.vigyos.vigyoscentercrm.Model.DocumentItemModel;
import com.vigyos.vigyoscentercrm.R;

import java.util.ArrayList;

public class DocumentItemAdapter extends RecyclerView.Adapter<DocumentItemAdapter.holder> {

    private ArrayList<DocumentItemModel> documentItemModels;

    public DocumentItemAdapter(ArrayList<DocumentItemModel> documentItemModels, ShowServicesActivity showServicesActivity) {
        this.documentItemModels = documentItemModels;
    }

    @NonNull
    @Override
    public DocumentItemAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.documents_item_layout, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentItemAdapter.holder holder, int position) {
        DocumentItemModel itemModel = documentItemModels.get(position);
        holder.txt.setText(itemModel.getDocument_name());
    }

    @Override
    public int getItemCount() {
        return documentItemModels.size();
    }

    public static class holder extends RecyclerView.ViewHolder {

        TextView txt;

        public holder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.service1);
        }
    }
}