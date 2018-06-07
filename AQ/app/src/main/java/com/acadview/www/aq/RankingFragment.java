package com.acadview.www.aq;

import android.content.Context;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    DatabaseReference questionScore,rankingTbl;

    public static int sum=0;

    public static RankingFragment newInstance(){
        RankingFragment rankingFragment =new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database =FirebaseDatabase.getInstance();
        questionScore =database.getReference("Question_Score");
        rankingTbl =database.getReference("Ranking");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment =inflater.inflate(R.layout.fragment_ranking,container,false);

        rankingList =(RecyclerView)myFragment.findViewById(R.id.rankingList);
        layoutManager =new LinearLayoutManager(getActivity());
        rankingList.setHasFixedSize(true);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);
        rankingList.setItemAnimator(new DefaultItemAnimator());

        listdata = new ArrayList<>();

        adapter = new RankingAdapter(listdata);

        updateScore(Common.currentuser.getUsername(), new RankingCallback<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                rankingTbl.child(ranking.getUserName()).setValue(ranking);
                showRanking();
            }
        });

        adapter.notifyDataSetChanged();
        return myFragment ;
    }

    void GetDataFirebase(){

        rankingTbl =database.getReference("Ranking");

        rankingTbl.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Ranking data = new Ranking();
                data = dataSnapshot.getValue(Ranking.class);
                listdata.add(data);
                rankingList.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showRanking() {
        rankingTbl.orderByChild("score").addValueEventListener(new ValueEventListener() {
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

    private void updateScore(final String username,final RankingCallback<Ranking> callback) {

        questionScore.orderByChild("user").equalTo(username)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        adapter.notifyDataSetChanged();
                        for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                            QuestionScore ques =postSnapshot.getValue(QuestionScore.class);
                            sum+=Integer.parseInt(ques.getScore());

                        }
                        Ranking ranking =new Ranking(username,sum);
                        callback.callBack(ranking);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}