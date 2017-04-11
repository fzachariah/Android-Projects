package com.example.febin.cloudtest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FetchData.IData {

    ArrayList<AppInfo> listFinal=new ArrayList<>();
    final String url="https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json";
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView textView;

    private MainAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager ;

    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef=mRootRef.child("favorites");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("iTunes Top Paid App");
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        textView =(TextView)findViewById(R.id.textViewMessage);
        new FetchData(MainActivity.this).execute(url,"1");
    }

    @Override
    public void setUpData(String data, String identifier) {

        ArrayList<AppInfo> list=AppUtil.parser(data);
        listFinal=list;
        mAdapter = new MainAdapter(listFinal, MainActivity.this, new MainAdapter.MyAdapterListener() {
        });
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public Context getContext() {
        return null;
    }
}
