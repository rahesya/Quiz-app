package com.acadview.www.aq;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ScoreDetails extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference question_score,scoretbl;

    String viewUser = "";

    ScoreDetailAdapter adapter;

    List<Scores> AllScores;

    RecyclerView scoreList;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_details);

        viewUser =getIntent().getStringExtra("viewUser");

        database =FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score/"+viewUser);
        scoretbl = database.getReference("Scores/"+viewUser);

        AllScores = new ArrayList<>();

        scoreList = (RecyclerView)findViewById(R.id.scoreList);
        layoutManager = new LinearLayoutManager(this);
        scoreList.setLayoutManager(layoutManager);

        adapter = new ScoreDetailAdapter(AllScores);

        updating_individual_scores();
        showscores();

        adapter.notifyDataSetChanged();
        scoreList.setAdapter(adapter);
    }

    private void showscores() {

        scoretbl.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Scores local = data.getValue(Scores.class);
                    AllScores.add(local);
                    adapter.notifyDataSetChanged();
                    scoreList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void loadScoreDetail(final String viewUser) {
        question_score.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    QuestionScore local = data.getValue(QuestionScore.class);
                    String Category_name = local.getCategoryName();
                    int score = Integer.parseInt(local.getScore());
                    Scores scoreobj = new Scores(Category_name,score);
                    AllScores.add(scoreobj);
                    uploadingscores(scoreobj,viewUser);
                    adapter.notifyDataSetChanged();
                    scoreList.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        Query query = question_score.orderByChild("user").equalTo(viewUser);
//
//        final FirebaseRecyclerOptions<QuestionScore> option = new FirebaseRecyclerOptions.Builder<QuestionScore>()
//                .setQuery(query, QuestionScore.class)
//                .build();
//
//        adapter = new FirebaseRecyclerAdapter <QuestionScore,ScoreDetailViewHolder>(option) {
//            @Override
//            protected void onBindViewHolder(@NonNull ScoreDetailViewHolder holder, int position, @NonNull QuestionScore model) {
//                holder.txt_score.setText(model.getScore());
//                holder.txt_categoryname.setText(model.getCategoryName());
//            }
//
//            @NonNull
//            @Override
//            public ScoreDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.scoredetail_layout,parent,false);
//                ScoreDetailViewHolder svh = new ScoreDetailViewHolder(view);
//                return svh;
//            }
//        };
//
//        adapter.notifyDataSetChanged();
//        scoreList.setAdapter(adapter);
//        question_score.orderByChild("user").equalTo(viewUser).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot data:dataSnapshot.getChildren()){
//                    Scores local = data.getValue(Scores.class);
//                    AllScores.add(local);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        adapter.notifyDataSetChanged();
//        Toast.makeText(getApplicationContext(),"size of list"+String.valueOf(AllScores.size()), Toast.LENGTH_LONG).show();
//        scoreList.setAdapter(adapter);
    }

    private void uploadingscores(Scores scores,String user) {

        scoretbl.child(user).setValue(scores);

    }

    private void updating_individual_scores() {
        question_score.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    QuestionScore ques =data.getValue(QuestionScore.class);
                    Scores scoreobj =new Scores(ques.getCategoryName(),Integer.parseInt(ques.getScore()));
                    scoretbl.child(ques.getCategoryName()).setValue(scoreobj);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
