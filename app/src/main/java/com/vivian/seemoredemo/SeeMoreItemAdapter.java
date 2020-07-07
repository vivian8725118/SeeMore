package com.vivian.seemoredemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vivian.seemore.SeeMoreAdapter;

public class SeeMoreItemAdapter extends SeeMoreAdapter<Item> {
    public SeeMoreItemAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder generateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void bindviewholder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh=(ViewHolder)holder;
        vh.title.setText(mList.get(position).title);
    }

    @Override
    public void load() {

    }

    @Override
    public int itemViewType(int position) {
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
        }
    }
}
