package com.example.febin.inclass06_zachariah;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SimilarDetails extends AppCompatActivity implements  FetchImageAsync.IDataImage{

    final String DATA_KEY="DATA OBJECT";
    TextView textViewTitle;
    TextView textViewOverView;
    TextView textGenre;
    TextView textViewPublisher;

    ImageView imageView;

    Button buttonFinish;
    Button buttonSimilar;
    ProgressDialog progressDialog;
    SingleGame singleGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_details);
        setTitle("Game Details");


        progressDialog=ProgressDialog.show(this,null,null);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C8C8C8")));
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.loader1);
        progressDialog.getWindow().setLayout(620,450);
        singleGame=(SingleGame)getIntent().getSerializableExtra(DATA_KEY);

        textViewTitle=(TextView)findViewById(R.id.textViewTitleSimilar);
        textViewOverView=(TextView)findViewById(R.id.textViewOverViewSimilar);
        textGenre=(TextView)findViewById(R.id.textViewGenreSimilar);
        textViewPublisher=(TextView)findViewById(R.id.textViewPublisherSimilar);
        textViewOverView.setMovementMethod(new ScrollingMovementMethod());

        imageView=(ImageView)findViewById(R.id.imageViewSimilar);
        buttonFinish=(Button)findViewById(R.id.buttonFinishSimilar);

        Log.d("image_url: ",""+singleGame.getImageURL());
        new FetchImageAsync(SimilarDetails.this).execute(singleGame.getImageURL());


        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
}
