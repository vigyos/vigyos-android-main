package com.vigyos.vigyoscentercrm.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vigyos.vigyoscentercrm.R;
import com.vigyos.vigyoscentercrm.Utils.UserItemListener;

public class AdapterForUser extends RecyclerView.Adapter<AdapterForUser.holder> {

    private final UserItemListener listener;
    private String arr[];
    private int icon_img[];

    public AdapterForUser(String[] arr, UserItemListener listener, int[] theBitmapIds) {
        this.arr = arr;
        this.listener = listener;
        this.icon_img = theBitmapIds;
    }

    @NonNull
    @Override
    public AdapterForUser.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.profile_item_list, parent, false);
        return new holder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForUser.holder holder, int position) {
        holder.txt.setText(arr[position]);
        holder.img.setImageResource(icon_img[position]);
    }

    @Override
    public int getItemCount() {
        return arr.length;
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView txt;
        ImageView img;

        public holder(@NonNull View itemView, UserItemListener listener) {
            super(itemView);

            txt = itemView.findViewById(R.id.item_name);
            img = itemView.findViewById(R.id.item_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            Log.i("12121"," position - " + pos);
                            listener.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}