package com.vigyos.vigyoscentercrm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.os.BuildCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vigyos.vigyoscentercrm.Activity.BBPS.BillPay.BBPSOperatorListActivity;
import com.vigyos.vigyoscentercrm.R;

import java.util.List;

@BuildCompat.PrereleaseSdkCheck
public class MunicipalityAdapter extends RecyclerView.Adapter<MunicipalityAdapter.MunicipalityViewHolder> {

    private List<MunicipalityItem> municipalityList;
    private Context context;

    public MunicipalityAdapter(List<MunicipalityItem> municipalityList, Context context) {
        this.municipalityList = municipalityList;
        this.context = context;
    }

    @NonNull
    @Override
    public MunicipalityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_seemaore_item, parent, false);
        return new MunicipalityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MunicipalityViewHolder holder, int position) {
        MunicipalityItem currentItem = municipalityList.get(position);
        holder.municipalityName.setText(currentItem.getCategoryName());
        // Check if categoryIconUrl is not empty or null before loading with Picasso
        if (!TextUtils.isEmpty(currentItem.getCategoryIconUrl())) {
            Picasso.get()
                    .load(currentItem.getCategoryIconUrl())
                    .into(holder.municipalityIcon, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                        @Override
                        public void onError(Exception e) { }
                    });
        } else {
            // Handle the case where categoryIconUrl is empty or null
            // You can set a placeholder image or handle it based on your requirements
            holder.municipalityIcon.setImageResource(R.mipmap.ic_launcher);
        }

        holder.mainLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,  BBPSOperatorListActivity.class);
                intent.putExtra("categoryData", currentItem.getCategoryName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return municipalityList.size();
    }

    public static class MunicipalityViewHolder extends RecyclerView.ViewHolder {

        public ImageView municipalityIcon;
        public TextView municipalityName;
        public RelativeLayout mainLyt;
        public ProgressBar progressBar;

        public MunicipalityViewHolder(@NonNull View itemView) {
            super(itemView);
            municipalityIcon = itemView.findViewById(R.id.iconImage);
            municipalityName = itemView.findViewById(R.id.titleName);
            mainLyt = itemView.findViewById(R.id.totalCardView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}