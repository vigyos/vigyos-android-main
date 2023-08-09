package com.yoursuperidea.vigyos.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yoursuperidea.vigyos.R;

public class AdapterForRecyclerview extends RecyclerView.Adapter<AdapterForRecyclerview.holder> {

    String[] data;

    public AdapterForRecyclerview(String[] data) {
        this.data = data;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recyclerview,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        holder.txt.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public static class holder extends RecyclerView.ViewHolder{

        TextView txt;

        public holder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.listName);
        }
    }
}