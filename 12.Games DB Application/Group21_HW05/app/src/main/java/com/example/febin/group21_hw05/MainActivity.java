package com.example.febin.group21_hw05;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FetchGameListAsync.IData,FetchGameDetailsAsync.IDataDetails {

    LinearLayout linearLayout;
    Button buttonSearch;
    Button buttonGo;
    RadioGroup radioGroup;
    EditText editTextKey;
    ProgressDialog progressDialog;

    String DATA_KEY="DATA OBJECT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("The Games DB");

        linearLayout =(LinearLayout)findViewById(R.id.linearLayout);
        buttonSearch=(Button)findViewById(R.id.button);
        buttonGo=(Button)findViewById(R.id.buttonGo);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        editTextKey=(EditText)findViewById(R.id.editTextSearch);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                buttonGo.setEnabled(true);
            }
        });

    }
    public void clickSearch(View view)
    {
        String key=editTextKey.getText().toString();
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
            radioGroup.removeAllViews();
            new FetchGameListAsync(MainActivity.this).execute(key);
        }
    }

    public void clickGo(View view)
    {
        int idOne=radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton=(RadioButton)findViewById(idOne);
        String tag=(String)radioButton.getTag();
        Log.d("here:",tag);
        progressDialog=ProgressDialog.show(this,null,null);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C8C8C8")));
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.loader1);
        progressDialog.getWindow().setLayout(620,450);
        new FetchGameDetailsAsync(MainActivity.this).execute(tag);
    }

    @Override
    public void setUpData(ArrayList<Game> gameArrayList) {

        Log.d("!!!!!12345",""+gameArrayList.size());
        if(gameArrayList.size()==0)
        {
            Toast.makeText(getApplicationContext(),"No Data to Display/Try Again",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }else {
            for (int i = 0; i < gameArrayList.size(); i++) {
                Game game = gameArrayList.get(i);
                StringBuilder value = new StringBuilder();
                if (game.getTitle().length() > 0) {
                    value.append(game.getTitle());

                }
                if (game.getReleaseDate().length() > 0) {
                    String temp = game.getReleaseDate();
                    temp = temp.substring(temp.lastIndexOf("/") + 1);
                    value.append(" Released in " + temp + ".");

                }
                if (game.getPlatform().length() > 0) {
                    value.append("Platform: " + game.getPlatform());
                }
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(value.toString().trim());
                radioButton.setTag(game.getId());
                radioButton.setBackgroundResource(R.drawable.border);
                ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                radioButton.setLayoutParams(layoutParams);
                radioGroup.addView(radioButton);

            }

            progressDialog.dismiss();
        }


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

    @Override
    public Context getContext() {
        return this;
    }
}
