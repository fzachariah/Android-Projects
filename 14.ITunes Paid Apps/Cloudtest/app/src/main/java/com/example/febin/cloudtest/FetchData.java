package com.example.febin.cloudtest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by febin on 04/04/2017.
 */

public class FetchData extends AsyncTask<String,Void,String> {

    IData activity;
    String identifier;

    public FetchData(IData activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        String val="";
        identifier=params[1];
        OkHttpClient okHttpClient=new OkHttpClient();

        Request request = new Request.Builder()
                .url(params[0])
                .build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            val=response.body().string();
            Log.d("testing here",""+val);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return val ;
    }

    @Override
    protected void onPostExecute(String data) {
        super.onPostExecute(data);
        Log.d("Log:data",data);
        activity.setUpData(data,identifier);

    }
    static public interface IData
    {
        public void setUpData(String data, String identifier);
        public Context getContext();
    }
}
