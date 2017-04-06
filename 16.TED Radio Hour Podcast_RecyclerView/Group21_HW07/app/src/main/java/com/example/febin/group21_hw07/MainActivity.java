package com.example.febin.group21_hw07;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FetchXMLAsync.IData {

    final String DATA_PASSED="Data_Passed";
    ProgressDialog progressDialog;
    ArrayList<Episode> episodes;

    private RecyclerView recyclerView;
    private EpisodeAdapter mAdapter;
    private GridAdapter gridAdapter;

    RecyclerView.LayoutManager mLayoutManager ;

    RelativeLayout relativeLayout;
    ImageButton imageButton;
    SeekBar seekBar;

    private Handler myHandler = new Handler();;

    private MediaPlayer mp;

    int status=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        relativeLayout=(RelativeLayout)findViewById(R.id.RelativeLayout);
        imageButton=(ImageButton) findViewById(R.id.imageButtonPause);
        seekBar =(SeekBar)findViewById(R.id.seekBar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action)));
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Episodes");
        progressDialog.show();
        new FetchXMLAsync(MainActivity.this).execute();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    if(mp!=null){
                        mp.pause();
                        // Changing button image to play button
                        imageButton.setImageResource(R.drawable.playbutton);
                    }
                }else{
                    // Resume song
                    if(mp!=null){
                        mp.start();
                        // Changing button image to pause button
                        imageButton.setImageResource(R.drawable.pausebutton);
                    }
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mp != null && fromUser){
                    mp.seekTo(progress * 1000);
                }
            }
        });

        MainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(mp != null){
                    int mCurrentPosition = mp.getCurrentPosition() / 1000;
                    Log.d("testing",""+mCurrentPosition);
                    seekBar.setProgress(mCurrentPosition);
                    if(mCurrentPosition==seekBar.getMax())
                    {
                        relativeLayout.setVisibility(View.GONE);
                    }
                }
                myHandler.postDelayed(this, 1000);
            }
        });


    }

    @Override
    public void setUpData(ArrayList<Episode> episodeArrayList) {

        Log.d("Testing 123",""+episodeArrayList.get(0).toString());
        episodes=episodeArrayList;
        if(status==0) {
            listViewGenerator();
        }

    }

    public void playMusic(Episode episode)
    {
        if(mp!=null&&mp.isPlaying())
        {
            mp.stop();
            mp=null;
            imageButton.setImageResource(R.drawable.pausebutton);
        }
        else
        {
            imageButton.setImageResource(R.drawable.pausebutton);
            relativeLayout.setVisibility(View.VISIBLE);
        }

        mp=new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mp.setDataSource(episode.getMediaURL());
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Duration",""+mp.getDuration());
        seekBar.setProgress(0);
        seekBar.setMax(mp.getDuration()/1000);
        mp.start();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_itemRefresh:
                if(mp!=null)
                {
                    mp.stop();
                    relativeLayout.setVisibility(View.GONE);
                }
                if(status==0)
                {
                    progressDialog.show();
                    Log.d("Clicked for Grid","True");
                    gridViewGenerator();
                    status=1;
                }
                else if(status==1)
                {
                    progressDialog.show();
                    Log.d("Clicked for List","True");
                    listViewGenerator();
                    status=0;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void listViewGenerator()
    {
        mAdapter = new EpisodeAdapter(episodes, MainActivity.this, new EpisodeAdapter.MyAdapterListener() {
            @Override
            public void playClick(View v, int position) {
                Log.d(" Testing ", episodes.get(position).toString());
                Toast.makeText(getApplicationContext(),"Media Playing: "+episodes.get(position).getTitle(),Toast.LENGTH_SHORT).show();
                playMusic(episodes.get(position));
            }

            @Override
            public void recyclerViewListClicked(View v, int position) {
                if(mp!=null && mp.isPlaying())
                {
                    mp.stop();
                }
                Episode episodeVar=episodes.get(position);
                Log.d("Testing position",episodes.get(position).toString());
                Intent intent=new Intent(MainActivity.this,PlayActivity.class);
                intent.putExtra(DATA_PASSED,episodeVar);
                startActivity(intent);

            }
        });
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        progressDialog.dismiss();
    }

    public void gridViewGenerator()
    {
        gridAdapter = new GridAdapter(episodes, MainActivity.this, new GridAdapter.GridAdapterListener() {
            @Override
            public void playClickGrid(View v, int position) {
                Log.d(" Testing ", episodes.get(position).toString());
                Toast.makeText(getApplicationContext(),"Media Playing: "+episodes.get(position).getTitle(),Toast.LENGTH_SHORT).show();
                playMusic(episodes.get(position));
            }

            @Override
            public void recyclerViewGridClicked(View v, int position) {

                if(mp!=null && mp.isPlaying())
                {
                    mp.stop();
                }

                Episode episodeVar=episodes.get(position);
                Log.d("Testing position",episodes.get(position).toString());
                Intent intent=new Intent(MainActivity.this,PlayActivity.class);
                intent.putExtra(DATA_PASSED,episodeVar);
                startActivity(intent);

            }

        });
        mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(gridAdapter);
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mp!=null &&mp.isPlaying())
        {
            mp.stop();
        }
        finish();
    }

}
