package com.acadview.www.aq;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private ItemData[] itemData;

    private Context context;

    private AdapterView.OnItemClickListener itemClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textView;
        public ImageView imageview;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.Myview);
            textView =(TextView)itemView.findViewById(R.id.textView);
            imageview =(ImageView)itemView.findViewById(R.id.imageView2);

            textView.setOnClickListener(this);
            imageview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == textView.getId()) {
                Intent intent =new Intent(v.getContext(),QuizStart.class);
                Common.categoryId="0"+String.valueOf(getAdapterPosition()+1);
                Toast.makeText(v.getContext(),Common.categoryId,Toast.LENGTH_LONG).show();
                v.getContext().startActivity(intent);

            } else if(v.getId() == imageview.getId()){
                Intent intent =new Intent(v.getContext(),QuizStart.class);
                Common.categoryId="0"+String.valueOf(getAdapterPosition()+1);
                Toast.makeText(v.getContext(),Common.categoryId,Toast.LENGTH_LONG).show();
                v.getContext().startActivity(intent);
            }
        }
    }

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


}

