package com.example.febin.group21_hw07;

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

public class FetchXMLAsync extends AsyncTask<Void,Void,ArrayList<Episode>> {
    final String URL="https://www.npr.org/rss/podcast.php?id=510298";


    IData activity;

    public FetchXMLAsync(IData activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<Episode> doInBackground(Void... params) {

        RequestParams requestParams=new RequestParams("GET",URL);
        ArrayList<Episode> episodeArrayList=new ArrayList<>();
        try {
            HttpURLConnection httpURLConnection=requestParams.setUpConnection();
            httpURLConnection.connect();

            int status_code = httpURLConnection.getResponseCode();
            Log.d("^^^^11111status",""+status_code);
            if(status_code==HttpURLConnection.HTTP_OK)
            {
                InputStream inputStream=httpURLConnection.getInputStream();


                    episodeArrayList=XMLUtil.PullParser.parsePerson(inputStream);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return  episodeArrayList;

    }

    @Override
    protected void onPostExecute(ArrayList<Episode> episodeArrayList) {
        super.onPostExecute(episodeArrayList);
        activity.setUpData(episodeArrayList);
    }



    static public interface IData
    {
        public void setUpData(ArrayList<Episode> episodeArrayList);
        public Context getContext();
    }

}
