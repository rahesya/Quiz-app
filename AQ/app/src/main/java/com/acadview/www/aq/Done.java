package com.acadview.www.aq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Done extends AppCompatActivity {

    Button btnTryAgain;
    TextView txtResultScore,gettxtResultQuestion;
    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference question_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        database =FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score ");

        txtResultScore = (TextView)findViewById(R.id.txtTotalQuestion);
        progressBar = (ProgressBar)findViewById(R.id.doneProgressBar);
        gettxtResultQuestion = (TextView)findViewById(R.id.txtTotalQuestion);
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

            txtResultScore.setText(String .format("Score : %d",score));
            gettxtResultQuestion.setText(String.format("Passed : %d/%d",correctAnswer,totalQuestion));

            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);

            question_score.child(String.format("%s_%s",Common.currentuser.getUsername(),Common.categoryId))
                    .setValue(new QuestionScore(String.format("%s_%s",Common.currentuser.getUsername(),Common.categoryId),Common.currentuser.getUsername(),String.valueOf(score),Common.categoryId,Common.categoryName));
        }
    }
}
