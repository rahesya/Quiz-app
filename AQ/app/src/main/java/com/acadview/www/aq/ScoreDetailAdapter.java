package com.acadview.www.aq;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ScoreDetailAdapter extends RecyclerView.Adapter<ScoreDetailAdapter.ScoreDetailViewHolder> {

    List<Scores> Scorelist;

    public ScoreDetailAdapter(List<Scores> List){
        this.Scorelist = List;
    }

    @NonNull
    @Override
    public ScoreDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.scoredetail_layout,parent,false);
        ScoreDetailViewHolder svh = new ScoreDetailViewHolder(view);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreDetailViewHolder holder, int position) {

        Scores data = Scorelist.get(position);
        holder.txt_score.setText(data.getScore());
        holder.txt_categoryname.setText(data.getCategoryName());

    }

    @Override
    public int getItemCount() {
        return Scorelist.size();
    }

    public class ScoreDetailViewHolder extends RecyclerView.ViewHolder{

        TextView txt_categoryname,txt_score;

        public ScoreDetailViewHolder(View itemView) {
            super(itemView);

            txt_categoryname = (TextView)itemView.findViewById(R.id.txt_category);
            txt_score = (TextView)itemView.findViewById(R.id.txt_Scoredetail);

        }
    }
}
