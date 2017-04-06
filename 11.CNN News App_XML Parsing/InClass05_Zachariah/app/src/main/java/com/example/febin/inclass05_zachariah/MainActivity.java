package com.example.febin.inclass05_zachariah;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements  FetchXMLAsync.IData,FetchImageAsync.IDataImage{

    ImageButton imageButtonNext;
    ImageButton imageButtonPrev;
    ImageButton imageButtonFirst;
    ImageButton imageButtonLast;
    LinearLayout linearLayout;

    Button buttonGet;
    Button buttonFinish;

    ImageView imageView;
    ArrayList<News> finalNewsArrayList=new ArrayList<>();
    int count=0;
    int maximum;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setTitle("CNN News");

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        imageButtonFirst=(ImageButton)findViewById(R.id.imageButtonFirst);
        imageButtonPrev=(ImageButton)findViewById(R.id.imageButtonPrev);
        imageButtonNext=(ImageButton)findViewById(R.id.imageButtonNext);
        imageButtonLast=(ImageButton)findViewById(R.id.imageButtonLast);

        imageButtonFirst.setEnabled(false);
        imageButtonLast.setEnabled(false);
        imageButtonNext.setEnabled(false);
        imageButtonPrev.setEnabled(false);


        linearLayout=(LinearLayout)findViewById(R.id.linearLayout);

        buttonGet=(Button)findViewById(R.id.buttonget);
        buttonFinish=(Button)findViewById(R.id.buttonFinish);
        buttonFinish.setEnabled(false);

        imageView=(ImageView)findViewById(R.id.imageView);

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=0;
                progressDialog.show();
                new FetchXMLAsync(MainActivity.this).execute();

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
                    String url = finalNewsArrayList.get(count).getUrlToImage();
                    imageButtonFirst.setEnabled(false);
                    imageButtonLast.setEnabled(false);
                    imageButtonNext.setEnabled(false);
                    imageButtonPrev.setEnabled(false);
                    buttonFinish.setEnabled(false);
                    new FetchImageAsync(MainActivity.this).execute(url);
                }
            }
        });

        imageButtonLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==finalNewsArrayList.size()-1)
                {
                    Toast.makeText(getApplicationContext(),"Already Reached the Last News",Toast.LENGTH_SHORT).show();
                }
                else {
                    count = finalNewsArrayList.size() - 1;
                    String url = finalNewsArrayList.get(count).getUrlToImage();
                    imageButtonFirst.setEnabled(false);
                    imageButtonLast.setEnabled(false);
                    imageButtonNext.setEnabled(false);
                    imageButtonPrev.setEnabled(false);
                    buttonFinish.setEnabled(false);
                    new FetchImageAsync(MainActivity.this).execute(url);
                }
            }
        });

        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==finalNewsArrayList.size()-1)
                {
                    Toast.makeText(getApplicationContext(),"Already Reached the Last News",Toast.LENGTH_SHORT).show();
                }
                else {
                    count = count + 1;
                    String url = finalNewsArrayList.get(count).getUrlToImage();
                    imageButtonFirst.setEnabled(false);
                    imageButtonLast.setEnabled(false);
                    imageButtonNext.setEnabled(false);
                    imageButtonPrev.setEnabled(false);
                    buttonFinish.setEnabled(false);
                    new FetchImageAsync(MainActivity.this).execute(url);
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
                    String url = finalNewsArrayList.get(count).getUrlToImage();
                    imageButtonFirst.setEnabled(false);
                    imageButtonLast.setEnabled(false);
                    imageButtonNext.setEnabled(false);
                    buttonFinish.setEnabled(false);
                    imageButtonPrev.setEnabled(false);
                    new FetchImageAsync(MainActivity.this).execute(url);
                }
            }
        });



    }

    @Override
    public void setUpData(ArrayList<News> newsArrayList) {
        Log.d("Print Value",""+newsArrayList.size());
        finalNewsArrayList=newsArrayList;
        maximum=finalNewsArrayList.size();
        News news=finalNewsArrayList.get(count);
        new FetchImageAsync(MainActivity.this).execute(news.getUrlToImage());



    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setUpImage(Bitmap upImage) {
        imageView.setImageBitmap(upImage);
        News news=finalNewsArrayList.get(count);
        String dateString="";
        String dateTemp=news.getPublishedAt();
        dateTemp=dateTemp.substring(dateTemp.indexOf(",")+1).trim();

        SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy hh:mm:ss zzz");;
        try {
            Date date = fmt.parse(dateTemp);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
            dateString=dateFormatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TextView textViewTitle=new TextView(this);
        textViewTitle.setTypeface(null, Typeface.BOLD);
        textViewTitle.setText("Title: "+news.getTitle());

        TextView textViewDate=new TextView(this);
        textViewDate.setText("Published On:"+dateString);
        TextView textViewDescription=new TextView(this);
        textViewDescription.setText("Description: \n"+news.getDescription());
        linearLayout.removeAllViews();
        linearLayout.addView(textViewTitle);
        linearLayout.addView(textViewDate);
        linearLayout.addView(textViewDescription);
        imageButtonFirst.setEnabled(true);
        imageButtonLast.setEnabled(true);
        imageButtonNext.setEnabled(true);
        imageButtonPrev.setEnabled(true);
        buttonFinish.setEnabled(true);
        progressDialog.dismiss();
    }
}
