package com.acadview.www.aq;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ItemData[] itemData;

    public MyAdapter(ItemData[] itemData) {
        this.itemData = itemData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,null);
        ViewHolder vh =new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(itemData[position].getName());
        holder.imageview.setImageResource(itemData[position].getUrl());
    }

    @Override
    public int getItemCount() {
        return itemData.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageview;

        public ViewHolder(View itemView) {
            super(itemView);

            textView =(TextView)itemView.findViewById(R.id.textView);
            imageview =(ImageView)itemView.findViewById(R.id.imageView2);
        }
    }
}

