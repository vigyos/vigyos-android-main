package com.vigyos.vigyoscentercrm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.os.BuildCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vigyos.vigyoscentercrm.Activity.CategoryDetailsActivity;
import com.vigyos.vigyoscentercrm.R;

import org.json.JSONObject;

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
            Picasso.get().load(currentItem.getCategoryIconUrl()).into(holder.municipalityIcon);
        } else {
            // Handle the case where categoryIconUrl is empty or null
            // You can set a placeholder image or handle it based on your requirements
            holder.municipalityIcon.setImageResource(R.mipmap.ic_launcher);
        }

        holder.mainLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,  CategoryDetailsActivity.class);
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
        private RelativeLayout mainLyt;

        public MunicipalityViewHolder(@NonNull View itemView) {
            super(itemView);
            municipalityIcon = itemView.findViewById(R.id.iconImage);
            municipalityName = itemView.findViewById(R.id.titleName);
            mainLyt = itemView.findViewById(R.id.totalCardView);
        }
    }
}