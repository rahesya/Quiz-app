package com.acadview.www.aq;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jinatonic.confetti.CommonConfetti;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Done extends AppCompatActivity {

    Button btnTryAgain;
    TextView txtResultScore,name;

    CircleImageView circleImageView;
    RelativeLayout container;
    FirebaseDatabase database;
    DatabaseReference question_score,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        container= (RelativeLayout)findViewById(R.id.container);
        circleImageView = (CircleImageView)findViewById(R.id.userpic);

        ViewTreeObserver vto = container.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                CommonConfetti.rainingConfetti(container, new int[] { Color.YELLOW ,Color.RED})
                        .infinite();
            }
        });

        database =FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score/"+Common.currentuser.getUsername());
        user = database.getReference("Users");

        txtResultScore = (TextView)findViewById(R.id.player_score);
        name = (TextView)findViewById(R.id.player_name);

        btnTryAgain =(Button)findViewById(R.id.btnTryAgain);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Done.this,Catalog.class);
                startActivity(intent);
                finish();
            }
        });

        Bundle extra=getIntent().getExtras();
        if(extra != null){
            int score = extra.getInt("Score");
            int totalQuestion = extra.getInt("Total");
            int correctAnswer = extra.getInt("Correct");

            name.setText(Common.currentuser.getUsername());

            user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Picasso.with(getBaseContext()).load(dataSnapshot.child(Common.currentuser.getUsername()).child("pathtoprofileimage").getValue().toString())
                            .into(circleImageView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            txtResultScore.setText(String.valueOf(score));

            QuestionScore questionScore = new QuestionScore(Common.categoryId,Common.currentuser.getUsername(),String.valueOf(score),Common.categoryName);

            question_score.child(Common.categoryName).setValue(questionScore);
        }
    }
}
