package com.acadview.www.aq;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jinatonic.confetti.CommonConfetti;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class Done extends AppCompatActivity {

    Button btnTryAgain;
    TextView txtResultScore,name,txttotalques,txtcorrectans;

    CircleImageView circleImageView;
    MediaPlayer submitmedia;
    RelativeLayout container;
    FirebaseDatabase database;
    DatabaseReference question_score,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        submitmedia = MediaPlayer.create(this,R.raw.done);

        if (Login.getsharedpreference(Common.currentuser.getUsername(),Login.userdetails)=="true"){
            submitmedia.start();
        }

        container= (RelativeLayout)findViewById(R.id.container);
        circleImageView = (CircleImageView)findViewById(R.id.userpic);

        ViewTreeObserver vto = container.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                CommonConfetti.rainingConfetti(container, new int[] { Color.YELLOW ,Color.RED,Color.MAGENTA})
                        .infinite();
            }
        });

        database =FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score/"+Common.currentuser.getUsername());
        user = database.getReference("Users");

        txtResultScore = (TextView)findViewById(R.id.player_score);
        txttotalques = (TextView)findViewById(R.id.player_total_ques);
        txtcorrectans = (TextView)findViewById(R.id.player_correct_ans);
        name = (TextView)findViewById(R.id.player_name);

        btnTryAgain =(Button)findViewById(R.id.btnTryAgain);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitmedia.stop();
                Intent intent = new Intent(Done.this,Catalog.class);
                startActivity(intent);
                finish();
            }
        });

        Bundle extra=getIntent().getExtras();
        if(extra != null){
            final int nowscore = extra.getInt("Score");
            int totalQuestion = extra.getInt("Total");
            int correctAnswer = extra.getInt("Correct");

            name.setText(Common.currentuser.getUsername());

            question_score.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    QuestionScore questionScore = dataSnapshot.child(Common.categoryName).getValue(QuestionScore.class);
                    QuestionScore newupdated = new QuestionScore(Common.categoryId,Common.currentuser.getUsername(),String.valueOf(nowscore),Common.categoryName);
                    question_score.child(Common.categoryName).setValue(newupdated);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

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

            txtResultScore.setText(String.valueOf(nowscore));
            txtcorrectans.setText(String.valueOf(correctAnswer));
            txttotalques.setText(String.valueOf(totalQuestion));

        }
    }

    @Override
    public void onBackPressed() {
        submitmedia.stop();
        Intent intent = new Intent(Done.this,Catalog.class);
        startActivity(intent);
        finish();
    }
}
