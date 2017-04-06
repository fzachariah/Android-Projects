package com.example.febin.inclass06_zachariah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SimilarActivity extends AppCompatActivity implements  FetchGameDetailsAsync.IDataDetails{

    final String SIMILAR_DATA="Similar Data";
    final String SIMILAR_TITLE="Similar Title";
    ProgressDialog progressDialog;
    ListView listView;
    TextView textViewHeading;
    Button buttonFinish;
    String DATA_KEY="DATA OBJECT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar);

        setTitle("Similar Games");

        listView=(ListView) findViewById(R.id.listViewSimilar);
        textViewHeading=(TextView)findViewById(R.id.textViewHeading);
        buttonFinish=(Button)findViewById(R.id.buttonSimFinish);
        String title=getIntent().getStringExtra(SIMILAR_TITLE);
        textViewHeading.setText("Similar Games to "+title);


        final ArrayList<Game> gameArrayList=(ArrayList<Game>)getIntent().getSerializableExtra(SIMILAR_DATA);

        SimilarAdapter similarAdapter=new SimilarAdapter(this,R.layout.row_similar,gameArrayList);
        listView.setAdapter(similarAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                progressDialog= ProgressDialog.show(SimilarActivity.this,null,null);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C8C8C8")));
                progressDialog.setCancelable(false);
                progressDialog.setContentView(R.layout.loader1);
                progressDialog.getWindow().setLayout(620,450);
                new FetchGameDetailsAsync(SimilarActivity.this).execute(gameArrayList.get(position).getId());

            }
        });




        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void setUpDataDetails(SingleGame singleGame) {

        if(singleGame!=null) {
            Log.d("Printing value:", singleGame.toString());
            Intent intent = new Intent(SimilarActivity.this, SimilarDetails.class);
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
