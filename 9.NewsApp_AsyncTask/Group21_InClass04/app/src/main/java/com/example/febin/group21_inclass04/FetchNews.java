package com.example.febin.group21_inclass04;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by febin on 06/02/2017.
 */

class FetchNews extends AsyncTask<String,Void,ArrayList<News>>
{
    IData activity;

    public FetchNews(IData activity) {
        this.activity = activity;
    }

    final String KEY="b3987b48abf94dce8307f914edbe3f49";
    @Override
    protected ArrayList<News> doInBackground(String... params) {

        RequestParams requestParams=new RequestParams("GET","https://newsapi.org/v1/articles");
        requestParams.addParam("apiKey",KEY);
        requestParams.addParam("source",params[0]);
        StringBuilder stringBuilder=new StringBuilder("");
        try {
            HttpURLConnection httpURLConnection=requestParams.setUpConnection();
            httpURLConnection.connect();

            int status_code = httpURLConnection.getResponseCode();
            Log.d("22222222status",""+status_code);
            if (status_code == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                stringBuilder = new StringBuilder("");
                String line = "";
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("check here",stringBuilder.toString());
        ArrayList<News> list=NewsUtil.parser(stringBuilder.toString());
        Log.d("5555555555555555Final",""+list.size());
        activity.setUpData(list);
        return null;
    }
    static public interface IData
    {
        public void setUpData(ArrayList<News> newses);
        public Context getContext();
    }
}
