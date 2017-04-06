package com.example.febin.inclass05_zachariah;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by febin on 13/02/2017.
 */

public class FetchXMLAsync extends AsyncTask<String,Void,ArrayList<News>> {
    final String URL="http://rss.cnn.com/rss/cnn_tech.rss";


    IData activity;

    public FetchXMLAsync(IData activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<News> doInBackground(String... params) {

        RequestParams requestParams=new RequestParams("GET",URL);
        ArrayList<News> newsArrayList=new ArrayList<>();
        try {
            HttpURLConnection httpURLConnection=requestParams.setUpConnection();
            httpURLConnection.connect();

            int status_code = httpURLConnection.getResponseCode();
            Log.d("^^^^11111status",""+status_code);
            if(status_code==HttpURLConnection.HTTP_OK)
            {
                InputStream inputStream=httpURLConnection.getInputStream();
                newsArrayList=XMLUtil.PullParser.parsePerson(inputStream);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return  newsArrayList;

    }

    @Override
    protected void onPostExecute(ArrayList<News> newsArrayList) {
        super.onPostExecute(newsArrayList);
        activity.setUpData(newsArrayList);
    }

    static public interface IData
    {
        public void setUpData(ArrayList<News> newsArrayList);
        public Context getContext();
    }

}
