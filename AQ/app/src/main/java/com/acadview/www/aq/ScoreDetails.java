package com.acadview.www.aq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ScoreDetails extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference question_score;

    String viewUser = "";

    List<Scores> AllScores;

    RecyclerView scoreList;
    RecyclerView.LayoutManager layoutManager;
    ScoreDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_details);

        database =FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");
        scoreList = (RecyclerView)findViewById(R.id.scoreList);
        layoutManager = new LinearLayoutManager(this);
        scoreList.setHasFixedSize(true);
        scoreList.setLayoutManager(layoutManager);

        AllScores = new ArrayList<>();

        adapter = new ScoreDetailAdapter(AllScores);

        if(getIntent()!=null){
            viewUser =getIntent().getStringExtra("viewUser");
        }
        if(!viewUser.isEmpty()){
            loadScoreDetail(viewUser);
        }
    }

    private void loadScoreDetail(String viewUser) {

        question_score.orderByChild("user").equalTo(viewUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Scores local = data.getValue(Scores.class);
                    AllScores.add(local);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(),"size of list"+String.valueOf(AllScores.size()),Toast.LENGTH_LONG).show();
        scoreList.setAdapter(adapter);
    }
}
