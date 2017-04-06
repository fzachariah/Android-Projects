package com.example.febin.inclass03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GeneratedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated);
        setTitle("Generated Passwords ");
        ArrayList<String> listThread=getIntent().getStringArrayListExtra("Threaded Data");
        ArrayList<String> listAsyc=getIntent().getStringArrayListExtra("DATA");
        Log.d("Checking value",listAsyc.size()+":"+listAsyc.size());
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout linearLayoutThread=(LinearLayout)findViewById(R.id.linearLayoutThread);
        LinearLayout linearLayoutAsync=(LinearLayout)findViewById(R.id.linearLayoutAsync);

        for(int i=0;i<listThread.size();i++)
        {
            TextView textView=new TextView(this);
            textView.setText(""+listThread.get(i));
            textView.setGravity(Gravity.CENTER );
            textView.setTextSize(20);
            linearLayoutThread.addView(textView);

        }
        for(int j=0;j<listAsyc.size();j++)
        {
            TextView textView=new TextView(this);
            textView.setTextSize(20);
            textView.setGravity(Gravity.CENTER );
            textView.setText(""+listAsyc.get(j));
            linearLayoutAsync.addView(textView);
        }






    }
}
