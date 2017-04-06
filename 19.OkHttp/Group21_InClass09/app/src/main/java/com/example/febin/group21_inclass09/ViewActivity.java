package com.example.febin.group21_inclass09;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ViewActivity extends AppCompatActivity {


    Button buttonSend;
    LinearLayout linearLayout;
    EditText editTextMessage;
    SharedPreferences appDetails;
    Channel channel;

    ArrayList<Message> messages=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        setTitle("ChitChat");
        appDetails = getSharedPreferences("userSession",
                Context.MODE_PRIVATE);
        channel=(Channel)getIntent().getExtras().getSerializable("Data");
        buttonSend=(Button)findViewById(R.id.buttonSend);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayoutMessage1);
        editTextMessage=(EditText)findViewById(R.id.editTextMessage);
        displayData();
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message=editTextMessage.getText().toString();
                if(message.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Enter Message",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    sendData(message);
                }

            }
        });
    }

    private void displayData() {


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://52.90.79.130:8080/Groups/api/get/messages?channel_id="+channel.getId())
                .header("Authorization","BEARER " + appDetails.getString("token", null))
                .build();
        messages=new ArrayList<>();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {

               // Log.d("testing here check", "" + response.body().string());
                String val=response.body().string().toString();
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(val);
                    String status=jsonObject.getString("status");
                    if(status.equals("1"))
                    {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Log.d("Test 123",""+jsonArray.length()+" "+messages.size());
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            Message message=new Message();
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            int id=jsonObject1.getInt("message_id");
                            message.setMessageId(id);
                            String messageTime=jsonObject1.getString("msg_time");
                            message.setMsgTime(messageTime);
                            String messageVal=jsonObject1.getString("messages_text");
                            message.setMessage(messageVal);
                            JSONObject userDetails=jsonObject1.getJSONObject("user");
                            String email=userDetails.getString("email");
                            message.setEmail(email);
                            String fName=userDetails.getString("fname");
                            message.setfName(fName);
                            String lName=userDetails.getString("lname");
                            message.setlName(lName);

                            messages.add(message);




                        }

                    }
                    else
                    {


                    }
                    alignData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }




        });
    }






    public void sendData(String message)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        PrettyTime p = new PrettyTime();
        //System.out.println(p.format(new Date()));


        Log.d("Data",""+(System.currentTimeMillis() + (1000*60*10)));

        RequestBody formBody = new FormBody.Builder()
                .add("msg_text", message)
                .add("msg_time",""+(System.currentTimeMillis() + (1000*60*10)))
                .add("channel_id",""+channel.getId())
                .build();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://52.90.79.130:8080/Groups/api/post/message")
                .header("Authorization","BEARER " + appDetails.getString("token", null))
                .header("Content-Type","application/x-www-form-urlencoded")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {

                 //Log.d("testing here chck",""+response.body().string());
                String val=response.body().string().toString();
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(val);
                    String status=jsonObject.getString("status");
                    if(status.equals("1"))
                    {

                        successFull();
                    }
                    else
                    {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }





            }


        });
    }
    private void successFull() {
        ViewActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ViewActivity.this, " Operation Successful",
                        Toast.LENGTH_LONG).show();
            }
        });


    }

    private void alignData()
    {

            ViewActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    linearLayout.removeAllViews();
                    Log.d("Test1234",""+messages.size());
                    for(int i=0;i<messages.size();i++)
                    {
                        LinearLayout layout2 = new LinearLayout(ViewActivity.this);
                         LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, ViewGroup.LayoutParams.WRAP_CONTENT);

                        //layout2.setLayoutParams(new LinearLayout.LayoutParams(500, ViewGroup.LayoutParams.WRAP_CONTENT));
                        layout2.setBackgroundResource(R.drawable.border);
                        layout2.setOrientation(LinearLayout.VERTICAL);


                        TextView tv1 = new TextView(ViewActivity.this);
                        TextView tv2 = new TextView(ViewActivity.this);
                        TextView tv3 = new TextView(ViewActivity.this);


                        tv1.setText(""+messages.get(i).getfName()+" "+messages.get(i).getlName());
                        tv2.setText(messages.get(i).getMessage());
                        PrettyTime p = new PrettyTime();
                        //System.out.println(p.format(new Date()));
                        //prints: “moments from now”

                        //System.out.println(p.format(new Date(System.currentTimeMillis() + 1000*60*10)));
                        tv3.setText("Posted "+ p.format(new Date(Long.parseLong(messages.get(i).getMsgTime()))));

                        layout2.addView(tv1);
                        layout2.addView(tv2);
                        layout2.addView(tv3);
                        if(messages.get(i).getEmail().equals(appDetails.getString("email",null)))
                        {
                            //layout2.setGravity(Gravity.RIGHT);
                            layoutParams.gravity=Gravity.RIGHT;


                        }
                        else
                        {
                            layoutParams.gravity=Gravity.LEFT;
                        }
                        layout2.setLayoutParams(layoutParams);
                        linearLayout.addView(layout2);
                        //layout2.addView(tv4);
                    }

                }
            });



        }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuboth, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:

                //messages=new ArrayList<>();
                messages.clear();
                displayData();

                return true;
            case R.id.logout:

                SharedPreferences.Editor editor = appDetails.edit();
                editor.remove("email");
                editor.remove("password");
                editor.remove("token");
                editor.commit();
                Toast.makeText(getApplicationContext(),"Logout Successful",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ViewActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();



                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
