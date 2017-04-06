package com.example.febin.group21_hw04;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;

public class Trivia extends AppCompatActivity implements FetchImageAsync.IDataImage {

    TextView textViewMessage;
    ProgressBar progressBar;

    FetchImageAsync fetchImageAsync;

    TextView textViewQnumber;
    TextView textViewTimer;
    TextView textViewQuestion;

    RadioGroup radioGroup;

    Button buttonPrevious;
    Button buttonNext;

    ImageView imageView;
    int array[];

    CountDownTimer countDownTimer;
    final String DATA="DATA_PASS";
    final String DATA_PASS="QUESTIONS";
    final String ANSWERS="ANSWERS";
    int count=0;
    ArrayList<Question> questionArrayList;
    int size;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        setTitle("Trivia");
        textViewMessage=(TextView)findViewById(R.id.textViewMessageTrivia);
        progressBar=(ProgressBar) findViewById(R.id.progressBarTrivia);

        textViewQnumber =(TextView)findViewById(R.id.textViewQNumber);
        textViewTimer=(TextView)findViewById(R.id.textViewTimer);
        textViewQuestion=(TextView) findViewById(R.id.textViewQuestion);
        imageView=(ImageView)findViewById(R.id.imageView2);


        radioGroup=(RadioGroup)findViewById(R.id.rgOptions);
        buttonNext=(Button)findViewById(R.id.buttonNext);
        buttonPrevious=(Button)findViewById(R.id.buttonPrev);
        if(count==0)
        {
            buttonPrevious.setEnabled(false);
        }
        else
        {
            buttonPrevious.setEnabled(true);
        }

        questionArrayList= (ArrayList<Question>) getIntent().getSerializableExtra(DATA);
        size=questionArrayList.size();
        array=new int[size];
        countDownTimer=new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                textViewTimer.setText("Time Left: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Intent intent=new Intent(Trivia.this,Stats.class);
                intent.putExtra(DATA_PASS,questionArrayList);
                intent.putExtra(ANSWERS,array);
                if(fetchImageAsync!=null)
                {
                    fetchImageAsync.cancel(true);
                }
                cancel();
                startActivity(intent);

            }
        }.start();
        dataBuilt();




    }

    @Override
    public void setUpImage(HashMap<Integer,Bitmap> upImage) {


        int valid=0;
        for(int value:upImage.keySet())
        {
            valid=value;
        }
        if(valid==count) {
            textViewMessage.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(upImage.get(valid));
        }




    }

    public void clickNext(View view)
    {

        if(count==size-1)
        {
            Intent intent=new Intent(Trivia.this,Stats.class);
            intent.putExtra(DATA_PASS,questionArrayList);
            intent.putExtra(ANSWERS,array);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            array[count]=radioGroup.getCheckedRadioButtonId();
            if(fetchImageAsync!=null)
            {
                fetchImageAsync.cancel(true);
            }
            countDownTimer.cancel();
            startActivity(intent);

        }else {
            imageView.setVisibility(View.GONE);
            textViewMessage.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            array[count]=radioGroup.getCheckedRadioButtonId();
            count++;
            if(count==size-1)
            {
                buttonNext.setText("Finish");
            }
            buttonPrevious.setEnabled(true);
            if(fetchImageAsync!=null)
            {
                fetchImageAsync.cancel(true);
            }
            dataBuilt();
        }


    }

    public void clickPrev(View view)
    {


        if(count==0)
        {
            Toast.makeText(getApplicationContext(),"This is the First Question",Toast.LENGTH_SHORT).show();
        }
        else {
            if(count==size-1) {
                buttonNext.setText("Next");
            }
            imageView.setVisibility(View.GONE);
            textViewMessage.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            array[count]=radioGroup.getCheckedRadioButtonId();
            count--;
            if(count==0)
            {
                buttonPrevious.setEnabled(false);
            }
            else
            {
                buttonPrevious.setEnabled(true);
            }
            if(fetchImageAsync!=null)
            {
                fetchImageAsync.cancel(true);
            }
            dataBuilt();
        }
    }

    public void dataBuilt()
    {
        Question question=questionArrayList.get(count);
        String url=question.getImageURL();
        String questionText=question.getQuestion();
        textViewQnumber.setText("Q"+(count+1));
        textViewQuestion.setText(questionText);
        ArrayList<String> choices=question.getChoices();
        radioGroup.removeAllViews();
        radioGroup.clearCheck();
        for(int i=0;i<choices.size();i++)
        {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(choices.get(i));
            radioButton.setId(i+1);
            radioGroup.addView(radioButton);


        }
        if(array[count]!=0)
        {
            radioGroup.check(array[count]);
        }
        if(!url.equals("")) {
            fetchImageAsync=new FetchImageAsync(this);
            fetchImageAsync.execute(url,""+count);
        }
        else
        {
            textViewMessage.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }


}
