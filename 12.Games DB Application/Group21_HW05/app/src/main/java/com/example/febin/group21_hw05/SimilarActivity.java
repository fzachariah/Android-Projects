package com.example.febin.group21_hw05;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class SimilarActivity extends AppCompatActivity {

    final String SIMILAR_DATA="Similar Data";
    final String SIMILAR_TITLE="Similar Title";

    LinearLayout linearLayout;
    TextView textViewHeading;
    Button buttonFinish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar);

        setTitle("Similar Games");

        linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
        textViewHeading=(TextView)findViewById(R.id.textViewHeading);
        buttonFinish=(Button)findViewById(R.id.buttonSimFinish);
        String title=getIntent().getStringExtra(SIMILAR_TITLE);
        textViewHeading.setText("Similar Games to "+title);


        ArrayList<Game> gameArrayList=(ArrayList<Game>)getIntent().getSerializableExtra(SIMILAR_DATA);


        for (int i = 0; i < gameArrayList.size(); i++) {
            Game game = gameArrayList.get(i);
            if(game!=null) {
                StringBuilder value = new StringBuilder();
                if (game.getTitle().length() > 0) {
                    value.append(game.getTitle());

                }
                if(game.getReleaseDate()!=null) {
                    if (game.getReleaseDate().length() > 0) {
                        String temp = game.getReleaseDate();
                        temp = temp.substring(temp.lastIndexOf("/") + 1);
                        value.append(" Released in " + temp + ".");

                    }
                }
                if (game.getPlatform().length() > 0) {
                    value.append("Platform: " + game.getPlatform());
                }
                TextView textView = new TextView(this);
                textView.setText(value.toString().trim());
                textView.setTag(game.getId());
                textView.setBackgroundResource(R.drawable.border);
                textView.setWidth(700);
                linearLayout.addView(textView);
            }

        }

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
