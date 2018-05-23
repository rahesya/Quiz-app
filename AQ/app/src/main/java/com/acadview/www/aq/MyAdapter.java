package com.acadview.www.aq;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageview;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.Myview);
            textView =(TextView)itemView.findViewById(R.id.textView);
            imageview =(ImageView)itemView.findViewById(R.id.imageView2);
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
//
//        holder.imageview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,"you clicked image",Toast.LENGTH_LONG).show();
//            }
//        });
//
//        holder.textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,"you clicked text",Toast.LENGTH_LONG).show();
//            }
//        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,QuizStart.class);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {

        return itemData.length;
    }


}

