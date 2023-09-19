package com.vigyos.vigyoscentercrm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vigyos.vigyoscentercrm.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BannerListAdapter extends RecyclerView.Adapter<BannerListAdapter.Holder>{

    private ArrayList<String> bannerListModels;

    public BannerListAdapter(ArrayList<String> bannerListModels) {
        this.bannerListModels = bannerListModels;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.banner_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
//        ArrayList<String> bannerListModel = bannerListModels.get(position);
        Picasso.get().load(bannerListModels.get(position)).into(holder.bannerImageView);
    }

    @Override
    public int getItemCount() {
        return bannerListModels.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{

        private ImageView bannerImageView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            bannerImageView = itemView.findViewById(R.id.bannerImageView);
        }
    }
}
