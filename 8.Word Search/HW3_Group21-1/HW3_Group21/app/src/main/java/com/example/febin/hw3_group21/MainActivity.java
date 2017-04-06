package com.example.febin.hw3_group21;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout linearLayoutParent;
    EditText editTextInput;
    ImageButton imageButton;
    LinearLayout linearLayoutHorizontal;
    ProgressDialog progressDialog;

    int count=1;
    final static int TOTAL_ELEMENTS=20;

    int totalItems;
    int startItems;
    LinkedHashMap<String,Integer> hashMap;

    String DATA="DATA_PASSED";
    String COUNT_VALUE="COUNT_PASSED";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Word Counter");
        linearLayoutParent=(LinearLayout)findViewById(R.id.linearLayoutParent);
        createRow();
    }


    @Override
        public void onClick(View v) {
        ImageButton imageButtonClick=(ImageButton)v;
        int option=Integer.parseInt(imageButtonClick.getTag().toString());
        if(option==0&&count<=TOTAL_ELEMENTS) {
            imageButtonClick.setImageResource(R.drawable.sub_button);
            imageButtonClick.setTag(""+1);
            createRow();
        }
        else if(option==1)
        {
            LinearLayout linearLayout=(LinearLayout)imageButtonClick.getParent();
            linearLayoutParent.removeView(linearLayout);
            count--;

        }else if(option==0&&count>TOTAL_ELEMENTS)
        {
            Toast.makeText(getApplicationContext(),"Only 20 Search elements are possible",Toast.LENGTH_SHORT).show();
        }


    }
    public void createRow()
    {
        linearLayoutHorizontal =new LinearLayout(this);
        linearLayoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        editTextInput=new EditText(this);
        editTextInput.setEms(10);
        ViewGroup.LayoutParams linearParams = new ViewGroup.LayoutParams(600,ViewGroup.LayoutParams.MATCH_PARENT);
        editTextInput.setLayoutParams(linearParams);
        imageButton=new ImageButton(this);
        imageButton.setTag(""+count);
        imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageButton.setAdjustViewBounds(true);
        imageButton.setImageResource(R.drawable.plus_button);
        imageButton.setOnClickListener(this);
        imageButton.setBackgroundResource(0);
        imageButton.setTag(""+0);
        ViewGroup.LayoutParams imageParams=new ViewGroup.LayoutParams(130,130);
        imageButton.setLayoutParams(imageParams);
        linearLayoutHorizontal.addView(editTextInput);
        linearLayoutHorizontal.addView(imageButton);
        linearLayoutParent.addView(linearLayoutHorizontal);
        count++;
    }

    public void searchClick(View view)
    {
        int elementCount=linearLayoutParent.getChildCount();
        Log.d("number of editTexts",""+ elementCount);
        hashMap=new LinkedHashMap<>();
        ArrayList<String> strings=new ArrayList<>();
        EditText editTextTest=null;
        CheckBox checkBox=(CheckBox)findViewById(R.id.checkBox);
        String checked=""+checkBox.isChecked();
        Log.d("CheckBox",checked);
        boolean check=true;
        LinearLayout linearLayoutTest;
        for(int i=0;i<elementCount;i++)
        {
            linearLayoutTest=(LinearLayout)linearLayoutParent.getChildAt(i);
            editTextTest=(EditText)linearLayoutTest.getChildAt(0);
            String input=editTextTest.getText().toString();
            strings.add(input);
            if(input.length()==0)
            {
                check=false;
            }
        }
        if(check)
        {

            totalItems=strings.size();
            progressDialog=new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog=ProgressDialog.show(this,null,null);
            progressDialog.setContentView(new ProgressBar(this));
            startItems=0;

            for(int i=0;i<strings.size();i++)
            {
                Log.d("Word:",strings.get(i));
                //new WordFinder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,strings.get(i),checked);
                new WordFinder().execute(strings.get(i),checked);
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please enter values correctly",Toast.LENGTH_SHORT).show();
        }
    }

    class WordFinder extends AsyncTask<String,Void,LinkedHashMap<String,Integer>>
    {
        @Override
        protected LinkedHashMap<String, Integer> doInBackground(String... params) {


            int count=0;
            Log.d("Found","here");
            String searchWord=params[0];
            String caseCheck=params[1];
            Log.d("2222222222value",caseCheck+count);
            BufferedReader reader=null;
            try {
                reader = new BufferedReader(new InputStreamReader(getAssets().open("textfile.txt")));
                String lineContent = null;
                // Loop will iterate over each line within the file.
                // It will stop when no new lines are found.
                while ((lineContent = reader.readLine()) != null) {
                    if (lineContent.toLowerCase().contains(searchWord.toLowerCase())) {
                        lineContent = lineContent.replaceAll("[-+;=).(^:,\"?!]", " ");

                        String[] resultingTokens = lineContent.split(" ");
                        for (int i = 0; i < resultingTokens.length; i++) {
                            String result = resultingTokens[i];
                            if (caseCheck.equals("true")) {
                                if (result.equals(searchWord)) {
                                    Log.d("matching word", result);
                                    count++;
                                }
                            } else if (caseCheck.equals("false")) {
                                result = result.toLowerCase();
                                String temp = searchWord.toLowerCase();
                                if (result.equals(temp)) {
                                    count++;
                                    Log.d("matching wordtwo", result);
                                }
                            }

                        }
                    }
                }



                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d("Task","Inside"+count);
            hashMap.put(searchWord,count);

            return hashMap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Pre Execute","Inside");
        }

        @Override
        protected void onPostExecute(LinkedHashMap<String,Integer> hashMap) {
            startItems++;
            Log.d("22222Values: ",""+startItems+":"+totalItems);
            if(startItems==totalItems)
            {
                ArrayList<String> keyElem=new ArrayList<>();
                ArrayList<Integer> valueElem=new ArrayList<>();
                for(String key: hashMap.keySet())
                {
                    Log.d("Priiting",""+key+":"+hashMap.get(key));
                    keyElem.add(key);
                    valueElem.add(hashMap.get(key));
                }
                Intent intent=new Intent(MainActivity.this,ResultActivity.class);
                intent.putStringArrayListExtra(DATA,keyElem);
                intent.putIntegerArrayListExtra(COUNT_VALUE,valueElem);
                progressDialog.dismiss();
                startActivity(intent);
            }
        }


    }

}
