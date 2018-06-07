package com.acadview.www.aq;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;


public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingViewHolder> {

    List<Ranking> Ranklist;

    public RankingAdapter(List<Ranking> List){
        this.Ranklist = List;
    }

    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_layout,parent,false);
        RankingViewHolder vh =new RankingViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder holder, int position) {
        Ranking data = Ranklist.get(position);
        holder.txt_name.setText(data.getUserName());
        holder.txt_Score.setText(String.valueOf(data.getScore()));

    }

    @Override
    public int getItemCount() {

        return Ranklist.size();
    }

    public static class RankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txt_name, txt_Score;

        public RankingViewHolder(View itemView) {
            super(itemView);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_Score = (TextView) itemView.findViewById(R.id.txt_Score);

            txt_name.setOnClickListener(this);
            txt_Score.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==txt_name.getId()){
                 Intent scoredetailintent = new Intent(v.getContext(),ScoreDetails.class);
                 scoredetailintent.putExtra("viewUser",txt_name.getText().toString());
                 v.getContext().startActivity(scoredetailintent);

                }

            }

        }

    }
