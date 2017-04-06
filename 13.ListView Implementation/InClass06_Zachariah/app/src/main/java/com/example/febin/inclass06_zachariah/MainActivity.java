package com.example.febin.inclass06_zachariah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FetchGameListAsync.IData,FetchImageLink.ILinkDetails,FetchGameDetailsAsync.IDataDetails{

    EditText editTextSearch;
    Button buttonSearch;
    ProgressDialog progressDialog;
    ListView listView;

    ArrayList<Game> gamesList;

    String DATA_KEY="DATA OBJECT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("The Games DB");

        buttonSearch=(Button)findViewById(R.id.buttonSearch);
        editTextSearch=(EditText)findViewById(R.id.editTextSearch);
        listView =(ListView)findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                progressDialog=ProgressDialog.show(MainActivity.this,null,null);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C8C8C8")));
                progressDialog.setCancelable(false);
                progressDialog.setContentView(R.layout.loader1);
                progressDialog.getWindow().setLayout(620,450);
                new FetchGameDetailsAsync(MainActivity.this).execute(gamesList.get(position).getId());

            }
        });

    }

    public void clickSearch(View view)
    {
        String key=editTextSearch.getText().toString();
        if(key.length()==0)
        {
            Toast.makeText(getApplicationContext(),"Enter Game Name",Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog=ProgressDialog.show(this,null,null);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C8C8C8")));
            progressDialog.setCancelable(false);
            progressDialog.setContentView(R.layout.loader1);
            progressDialog.getWindow().setLayout(620,450);
            new FetchGameListAsync(MainActivity.this).execute(key);
        }
    }

    @Override
    public void setUpData(ArrayList<Game> gameArrayList) {

        Log.d("Checking the status",""+gameArrayList.size());
        gamesList=gameArrayList;
        if(gamesList.size()!=0) {
            new FetchImageLink(MainActivity.this).execute(gamesList);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Try Again",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }


    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setUpDataDetails(ArrayList<String> links) {
        Log.d("checking",""+links.size()+" "+gamesList.size());

        for(int i=0;i<gamesList.size();i++)
        {
            Log.d("Print Value",gamesList.get(i).getId()+" "+links.get(i));
            gamesList.get(i).setImageURL(links.get(i));
        }

        GameAdapter gameAdapter=new GameAdapter(this,R.layout.row_list,gamesList);
        listView.setAdapter(gameAdapter);
        progressDialog.dismiss();
    }

    @Override
    public void setUpDataDetails(SingleGame singleGame) {

        if(singleGame!=null) {
            Log.d("Printing value:", singleGame.toString());
            Intent intent = new Intent(MainActivity.this, GameDetails.class);
            intent.putExtra(DATA_KEY, singleGame);
            startActivity(intent);
            progressDialog.dismiss();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Connection Timeout,Please Try Again",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }
}
