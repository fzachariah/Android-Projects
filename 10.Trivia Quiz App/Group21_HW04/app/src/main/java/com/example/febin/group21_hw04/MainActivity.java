package com.example.febin.group21_hw04;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements   FetchQuestionsAsync.IData{

    ImageView imageView;
    Button buttonExit;
    Button buttonStart;
    String DATA_URL="http://dev.theappsdr.com/apis/trivia_json/index.php";
    TextView textView;
    ProgressBar progressBar;
    final String DATA="DATA_PASS";
    ArrayList<Question> pass=new ArrayList<>();




    @Override
    public void setUpData(ArrayList<Question> questions) {
        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        imageView.setImageResource(R.drawable.trivia);
        buttonStart.setEnabled(true);
        pass=questions;



    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Trivia App");

        progressBar=(ProgressBar)findViewById(R.id.progressBar3);
        textView=(TextView)findViewById(R.id.textViewMessage);
        imageView=(ImageView)findViewById(R.id.imageView);
        buttonExit =(Button)findViewById(R.id.buttonExit);
        buttonStart=(Button)findViewById(R.id.buttonStart);
        buttonStart.setEnabled(false);

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                System.exit(0);

            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.size()!=0) {
                    Log.d("555555", "" + pass.size());
                    Intent intent = new Intent(MainActivity.this, Trivia.class);
                    intent.putExtra(DATA, pass);
                    startActivity(intent);
                }
                else
                {
                    Log.d("888Inside","Try again");
                    Toast.makeText(getApplicationContext(),"try Again",Toast.LENGTH_SHORT).show();
                }
            }
        });


        new FetchQuestionsAsync(MainActivity.this).execute(DATA_URL);







    }
}
