package com.yoursuperidea.vigyoscentersrm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yoursuperidea.vigyoscentersrm.R;

import java.util.ArrayList;

public class AdapterWishlist extends RecyclerView.Adapter<AdapterWishlist.holder>{

    private ArrayList<String> arrayList;
    private ArrayList<String> arrayPrice;

    public AdapterWishlist(ArrayList<String> arrayList, ArrayList<String> arrayPrice) {
        this.arrayList = arrayList;
        this.arrayPrice = arrayPrice;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.wishlist_item, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        holder.price.setText(arrayPrice.get(position));
        holder.serviceName.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class holder extends RecyclerView.ViewHolder {

        private TextView serviceName;
        private TextView price;

        public holder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.service_name);
            price = itemView.findViewById(R.id.price);

        }
    }
}
