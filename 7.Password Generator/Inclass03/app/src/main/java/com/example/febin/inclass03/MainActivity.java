package com.example.febin.inclass03;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    SeekBar seekBarCountThread;
    SeekBar seekBarLengthThread;
    SeekBar seekBarCountAsync;
    SeekBar seekBarLengthAsync;

    TextView textViewCountThread;
    TextView textViewLengthThread;
    TextView textViewCountAsync;
    TextView textViewLengthAsync;

    final static int TASK_START=101;
    final static int TASK_STEP=102;
    final static int TASK_STOP=103;

    int length;
    int count=0;

    Button buttonGenerate;
    ProgressDialog progressDialog;

    Handler handler;

    ArrayList<String> threadList;
    ArrayList<String> asyncList;

    ExecutorService executorService= Executors.newFixedThreadPool(2);

    static final String DATA_CODE="DATA";
    static final String DATA_BUNDLE="DATA BUNDLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Password Generator");

        seekBarCountThread=(SeekBar)findViewById(R.id.seekBarCountThread);
        seekBarLengthThread=(SeekBar)findViewById(R.id.seekBarLengthTHread);
        seekBarCountAsync=(SeekBar)findViewById(R.id.seekBarAsyncCount);
        seekBarLengthAsync=(SeekBar)findViewById(R.id.seekBarAsyncLength);

        buttonGenerate=(Button)findViewById(R.id.buttonGenerate);

        textViewCountThread=(TextView)findViewById(R.id.textViewDisplayCountThread);
        textViewCountThread.setText(""+1);
        textViewLengthThread=(TextView)findViewById(R.id.textViewLengthDisplayThread);
        textViewLengthThread.setText(""+7);
        textViewCountAsync=(TextView)findViewById(R.id.textViewDispCountAsync);
        textViewCountAsync.setText(""+1);
        textViewLengthAsync=(TextView)findViewById(R.id.textViewDispLegthAsync);
        textViewLengthAsync.setText(""+7);
        ;
        seekBarCountThread.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress=progress+1;

                textViewCountThread.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarLengthThread.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress=progress+7;
                textViewLengthThread.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarCountAsync.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress=progress+1;
                textViewCountAsync.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarLengthAsync.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress=progress+7;
                textViewLengthAsync.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                if(msg.what==TASK_START)
                {
                    Log.d("Handler_Start",""+count);
                }
                else if(msg.what==TASK_STEP)
                {
                    int progress=msg.getData().getInt(DATA_BUNDLE);
                    count=count+1;
                    int result=count*100;
                    progressDialog.setProgress(count);
                }
                else if(msg.what==TASK_STOP)
                {
                    Log.d("Count Value Thread",""+count+":"+length);
                    threadList=msg.getData().getStringArrayList(DATA_BUNDLE);
                    if(count==length) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(MainActivity.this, GeneratedActivity.class);
                        intent.putStringArrayListExtra(DATA_CODE, asyncList);
                        intent.putStringArrayListExtra("Threaded Data", threadList);
                        startActivity(intent);
                    }
                }

                return false;
            }
        });


        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ///For Async Task
                count=0;
                int countAsync=Integer.parseInt(textViewCountAsync.getText().toString());
                int lengthAsync=Integer.parseInt(textViewLengthAsync.getText().toString());

                int countThread=Integer.parseInt(textViewCountThread.getText().toString());
                int lengthThread=Integer.parseInt(textViewLengthThread.getText().toString());

                length=countAsync+countThread;

                progressDialog=new ProgressDialog(MainActivity.this);
                progressDialog.setProgress(0);
                progressDialog.setMessage("Generating Passwords");
                progressDialog.setCancelable(false);
                progressDialog.setMax(length);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();

                new PasswordGenerator().execute(countAsync,lengthAsync);
                executorService.execute(new DoWork(lengthThread,countThread));



            }
        });





    }

    class  PasswordGenerator extends AsyncTask<Integer,Integer,ArrayList<String>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);
            Log.d("Checking here12345",""+s.size());
            asyncList=s;
            Log.d("Count VAlue Async",""+count+":"+s.size());
            if(count==length) {
                progressDialog.dismiss();
                Intent intent = new Intent(MainActivity.this, GeneratedActivity.class);
                intent.putStringArrayListExtra(DATA_CODE, s);
                intent.putStringArrayListExtra("Threaded Data",threadList);
                startActivity(intent);
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int progress=values[0];
            count=count+1;
            int result=count*100;
            progressDialog.setProgress(count);

        }

        @Override
        protected ArrayList doInBackground(Integer... params) {

            ArrayList<String> passwords=new ArrayList<>();
            int count=params[0];
            int length=params[1];

            for(int i=0;i<count;i++)
            {
                String password=Util.getPassword(length);
                Log.d("Password Created Async", "Pass added: " + password);
                passwords.add(password);
                publishProgress(i+1);
            }

            return passwords;
        }
    }



    class DoWork implements Runnable {
        int passwordLength;
        int passwordCount;
        String password;
        ArrayList<String> threadPasswords=new ArrayList<>();
        public DoWork(int passwordLength,int passwordCount) {
            this.passwordLength = passwordLength;
            this.passwordCount= passwordCount;
        }

        @Override
        public void run() {
            Message message=new Message();
            message.what=TASK_START;
            handler.sendMessage(message);

            for(int i=0;i<passwordCount;i++) {
                String pass = Util.getPassword(passwordLength);
                Log.d("Password Created Thread", "Pass added: " + pass);
                threadPasswords.add(pass);
                Message msgOne=new Message();
                msgOne.what=TASK_STEP;
                Bundle bundle=new Bundle();
                bundle.putInt(DATA_BUNDLE,(i+1));
                msgOne.setData(bundle);
                handler.sendMessage(msgOne);

            }

            Message msg=new Message();
            msg.what=TASK_STOP;
            Bundle bundle=new Bundle();
            bundle.putStringArrayList(DATA_BUNDLE,threadPasswords);
            msg.setData(bundle);
            handler.sendMessage(msg);

        }
    }


}
