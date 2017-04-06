package com.example.febin.connect3;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int activePlayer=0;
    int gameState[]={2,2,2,2,2,2,2,2,2};
    int [][]winningPositions={{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    boolean gameIsactive=true;
    public void dropIn(View view)
    {

        ImageView counter =(ImageView)view;
        int tappedCounter=Integer.parseInt(counter.getTag().toString());

        if(gameState[tappedCounter]==2&&gameIsactive) {
            gameState[tappedCounter]=activePlayer;
            counter.setTranslationY(-1000f);
            if (activePlayer == 0) {
                counter.setImageResource(R.drawable.yellow);
                activePlayer = 1;
            } else {
                counter.setImageResource(R.drawable.red);
                activePlayer = 0;
            }
            counter.animate().translationYBy(1000f).rotation(360).setDuration(400);
            for(int [] winningPosition:winningPositions)
            {
                if(gameState[winningPosition[0]]==gameState[winningPosition[1]]&&
                        gameState[winningPosition[1]]==gameState[winningPosition[2]]&&
                        gameState[winningPosition[0]]!=2)
                {
                    gameIsactive=false;
                    if(gameState[winningPosition[0]]==0)
                    {
                        LinearLayout layout=(LinearLayout)findViewById(R.id.playgainLayout);
                        TextView message=(TextView)findViewById(R.id.textView);
                        message.setText("Yellow Has Won");
                        layout.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),"Player One Has Won the Game",Toast.LENGTH_LONG).show();
                    }
                    else if(gameState[winningPosition[0]]==1)
                    {
                        LinearLayout layout=(LinearLayout)findViewById(R.id.playgainLayout);
                        TextView message=(TextView)findViewById(R.id.textView);
                        message.setText("Red Has Won");
                        layout.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),"Player Two Has Won the Game",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    boolean gameIsover=true;
                    for(int i=0;i<gameState.length;i++)
                    {
                        if(gameState[i]==2)
                        {
                            gameIsover=false;
                        }
                    }
                    if(gameIsover)
                    {
                        LinearLayout layout=(LinearLayout)findViewById(R.id.playgainLayout);
                        TextView message=(TextView)findViewById(R.id.textView);
                        message.setText("It is a draw");
                        layout.setVisibility(View.VISIBLE);

                    }
                }
            }
        }
    }
    public void playAgain(View view)
    {
        LinearLayout layout=(LinearLayout)findViewById(R.id.playgainLayout);
        layout.setVisibility(View.INVISIBLE);
        gameIsactive=true;
        activePlayer=0;
        for(int i=0;i<gameState.length;i++)
        {
            gameState[i]=2;
        }
        GridLayout gridLayout=(GridLayout)findViewById(R.id.gridLayout);
        for(int i=0;i<gridLayout.getChildCount();i++)
        {
            ((ImageView)gridLayout.getChildAt(i)).setImageResource(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
