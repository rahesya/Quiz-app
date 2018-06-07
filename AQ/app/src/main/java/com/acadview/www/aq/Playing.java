package com.acadview.www.aq;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
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
import com.squareup.picasso.Picasso;

public class Playing extends AppCompatActivity implements View.OnClickListener{

    final static long INTERVAL = 1000;
    final static long TIMEOUT = 60000;

    String ClickedButton_txt;
    int progressValue =0;

    CountDownTimer mCountDown;
    int index=0,score =0,thisQuestion=0,totalQuestion,correctAnswer;

    ProgressBar progressBar;
    ImageView question_image;
    Button btnA,btnB,btnC,btnD,btnsubmit;
    TextView question_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        question_text = (TextView) findViewById(R.id.question_text);
        question_image = (ImageView)findViewById(R.id.question_image);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        btnA = (Button)findViewById(R.id.btnAnswerA);
        btnB = (Button)findViewById(R.id.btnAnswerB);
        btnC = (Button)findViewById(R.id.btnAnswerC);
        btnD = (Button)findViewById(R.id.btnAnswerD);
        btnsubmit = (Button)findViewById(R.id.btnsubmit);

        btnsubmit.setOnClickListener(this);
        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);


        totalQuestion = Common.questionList.size();

        mCountDown = new CountDownTimer(TIMEOUT,INTERVAL) {
            @Override
            public void onTick(long minisec ) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                mCountDown.cancel();
                showQuestion(++index);
            }
        };

        showQuestion(index);

    }

    @Override
    public void onClick(View v)  {
        mCountDown.cancel();
        if(index<totalQuestion){
            Button clickedButton = (Button)v;
            if(clickedButton.getId()==R.id.btnsubmit) {
                if (ClickedButton_txt != null) {
                    if(ClickedButton_txt.equals(Common.questionList.get(index).getCorrectAnswer())){
                        score = score + 10;
                        if(btnA.getText()==ClickedButton_txt)
                            btnA.setBackgroundColor(getResources().getColor(R.color.correct_ans));
                        else if(btnB.getText()==ClickedButton_txt)
                                btnB.setBackgroundColor(getResources().getColor(R.color.correct_ans));
                        else if(btnC.getText()==ClickedButton_txt)
                                btnC.setBackgroundColor(getResources().getColor(R.color.correct_ans));
                        else if(btnD.getText()==ClickedButton_txt)
                                btnD.setBackgroundColor(getResources().getColor(R.color.correct_ans));
                        correctAnswer++;
                        showQuestion(++index);}
                    else{
                        if(btnA.getText()==ClickedButton_txt)
                            btnA.setBackgroundColor(getResources().getColor(R.color.wrong_ans));
                        else if(btnB.getText()==ClickedButton_txt)
                            btnB.setBackgroundColor(getResources().getColor(R.color.wrong_ans));
                        else if(btnC.getText()==ClickedButton_txt)
                            btnC.setBackgroundColor(getResources().getColor(R.color.wrong_ans));
                        else if(btnD.getText()==ClickedButton_txt)
                            btnD.setBackgroundColor(getResources().getColor(R.color.wrong_ans));
                        showQuestion(++index);
                    }
                }
                else {
                    Toast.makeText(Playing.this, "Select a option "+ClickedButton_txt, Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                clickedButton.setBackgroundResource(R.drawable.btnstate_change);
                if(btnA.getText()==ClickedButton_txt)
                    btnA.setBackgroundResource(R.drawable.btnstate_change);
                else if(btnB.getText()==ClickedButton_txt)
                    btnB.setBackgroundResource(R.drawable.btnstate_change);
                else if(btnC.getText()==ClickedButton_txt)
                    btnC.setBackgroundResource(R.drawable.btnstate_change);
                else if(btnD.getText()==ClickedButton_txt)
                    btnD.setBackgroundResource(R.drawable.btnstate_change);
                ClickedButton_txt = (String) clickedButton.getText();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.donequiz_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("Score",score);
            dataSend.putInt("Total",totalQuestion);
            dataSend.putInt("Correct",correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showQuestion(int index) {
        if(index < totalQuestion){
            thisQuestion++;
            progressBar.setProgress(0);
            progressValue=0;

            if(Common.questionList.get(index).getIsImageQuestion().equals("true")){
                Picasso.with(getBaseContext()).load(Common.questionList.get(index).getQuestion())
                        .into(question_image);
                question_image.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);
            }
            else{
                question_text.setText(Common.questionList.get(index).getQuestion());
                question_text.setVisibility(View.VISIBLE);
                question_image.setVisibility(View.INVISIBLE);
            }
            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());

            mCountDown.start();
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

    public void onTapColourChange(View view){
        switch (view.getId()){

            case R.id.btnAnswerA:{

                break;
            }
            case R.id.btnAnswerB:{
                break;
            }
            case R.id.btnAnswerC:{
                break;
            }
            case R.id.btnAnswerD:{
                break;
            }

        }
    }

}
