package com.hisurga.simplelist;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<String> dataSet;
    private Context context;

    public RecyclerAdapter(ArrayList<String> data, Context context) {
        this.dataSet = data;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout relativeLayout;

        public ViewHolder(View v) {
            super(v);
            this.textView = (TextView) v.findViewById(R.id.itemText);
            this.relativeLayout = (RelativeLayout) v.findViewById(R.id.itemLayout);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SharedPreferences pref_item = PreferenceManager.getDefaultSharedPreferences(context);

        holder.textView.setText(dataSet.get(position));
        holder.textView.setTextColor(Color.parseColor(pref_item.getString("item_text_color_list", "#757575")));
        holder.textView.setTextSize(Float.parseFloat(pref_item.getString("item_text_size_list", "20")));
        holder.relativeLayout.setBackgroundColor(Color.parseColor(pref_item.getString("item_background_color_list", "#FFFFFF")));
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

}
