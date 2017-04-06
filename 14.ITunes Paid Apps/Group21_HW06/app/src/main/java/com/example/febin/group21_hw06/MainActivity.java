package com.example.febin.group21_hw06;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements FetchAppInfo.IData {

    final String url="https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json";
    ListView listView;
    ArrayList<AppInfo> listFinal;

    final String DATA="Data";
    ProgressBar progressBar;
    TextView textView;
    SharedPreferences favorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("iTunes Top Paid App");
        listView=(ListView)findViewById(R.id.listView);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        textView =(TextView)findViewById(R.id.textViewMessage);
        favorites = getSharedPreferences("myFavorites",
                Context.MODE_PRIVATE);
        new FetchAppInfo(MainActivity.this).execute(url);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemRefresh:
                progressBar.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                new FetchAppInfo(MainActivity.this).execute(url);
                return true;
            case R.id.itemFavorites:
                refresh();
                return true;

            case R.id.itemIncrease:

                ArrayList<AppInfo> appInfoArrayList=listFinal;
                        Collections.sort(appInfoArrayList);
                AppAdapter appAdapter=new AppAdapter(this,R.layout.row_linearlayout,appInfoArrayList);
                listView.setAdapter(appAdapter);

                return true;

            case R.id.itemDecrease:
                ArrayList<AppInfo> appInfoArrayListDec=listFinal;
                Collections.sort(appInfoArrayListDec);
                Collections.reverse(appInfoArrayListDec);
                AppAdapter appAdapterDec=new AppAdapter(this,R.layout.row_linearlayout,appInfoArrayListDec);
                listView.setAdapter(appAdapterDec);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void refresh()
    {
        ArrayList<AppInfo> appInfoArrayList=new ArrayList<>();
        for(int i=0;i<listFinal.size();i++)
        {
            AppInfo appInfo=listFinal.get(i);
            if(favorites.contains(appInfo.getAppName()))
            {
                String devName = favorites.getString(appInfo.getAppName(),"");
                if(devName.equals(appInfo.getDevName()))
                {
                    appInfoArrayList.add(appInfo);
                }
            }
        }
        Intent intent=new Intent(MainActivity.this,FavoriteList.class);
        intent.putExtra(DATA,appInfoArrayList);
        startActivity(intent);
    }

    @Override
    public void setUpData(ArrayList<AppInfo> appInfoArrayList) {

        Log.d("ArrayList Size",""+appInfoArrayList.size());
        listFinal=appInfoArrayList;
        AppAdapter appAdapter=new AppAdapter(this,R.layout.row_linearlayout,listFinal);
        listView.setAdapter(appAdapter);
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);

    }


    @Override
    public void onResume()
    {
        super.onResume();
        new FetchAppInfo(MainActivity.this).execute(url);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
