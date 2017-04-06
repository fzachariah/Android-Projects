package com.example.febin.group21_hw07;

import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayActivity extends AppCompatActivity {

    final String DATA_PASSED="Data_Passed";
    Episode episodeObj;

    TextView textViewTitle;
    TextView textViewDescription;
    TextView textViewDuration;
    TextView textViewDate;

    private Handler myHandler = new Handler();;

    ImageView imageView;
    SeekBar seekBarPlay;
    ImageButton imageButtonPlay;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        setTitle("Play!");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        textViewTitle =(TextView)findViewById(R.id.textViewPlayTitle);
        textViewDescription=(TextView)findViewById(R.id.textViewPlayDesc);
        textViewDescription.setMovementMethod(new ScrollingMovementMethod());
        textViewDuration=(TextView)findViewById(R.id.textViewPlayDur);
        textViewDate=(TextView)findViewById(R.id.textViewPlayDate);

        imageView =(ImageView)findViewById(R.id.imageViewPlayTitle);
        seekBarPlay=(SeekBar)findViewById(R.id.seekBarPlay);
        imageButtonPlay=(ImageButton)findViewById(R.id.imageButtonPlayPause);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action)));
        episodeObj=(Episode)getIntent().getSerializableExtra(DATA_PASSED);

        String title=episodeObj.getTitle();
        String description=episodeObj.getDescription();
        String duration=episodeObj.getDuration();
        String date=episodeObj.getReleaseDate();
        date=date.substring(0,16);
        date=date.substring(date.indexOf(",")+1).trim();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("DD MMM yyyy");
        try {
            Date date1=simpleDateFormat.parse(date);
            SimpleDateFormat sdf=new SimpleDateFormat("MM/DD/yyyy");
            date=sdf.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("Date:",date);
        if(title!=null && title.length()>0) {
            textViewTitle.setText(""+title);
        }
        else
        {
            textViewTitle.setText("");
        }

        if(description!=null &&description.length()>0)
        {
            description="<font color=#0000ff>Description: </font>"+description;
            textViewDescription.setText(Html.fromHtml(description));
        }
        else
        {
            textViewDescription.setText("Description: ");
        }

        Picasso.with(this).load(episodeObj.getImageURL()).fit().centerCrop().into(imageView);

        if(date!=null && date.length()>0)
        {
            textViewDate.setText("Publication Date: "+date);
        }
        else
        {
            textViewDate.setText("Publication Date:");
        }

        if(duration!=null &&duration.length()>0)
        {
            textViewDuration.setText("Duration: "+duration);
        }
        else
        {
            textViewDuration.setText("Duration: ");
        }


        imageButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
                    if(mediaPlayer!=null){
                        mediaPlayer.pause();
                        // Changing button image to play button
                        imageButtonPlay.setImageResource(R.drawable.playbutton);
                    }
                }else{
                    // Resume song
                    if(mediaPlayer!=null){
                        mediaPlayer.start();
                        imageButtonPlay.setImageResource(R.drawable.pausebutton);
                        // Changing button image to pause button

                    }
                    else
                    {
                        mediaPlayer=new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        try {
                            mediaPlayer.setDataSource(episodeObj.getMediaURL());
                            mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        imageButtonPlay.setImageResource(R.drawable.pausebutton);
                        Log.d("Duration",""+mediaPlayer.getDuration());
                        seekBarPlay.setProgress(0);
                        seekBarPlay.setMax(mediaPlayer.getDuration()/1000);
                        mediaPlayer.start();
                    }
                }
            }
        });


        PlayActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(mediaPlayer != null){
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    Log.d("testing",""+mCurrentPosition);
                    seekBarPlay.setProgress(mCurrentPosition);
                    if(mCurrentPosition==seekBarPlay.getMax())
                    {
                        mediaPlayer.stop();
                        mediaPlayer=null;
                        seekBarPlay.setProgress(0);
                        imageButtonPlay.setImageResource(R.drawable.playbutton);

                    }
                }
                myHandler.postDelayed(this, 1000);
            }
        });

        seekBarPlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mediaPlayer!=null &&mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
        }
        finish();
    }
}
