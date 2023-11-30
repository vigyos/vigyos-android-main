package com.vigyos.vigyoscentercrm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vigyos.vigyoscentercrm.R;

public class CustomArrayAdapter extends ArrayAdapter<CharSequence> {

    public CustomArrayAdapter(Context context, int resource, CharSequence[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        if (textView != null) {
            textView.setText(getItem(position));
        }

        return convertView;
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.spinner_item_text);
        ImageView imageView = convertView.findViewById(R.id.spinner_item_icon);

        if (textView != null && imageView != null) {
            textView.setText(getItem(position));
            imageView.setImageResource(R.drawable.drop_down);
        }

        return convertView;
    }
}