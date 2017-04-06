package com.example.febin.group21_hw05;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GameDetails extends AppCompatActivity implements FetchImageAsync.IDataImage,FetchSimilarAsync.IDataSimilar {
    final String DATA_KEY="DATA OBJECT";
    final String URL="URL";
    final String SIMILAR_DATA="Similar Data";
    final String SIMILAR_TITLE="Similar Title";

    final String VIDEO_LINK="VIDEO";
    final String VIDEO_TITLE="VIDEO_TITLE";


    TextView textViewTitle;
    TextView textViewOverView;
    TextView textGenre;
    TextView textViewPublisher;

    ImageView imageView;

    Button buttonFinish;
    Button buttonTrailer;
    Button buttonSimilar;
    ProgressDialog progressDialog;
    SingleGame singleGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);
        setTitle("Game Details");
        progressDialog=ProgressDialog.show(this,null,null);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C8C8C8")));
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.loader1);
        progressDialog.getWindow().setLayout(620,450);
        singleGame=(SingleGame)getIntent().getSerializableExtra(DATA_KEY);

        textViewTitle=(TextView)findViewById(R.id.textViewTitle);
        textViewOverView=(TextView)findViewById(R.id.textViewOverView);
        textGenre=(TextView)findViewById(R.id.textViewGenre);
        textViewPublisher=(TextView)findViewById(R.id.textViewPublisher);
        textViewOverView.setMovementMethod(new ScrollingMovementMethod());

        imageView=(ImageView)findViewById(R.id.imageView);
        buttonFinish=(Button)findViewById(R.id.buttonFinish);
        buttonSimilar=(Button)findViewById(R.id.buttonSimilar);
        buttonTrailer=(Button)findViewById(R.id.buttonPlay);

        Log.d("image_url: ",""+singleGame.getImageURL());
        new FetchImageAsync(GameDetails.this).execute(singleGame.getImageURL());




        buttonTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(singleGame.getVideoURL()!=null&&singleGame.getVideoURL().length()>0) {
                    Intent intent = new Intent(GameDetails.this, Web_View.class);
                    intent.putExtra(VIDEO_LINK, singleGame.getVideoURL());
                    intent.putExtra(VIDEO_TITLE,singleGame.getTitle());
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(GameDetails.this,"No Video Available",Toast.LENGTH_SHORT).show();
                }

            }
        });

       buttonFinish.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });

        buttonSimilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list=singleGame.getSimilarGames();
                if(list.size()==0)
                {
                    Toast.makeText(getApplicationContext(),"Similar Games are not Found",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog=ProgressDialog.show(GameDetails.this,null,null);
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C8C8C8")));
                    progressDialog.setCancelable(false);
                    progressDialog.setContentView(R.layout.loader1);
                    progressDialog.getWindow().setLayout(620,450);
                    new FetchSimilarAsync(GameDetails.this).execute(list);
                }
            }
        });


    }

    @Override
    public void setUpImage(Bitmap upImage) {
        if(upImage!=null) {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageBitmap(upImage);
        }
        else if (upImage==null&&singleGame.getImageURL().length()>0)
        {
            Picasso.with(this).load(singleGame.getImageURL()).fit()
                .into(imageView);
        }
        if(singleGame.getTitle()!=null) {
            textViewTitle.setText("" + singleGame.getTitle());
        }
        if(singleGame.getPublisher()!=null) {
            textViewPublisher.setText("Publisher: " + singleGame.getPublisher());
        }
        else
        {
            textViewPublisher.setText("Publisher: " );
        }
        if(singleGame.getOverView()!=null) {
            textViewOverView.setText(singleGame.getOverView());
        }
        ArrayList<String> genre=singleGame.getGenreList();
        String genreText="";
        for(int i=0;i<genre.size();i++)
        {
            genreText=""+genre.get(i);
            if(i<genre.size()-1)
            {
                genreText=genreText+",";
            }
        }
        textGenre.setText("Genre: "+genreText);

        progressDialog.dismiss();

    }

    @Override
    public void setUpDataDetails(ArrayList<Game> similarList) {

        Log.d("Similar lIst",""+similarList.size()+""+similarList.get(0).toString());
        progressDialog.dismiss();
        if(similarList!=null &&similarList.size()>0) {
            Intent intent = new Intent(GameDetails.this, SimilarActivity.class);
            intent.putExtra(SIMILAR_DATA, similarList);
            intent.putExtra(SIMILAR_TITLE, singleGame.getTitle());
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Error Occurred,Please Try Again",Toast.LENGTH_SHORT).show();
        }

    }
}
