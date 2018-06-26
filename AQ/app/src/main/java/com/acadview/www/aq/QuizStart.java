package com.acadview.www.aq;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class QuizStart extends AppCompatActivity implements View.OnClickListener{

    Button btnchallenging,btneasy,btnnormal,btnhard;

    FirebaseDatabase database;
    boolean gotonextclass=false;
    DatabaseReference questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_start);

        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");

        btnchallenging =(Button)findViewById(R.id.challenging);
        btneasy = (Button)findViewById(R.id.easy);
        btnnormal = (Button)findViewById(R.id.normal);
        btnhard = (Button)findViewById(R.id.hard);

        btnchallenging.setOnClickListener(this);
        btnhard.setOnClickListener(this);
        btnnormal.setOnClickListener(this);
        btneasy.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.challenging:{

                loadQuestionForChallengingMode(Common.categoryId);
                if (gotonextclass) {
                    Intent intent = new Intent(QuizStart.this, Playing.class);
                    Bundle dataSend = new Bundle();
                    dataSend.putInt("Difficulty", 4);
                    intent.putExtras(dataSend);
                    startActivity(intent);
                    finish();
                }
                break;
            }
            case R.id.hard:{

                loadQuestionForHardMode(Common.categoryId);
                if (gotonextclass) {
                    Intent intent = new Intent(QuizStart.this, Playing.class);
                    Bundle dataSend = new Bundle();
                    dataSend.putInt("Difficulty", 3);
                    intent.putExtras(dataSend);
                    startActivity(intent);
                    finish();
                }
                break;
            }
            case R.id.normal:{

                loadQuestionForNormalMode(Common.categoryId);
                if (gotonextclass) {
                    Intent intent = new Intent(QuizStart.this, Playing.class);
                    Bundle dataSend = new Bundle();
                    dataSend.putInt("Difficulty", 2);
                    intent.putExtras(dataSend);
                    startActivity(intent);
                    finish();
                }
                break;
            }
            case R.id.easy:{

                loadQuestionForEasyMode(Common.categoryId);
                if (gotonextclass) {
                    Intent intent = new Intent(QuizStart.this, Playing.class);
                    Bundle dataSend = new Bundle();
                    dataSend.putInt("Difficulty", 1);
                    intent.putExtras(dataSend);
                    startActivity(intent);
                    finish();
                }
                break;
            }

        }
    }

    private void loadQuestionForNormalMode(String categoryId) {

        if(Common.questionList.size()>0){
            Common.questionList.clear();
        }

        questions.orderByChild("CategoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gotonextclass=false;
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Question ques =postSnapshot.getValue(Question.class);
                    if(ques.getDifficulty().equals("Normal")){
                        Common.questionList.add(ques);
                    }
                }
                gotonextclass = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Collections.shuffle(Common.questionList);

    }

    private void loadQuestionForEasyMode(String categoryId) {

        if(Common.questionList.size()>0){
            Common.questionList.clear();
        }

        questions.orderByChild("CategoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gotonextclass=false;
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Question ques =postSnapshot.getValue(Question.class);
                    if(ques.getDifficulty().equals("Easy")){
                        Common.questionList.add(ques);
                    }
                }
                gotonextclass =true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Collections.shuffle(Common.questionList);

    }

    private void loadQuestionForHardMode(String categoryId) {

        if(Common.questionList.size()>0){
            Common.questionList.clear();
        }

        questions.orderByChild("CategoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gotonextclass=false;
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Question ques =postSnapshot.getValue(Question.class);
                    if(ques.getDifficulty().equals("Hard")){
                        Common.questionList.add(ques);
                    }
                }

                gotonextclass=true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Collections.shuffle(Common.questionList);

    }

    private void loadQuestionForChallengingMode(String categoryId) {

        if(Common.questionList.size()>0){
            Common.questionList.clear();
        }

        questions.orderByChild("CategoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gotonextclass=false;
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Question ques = postSnapshot.getValue(Question.class);
                    Common.questionList.add(ques);
                    Collections.sort(Common.questionList,Question.Questionratio);
                }

                gotonextclass=true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
