package com.vigyos.vigyoscentercrm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vigyos.vigyoscentercrm.R;

public class AdapterForHistory extends RecyclerView.Adapter<AdapterForHistory.holder> {

    public String[] sn;
    public String[] rs;

    public AdapterForHistory(String[] sn, String[] rs) {
        this.sn = sn;
        this.rs = rs;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_history_item, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.service_name.setText(sn[position]);
        holder.amount.setText(rs[position]);
    }

    @Override
    public int getItemCount() {
        return rs.length;
    }

    public static class holder extends RecyclerView.ViewHolder {

        TextView service_name;
        TextView amount;

        public holder(@NonNull View itemView) {
            super(itemView);
            service_name = itemView.findViewById(R.id.service_name);
            amount = itemView.findViewById(R.id.amount);
        }
    }
}
