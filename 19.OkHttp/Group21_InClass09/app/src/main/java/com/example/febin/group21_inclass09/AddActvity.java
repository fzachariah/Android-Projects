package com.example.febin.group21_inclass09;

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

public class AddActvity extends AppCompatActivity {

    SharedPreferences appDetails;
    HashSet<String> subscribedData;

    RecyclerView recyclerView;
    private MainAdapter mAdapter;

    ArrayList<Channel> mainList=new ArrayList<>();

    RecyclerView.LayoutManager mLayoutManager ;

    Button buttonDone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_actvity);
        subscribedData=(HashSet<String>)getIntent().getSerializableExtra("mySet");
        setTitle("Chit Chat");
        recyclerView=(RecyclerView)findViewById(R.id.recyclerViewShow);
        appDetails = getSharedPreferences("userSession",
                Context.MODE_PRIVATE);
        buttonDone=(Button)findViewById(R.id.buttonDone);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddActvity.this,MainScreen.class);
                startActivity(intent);
            }
        });
        getData();
    }

    public void getData()
    {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://52.90.79.130:8080/Groups/api/get/channels")
                .addHeader("Authorization", "BEARER "+appDetails.getString("token",null))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    try{
                        String val=response.body().string();
                        JSONObject jSONObject =new JSONObject(val);
                        String status=jSONObject.getString("status");
                        if(status.equals("1")) {
                            JSONArray jsonArray = jSONObject.getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                Channel channel=new Channel();
                                JSONObject jsonQuestion=jsonArray.getJSONObject(i);
                                int id=jsonQuestion.getInt("channel_id");
                                String name=jsonQuestion.getString("channel_name");
                                channel.setId(id);
                                channel.setName(name);
                                if(!subscribedData.contains(name))
                                {
                                    channel.setStatus(1);
                                }
                                else
                                {
                                    channel.setStatus(0);
                                }
                                mainList.add(channel);

                            }

                        }

                    }catch (JSONException e)
                    {

                    }


            }
        });
        mAdapter = new MainAdapter(mainList, getApplicationContext(), new MainAdapter.MyAdapterListener() {
            @Override
            public void blockItem(View v, int position,int status) {
                if(status==0)
                {
                    Intent intent=new Intent(AddActvity.this,ViewActivity.class);
                    intent.putExtra("Data",mainList.get(position));
                    startActivity(intent);
                }
                else {
                    Log.d(" Testing ", mainList.get(position).toString());
                    OkHttpClient client = new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder()
                            .add("channel_id", "" + mainList.get(position).getId())
                            .build();

                    Request request = new Request.Builder()
                            .url("http://52.90.79.130:8080/Groups/api/subscribe/channel")
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .addHeader("Authorization", "BEARER " + appDetails.getString("token", null))
                            .post(formBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            Log.d("testing here chck", "" + response.body().string());

                        }
                    });
                }



            }

            @Override
            public void recyclerViewListClicked(View v, int position) {

                //Log.d("Testing position",arrayList.get(position).toString());
                //mListener.ClickFromUnBlocked(arrayList.get(position));


            }
        });

        mLayoutManager = new LinearLayoutManager(AddActvity.this);
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
                Intent intent=new Intent(AddActvity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();



                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
