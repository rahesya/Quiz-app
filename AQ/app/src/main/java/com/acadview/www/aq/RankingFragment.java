package com.acadview.www.aq;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RankingFragment extends Fragment {

    View myFragment;

    RecyclerView rankingList;
    LinearLayoutManager layoutManager;

    RankingAdapter adapter;
    List<Ranking> listdata;

    FirebaseDatabase database;
    DatabaseReference questionScore,rankingTbl,user;

    public static int sum=0;

    public static RankingFragment newInstance(){
        RankingFragment rankingFragment =new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        questionScore =database.getReference("Question_Score/"+Common.currentuser.getUsername());
        rankingTbl =database.getReference("Ranking");
        user=database.getReference("Users");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment =inflater.inflate(R.layout.fragment_ranking,container,false);

        rankingList =(RecyclerView)myFragment.findViewById(R.id.rankingList);
        layoutManager =new LinearLayoutManager(getActivity());

        rankingList.setItemAnimator(new DefaultItemAnimator());

        listdata = new ArrayList<>();

        adapter = new RankingAdapter(listdata,getContext());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);

        updateScore(Common.currentuser.getUsername(), new RankingCallback<Rank>() {
            @Override
            public void callBack(final Rank rank) {

                user.child(rank.getUsername()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        Ranking ranking = new Ranking(rank.getUsername(),rank.getScore(),user.getPathtoprofileimage());
                        rankingTbl.child(ranking.getUserName()).setValue(ranking);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                showRanking();
            }
        });

        return myFragment ;
    }
    
    private void showRanking() {
        rankingTbl.orderByChild("score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Ranking local = data.getValue(Ranking.class);
                    listdata.add(local);
                    adapter.notifyDataSetChanged();
                    rankingList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateScore(final String username,final RankingCallback<Rank> callback) {

        questionScore.orderByChild("user").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        sum =0;

                        for(DataSnapshot data:dataSnapshot.getChildren()){
                            QuestionScore ques =data.getValue(QuestionScore.class);
                            sum+=Integer.parseInt(ques.getScore());

                        }
                        user.child(Common.currentuser.getUsername()).child("totalScore").setValue(String.valueOf(sum));
                        Rank rank =new Rank(username,sum);
                        callback.callBack(rank);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}