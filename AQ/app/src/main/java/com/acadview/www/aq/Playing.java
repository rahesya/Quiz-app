package com.acadview.www.aq;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Playing extends AppCompatActivity implements View.OnClickListener{

    String ClickedButton_txt;

    int index=0,score =0,totalQuestion,correctAnswer,difficultylevel,lives;
    boolean questionattempted[];
    boolean allQuestionAttempted=false;
    MediaPlayer submitmedia;

    int questionshow=0;
    FirebaseDatabase database;
    DatabaseReference questions,user;

    ProgressBar progressBar;
    ImageView question_image;
    Button btnA,btnB,btnC,btnD,btnsubmit;
    ImageView previous,next;
    TextView question_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        Toolbar toolbar = findViewById(R.id.playtoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.transparent_icon);

        submitmedia = MediaPlayer.create(this,R.raw.fordback);

        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");
        user=database.getReference("Users");

        question_text = (TextView) findViewById(R.id.question_text);
        question_image = (ImageView)findViewById(R.id.question_image);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        btnA = (Button)findViewById(R.id.btnAnswerA);
        btnB = (Button)findViewById(R.id.btnAnswerB);
        btnC = (Button)findViewById(R.id.btnAnswerC);
        btnD = (Button)findViewById(R.id.btnAnswerD);
        btnsubmit = (Button)findViewById(R.id.btnsubmit);

        previous = (ImageView)findViewById(R.id.previous);
        next = (ImageView)findViewById(R.id.next);

        btnsubmit.setOnClickListener(this);
        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);

        totalQuestion = Common.questionList.size();
        questionattempted = new boolean[totalQuestion];

        progressBar.setProgress(index*100/totalQuestion);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login.getsharedpreference(Common.currentuser.getUsername(),Login.userdetails)=="true"){
                    submitmedia.start();
                }
                if(questionshow==0){
                    Toast.makeText(Playing.this,"This is the first question",Toast.LENGTH_SHORT).show();
                }
                else {
                    btnB.setTextColor(Color.WHITE);
                    btnA.setTextColor(Color.WHITE);
                    btnC.setTextColor(Color.WHITE);
                    btnD.setTextColor(Color.WHITE);
                        for(int counter=questionshow-1;counter>=0;counter--) {
                            if (!questionattempted[counter]){
                                questionshow=counter;
                                progressBar.setProgress(questionshow*100/totalQuestion);
                                showQuestion(counter);
                                break;}
                        }
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login.getsharedpreference(Common.currentuser.getUsername(),Login.userdetails)=="true"){
                    submitmedia.start();
                }
                if (questionshow==totalQuestion){
                    Toast.makeText(Playing.this,"This is the Last question",Toast.LENGTH_SHORT).show();
                }
                else{
                    btnB.setTextColor(Color.WHITE);
                    btnA.setTextColor(Color.WHITE);
                    btnC.setTextColor(Color.WHITE);
                    btnD.setTextColor(Color.WHITE);
                     for(int counter=questionshow+1;counter<totalQuestion;counter++){
                         if(!questionattempted[counter]){
                             questionshow=counter;
                             progressBar.setProgress(questionshow*100/totalQuestion);
                             showQuestion(counter);
                             break;}
                     }
                }
            }
        });
        showQuestion(index);
    }

    @Override
    public void onClick(View v) {

        Bundle extra=getIntent().getExtras();
        if(extra != null){
            difficultylevel = extra.getInt("Difficulty");
        }

        if (difficultylevel==4){
            lives=3;
        }

        switch (v.getId()) {

            case R.id.btnAnswerA: {
                btnA.setTextColor(Color.BLACK);
                btnB.setTextColor(Color.WHITE);
                btnC.setTextColor(Color.WHITE);
                btnD.setTextColor(Color.WHITE);
                ClickedButton_txt = (String) btnA.getText();
                break;
            }
            case R.id.btnAnswerB: {
                btnB.setTextColor(Color.BLACK);
                btnA.setTextColor(Color.WHITE);
                btnC.setTextColor(Color.WHITE);
                btnD.setTextColor(Color.WHITE);
                ClickedButton_txt = (String) btnB.getText();
                break;
            }
            case R.id.btnAnswerC: {
                btnC.setTextColor(Color.BLACK);
                btnB.setTextColor(Color.WHITE);
                btnA.setTextColor(Color.WHITE);
                btnD.setTextColor(Color.WHITE);
                ClickedButton_txt = (String) btnC.getText();
                break;
            }
            case R.id.btnAnswerD: {
                btnD.setTextColor(Color.BLACK);
                btnB.setTextColor(Color.WHITE);
                btnC.setTextColor(Color.WHITE);
                btnA.setTextColor(Color.WHITE);
                ClickedButton_txt = (String) btnD.getText();
                break;
            }
            case R.id.btnsubmit: {
                if (Login.getsharedpreference(Common.currentuser.getUsername(),Login.userdetails)=="true"){
                    submitmedia.start();
                }
                btnD.setTextColor(Color.WHITE);
                btnB.setTextColor(Color.WHITE);
                btnC.setTextColor(Color.WHITE);
                btnA.setTextColor(Color.WHITE);
                questionattempted[questionshow]=true;
                    if (ClickedButton_txt != null) {
                        ChangingAttempts(ClickedButton_txt, questionshow);
                        if (ClickedButton_txt.equals(Common.questionList.get(questionshow).getCorrectAnswer())) {
                            score = score + 10;
                            correctAnswer = correctAnswer + 1;
                            index=questionshow;
                            index = index + 1;
                            ClickedButton_txt=null;
                            showQuestion(index);
                        } else {
                            if (difficultylevel==4)
                            lives = lives - 1;
                            index=questionshow;
                            index = index + 1;
                            ClickedButton_txt=null;
                            showQuestion(index);
                        }
                    } else {
                        Toast.makeText(Playing.this, "Select a option", Toast.LENGTH_SHORT).show();
                    }

                break;
            }

        }
    }

    private void showQuestion(int now) {
        questionshow=now;
        if(now < totalQuestion&&lives>=0){
            progressBar.setProgress(now*100/totalQuestion);

            if(Common.questionList.get(now).getIsImageQuestion().equals("true")){
                Picasso.with(getBaseContext()).load(Common.questionList.get(now).getQuestion()).into(question_image);
                question_image.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);
            }
            else{
                question_text.setText(Common.questionList.get(now).getQuestion());
                question_text.setVisibility(View.VISIBLE);
                question_image.setVisibility(View.INVISIBLE);
            }
            btnA.setText(Common.questionList.get(now).getAnswerA());
            btnB.setText(Common.questionList.get(now).getAnswerB());
            btnC.setText(Common.questionList.get(now).getAnswerC());
            btnD.setText(Common.questionList.get(now).getAnswerD());

        }
        else if(now >= totalQuestion&&lives>=0&&!allQuestionAttempted) {
            for (int counter = 0; counter < totalQuestion-1; counter++) {
              if (!questionattempted[counter]){
                    questionshow=counter;
                    index=questionshow;
                    showQuestion(counter);
                }
                if (questionattempted[counter]&&counter==totalQuestion-2){
                    allQuestionAttempted = true;
                    Intent intent = new Intent(this,Done.class);
                    Bundle dataSend = new Bundle();
                    dataSend.putInt("Score",score);
                    dataSend.putInt("Total",totalQuestion);
                    dataSend.putInt("Correct",correctAnswer);
                    intent.putExtras(dataSend);
                    startActivity(intent);
                    finish();
                }
            }
        }
        else{
            Intent intent = new Intent(this,Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("Score",score);
            dataSend.putInt("Total",totalQuestion);
            dataSend.putInt("Correct",correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }

    }

    private void ChangingAttempts(String clickedButton_txt ,int index) {

        if (clickedButton_txt.equals(Common.questionList.get(index).getCorrectAnswer())){
            user.child(Common.currentuser.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User uname=dataSnapshot.getValue(User.class);
                    user.child(Common.currentuser.getUsername()).child("correctAttempts").setValue(String.valueOf(Integer.parseInt(uname.getCorrectAttempts())+1));
                    user.child(Common.currentuser.getUsername()).child("questionsAttempted").setValue(String.valueOf(Integer.parseInt(uname.getQuestionsAttempted())+1));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            questions.child(Common.questionList.get(index).getQno()).child("NoOfAttempts").setValue(String.valueOf(Integer.parseInt(Common.questionList.get(index).getNoOfAttempts())+1));
            questions.child(Common.questionList.get(index).getQno()).child("NoOfCorrectAttempts").setValue(String.valueOf(Integer.parseInt(Common.questionList.get(index).getNoOfCorrectAttempts())+1));
        }
        else {
            user.child(Common.currentuser.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User uname=dataSnapshot.getValue(User.class);
                    user.child(Common.currentuser.getUsername()).child("questionsAttempted").setValue(String.valueOf(Integer.parseInt(uname.getQuestionsAttempted())+1));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            questions.child(Common.questionList.get(index).getQno()).child("NoOfAttempts").setValue(String.valueOf(Integer.parseInt(Common.questionList.get(index).getNoOfAttempts())+1));
        }

        ChangingDifficulty(Common.questionList.get(index));

        }

    private void ChangingDifficulty(Question question) {

        Double ratio = Double.valueOf(question.getNoOfAttempts())/Double.valueOf(question.getNoOfCorrectAttempts());
        if(ratio<3.5){
        questions.child(question.getQno()).child("Difficulty").setValue("Easy");}
        if(ratio>=3.5&&ratio<=8){
        questions.child(question.getQno()).child("Difficulty").setValue("Normal");}
        if(ratio>8){
        questions.child(question.getQno()).child("Difficulty").setValue("Hard");}

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.donequiz_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Playing.this);
            builder.setTitle("Are you sure");
            builder.setMessage("You are done with the quiz");
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent intent = new Intent(Playing.this,Done.class);
                    Bundle dataSend = new Bundle();
                    dataSend.putInt("Score",score);
                    dataSend.putInt("Total",totalQuestion);
                    dataSend.putInt("Correct",correctAnswer);
                    intent.putExtras(dataSend);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Either submit all questions or press done quiz",Toast.LENGTH_LONG).show();
    }

}
