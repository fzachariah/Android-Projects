package com.example.febin.group21_hw04;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Stats extends AppCompatActivity {

    Button buttonFinish;
    LinearLayout linearLayout;
    final String DATA_PASS="QUESTIONS";
    final String ANSWERS="ANSWERS";
    ProgressBar progressBar;
    TextView textViewStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        setTitle("Trivia Stats");
        linearLayout=(LinearLayout)findViewById(R.id.linearLayout);

        ArrayList<Question> questionArrayList= (ArrayList<Question>) getIntent().getSerializableExtra(DATA_PASS);
        Bundle extras = getIntent().getExtras();
        int[] array = extras.getIntArray(ANSWERS);
        int wrong=0;
        int total=questionArrayList.size();

        Log.d("checking",""+array.length);
        for(int i=0;i<questionArrayList.size();i++)
        {
            LinearLayout linearLayoutChild=new LinearLayout(this);
            linearLayoutChild.setOrientation(LinearLayout.VERTICAL);
            linearLayoutChild.setBackgroundResource(R.drawable.border);
            Question question=new Question();
            question=questionArrayList.get(i);
            int answer=question.getAnswer();
            int yourAnswer=array[i];

            String correctAnswer=question.getChoices().get(answer-1);
            Log.d("Testing:",correctAnswer);
            Log.d("index",""+(yourAnswer-1));
            String myAnswer="";
            if(yourAnswer>0)
            {
                myAnswer=question.getChoices().get(yourAnswer-1);
            }
            Log.d("Testing123:",myAnswer);
            String questionText=question.getQuestion();
            if(answer!=yourAnswer)
            {
                wrong++;
                TextView textViewQuestion=new TextView(this);
                textViewQuestion.setText(questionText);
                TextView textViewYourAnswer=new TextView(this);
                textViewYourAnswer.setBackgroundResource(R.color.textColour);
                textViewYourAnswer.setTextColor(Color.BLACK);
                textViewYourAnswer.setText(myAnswer);
                TextView textViewCorrect=new TextView(this);
                textViewCorrect.setText(correctAnswer);
                linearLayoutChild.addView(textViewQuestion);
                linearLayoutChild.addView(textViewYourAnswer);
                linearLayoutChild.addView(textViewCorrect);


            }
            else
            {

                continue;
            }
            linearLayout.addView(linearLayoutChild);

        }
        progressBar=(ProgressBar)findViewById(R.id.progressBarStats);
        progressBar.setMax(100);
        int percentage=((total-wrong)*100/total);
        Log.d("Percentage: ",""+percentage);
        progressBar.setProgress(percentage);
        textViewStats=(TextView)findViewById(R.id.textViewDisp);
        textViewStats.setText(""+percentage+"%");


        buttonFinish=(Button)findViewById(R.id.buttonFinish);
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( Stats.this, MainActivity.class );
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent( Stats.this, MainActivity.class );
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
