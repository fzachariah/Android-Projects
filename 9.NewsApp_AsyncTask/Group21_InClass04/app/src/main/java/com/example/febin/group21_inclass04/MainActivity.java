package com.example.febin.group21_inclass04;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements FetchNews.IData,FetchImage.IDataImage {


    Spinner spinner;
    ArrayAdapter<CharSequence> staticAdapter;
    ImageView imageView;

    LinearLayout linearLayout;

    ImageButton imageButtonFirst;
    ImageButton imageButtonPrev;
    ImageButton imageButtonNext;
    ImageButton imageButtonLast;

    TextView textViewTitle;
    TextView textViewAuthor;
    TextView textViewDate;
    TextView textViewDescription;



    ArrayList<News> finalNewses;
    ArrayList<Bitmap> image;
    int count=0;

    Button buttonFinish;
    Button buttonGet;

    final String KEY="b3987b48abf94dce8307f914edbe3f49";

    @Override
    public void setUpData(ArrayList<News> newses) {
        finalNewses=newses;
        Log.d("22222222222Debug",""+finalNewses.size());
        if(finalNewses.size()==0)
        {
            Toast.makeText(getApplicationContext(),"Try again",Toast.LENGTH_SHORT).show();
        }
        else
        {
            String url=finalNewses.get(count).getUrlToImage();
            new FetchImage(this).execute(url);


        }

    }

    @Override
    public Context getContext() {
        return this;
    }
    @Override
    public void setUpImage(Bitmap bitmap) {

        imageView.setImageBitmap(bitmap);
        News news=finalNewses.get(count);
        String dateString="";
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = fmt.parse(news.getPublishedAt());
            dateString=fmt.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        textViewTitle.setText("Title: "+news.getTitle());
        if(!news.getAuthor().equals("null")) {
            textViewAuthor.setText("Author:" + news.getAuthor());
        }
        else
        {
            textViewAuthor.setText("Author:");
        }
        textViewDate.setText("Published On:"+dateString);
        textViewDescription.setText("Description: \n"+news.getDescription());
        imageButtonFirst.setEnabled(true);
        imageButtonLast.setEnabled(true);
        imageButtonNext.setEnabled(true);
        imageButtonPrev.setEnabled(true);
        buttonFinish.setEnabled(true);




    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("News App");
        linearLayout=(LinearLayout)findViewById(R.id.linearLyoutInner);
        imageView=(ImageView)findViewById(R.id.imageView);
        spinner=(Spinner)findViewById(R.id.spinner2);


        textViewTitle =(TextView)findViewById(R.id.textViewTitle);
        textViewAuthor =(TextView)findViewById(R.id.textViewAuthor);
        textViewDate =(TextView)findViewById(R.id.textViewDate);
        textViewDescription =(TextView)findViewById(R.id.textViewDescription);


        staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.source_array,
                        android.R.layout.simple_spinner_item);
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(staticAdapter);

        imageButtonFirst=(ImageButton)findViewById(R.id.imageButtonFirst);
        imageButtonFirst.setEnabled(false);
        imageButtonPrev=(ImageButton)findViewById(R.id.imageButtonPrev);
        imageButtonPrev.setEnabled(false);
        imageButtonNext=(ImageButton)findViewById(R.id.imageButtonNext);
        imageButtonNext.setEnabled(false);
        imageButtonLast=(ImageButton)findViewById(R.id.imageButtonLast);
        imageButtonLast.setEnabled(false);
        buttonFinish=(Button)findViewById(R.id.buttonFinish);
        buttonFinish.setEnabled(false);
        buttonGet=(Button)findViewById(R.id.buttonGetNews);
        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String news=spinner.getSelectedItem().toString();
                if(news.equals("Select"))
                {
                    Toast.makeText(getApplicationContext(),"Please select an item",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    count=0;
                    if(news.equals("BBC"))
                    {
                        news="bbc-news";
                    }
                    else if(news.equals("CNN"))
                    {
                        news="cnn";
                    }
                    else if(news.equals("Buzzfeed"))
                    {
                        news="buzzfeed";
                    }
                    else if(news.equals("ESPN"))
                    {
                        news="espn";
                    }
                    else if(news.equals("Sky News"))
                    {
                        news="sky-news";
                    }


                    new FetchNews(MainActivity.this).execute(news);

                }
            }
        });


        imageButtonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==0)
                {
                    Toast.makeText(getApplicationContext(),"Already Reached the First News",Toast.LENGTH_SHORT).show();
                }
                else {
                    count = 0;
                    String url = finalNewses.get(count).getUrlToImage();
                    imageButtonFirst.setEnabled(false);
                    imageButtonLast.setEnabled(false);
                    imageButtonNext.setEnabled(false);
                    imageButtonPrev.setEnabled(false);
                    buttonFinish.setEnabled(false);
                    new FetchImage(MainActivity.this).execute(url);
                }
            }
        });

        imageButtonLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==finalNewses.size()-1)
                {
                    Toast.makeText(getApplicationContext(),"Already Reached the Last News",Toast.LENGTH_SHORT).show();
                }
                else {
                    count = finalNewses.size() - 1;
                    String url = finalNewses.get(count).getUrlToImage();
                    imageButtonFirst.setEnabled(false);
                    imageButtonLast.setEnabled(false);
                    imageButtonNext.setEnabled(false);
                    imageButtonPrev.setEnabled(false);
                    buttonFinish.setEnabled(false);
                    new FetchImage(MainActivity.this).execute(url);
                }
            }
        });

        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==finalNewses.size()-1)
                {
                    Toast.makeText(getApplicationContext(),"Already Reached the Last News",Toast.LENGTH_SHORT).show();
                }
                else {
                    count = count + 1;
                    String url = finalNewses.get(count).getUrlToImage();
                    imageButtonFirst.setEnabled(false);
                    imageButtonLast.setEnabled(false);
                    imageButtonNext.setEnabled(false);
                    imageButtonPrev.setEnabled(false);
                    buttonFinish.setEnabled(false);
                    new FetchImage(MainActivity.this).execute(url);
                }
            }
        });
        imageButtonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==0)
                {
                    Toast.makeText(getApplicationContext(),"Already Reached the First News",Toast.LENGTH_SHORT).show();
                }
                else {
                    count--;
                    String url = finalNewses.get(count).getUrlToImage();
                    imageButtonFirst.setEnabled(false);
                    imageButtonLast.setEnabled(false);
                    imageButtonNext.setEnabled(false);
                    buttonFinish.setEnabled(false);
                    imageButtonPrev.setEnabled(false);
                    new FetchImage(MainActivity.this).execute(url);
                }
            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });


    }



}
