package com.acadview.www.aq;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Playing extends AppCompatActivity {

    final static long INTERVAL = 1000;
    final static long TIMEOUT = 7000;
    int progressValue =0;

    CountDownTimer mCountDown;
    int index=0,score =0,thisQuestion=0,totalQuestion,correctAnswer;

    FirebaseDatabase database;
    DatabaseReference questions;

    ProgressBar progressBar;
    ImageView question_image;
    Button btnA,btnB,btnC,btnd;
    TextView txtScore,txtQuestionNum,question_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");

        txtScore = (TextView)findViewById(R.id.txtScore);
    }
}
