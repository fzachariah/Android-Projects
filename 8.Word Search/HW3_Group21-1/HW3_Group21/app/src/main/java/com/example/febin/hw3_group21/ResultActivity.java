package com.example.febin.hw3_group21;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    String DATA="DATA_PASSED";
    String COUNT_VALUE="COUNT_PASSED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle("Words");

        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
        /*HashMap<String,Integer> hashMap=(HashMap<String,Integer>)getIntent().getSerializableExtra(DATA);*/
        ArrayList<String> words=getIntent().getStringArrayListExtra(DATA);
        ArrayList<Integer> countValues=getIntent().getIntegerArrayListExtra(COUNT_VALUE);



        for(int i=0;i<words.size();i++)
        {

                    TextView textView=new TextView(this);
                    textView.setText(""+words.get(i)+": "+countValues.get(i));
                    textView.setTextSize(20);
                    linearLayout.addView(textView);
        }


        findViewById(R.id.buttonFInish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ResultActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }
}
