package com.example.febin.group21_inclass09;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainScreen extends AppCompatActivity {

    SharedPreferences appDetails;
    HashSet<String> subscribedData=new HashSet<>();
    ArrayList<Channel> subScribedChannels=new ArrayList<>();

    RecyclerView recyclerView;
    private MainAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager ;

    Button buttonAddMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        setTitle("Chit Chat");
        recyclerView=(RecyclerView)findViewById(R.id.recyclerViewMainScreen);
        appDetails = getSharedPreferences("userSession",
                Context.MODE_PRIVATE);

        getData();


        buttonAddMore=(Button)findViewById(R.id.buttonAddMore);
        buttonAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainScreen.this,AddActvity.class);
                intent.putExtra("mySet", subscribedData);;
                startActivity(intent);
            }
        });




    }

    public void getData()

    {
        Log.d("Inside","Test123");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://52.90.79.130:8080/Groups/api/get/subscriptions")
                .addHeader("Authorization", "BEARER "+appDetails.getString("token",null))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                try{
                    Log.d("Test123","febin");
                    String val=response.body().string();
                    JSONObject jSONObject =new JSONObject(val);
                    String status=jSONObject.getString("status");
                    if(status.equals("1")) {

                        JSONArray jsonArray = jSONObject.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            Channel channel=new Channel();
                            JSONObject jsonQuestion=jsonArray.getJSONObject(i);
                            JSONObject jsonObject1=jsonQuestion.getJSONObject("channel");
                            int id=jsonObject1.getInt("channel_id");
                            String name=jsonObject1.getString("channel_name");
                            channel.setId(id);
                            channel.setName(name);
                            subscribedData.add(name);
                            channel.setStatus(0);
                            subScribedChannels.add(channel);

                        }

                    }

                }catch (JSONException e)
                {

                }


            }
        });

        mAdapter = new MainAdapter(subScribedChannels, getApplicationContext(), new MainAdapter.MyAdapterListener() {
            @Override
            public void blockItem(View v, int position,int status) {
                Log.d(" Testing ", subScribedChannels.get(position).toString());
                if(status==0)
                {
                    Intent intent=new Intent(MainScreen.this,ViewActivity.class);
                    intent.putExtra("Data",subScribedChannels.get(position));
                    startActivity(intent);
                }




            }

            @Override
            public void recyclerViewListClicked(View v, int position) {




            }
        });

        mLayoutManager = new LinearLayoutManager(MainScreen.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menusingle, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:

                SharedPreferences.Editor editor = appDetails.edit();
                editor.remove("email");
                editor.remove("password");
                editor.remove("token");
                editor.commit();
                Toast.makeText(getApplicationContext(),"Logout Successful",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainScreen.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();



                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
