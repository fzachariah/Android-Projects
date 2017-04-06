package com.example.febin.group21_hw05;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by febin on 15/02/2017.
 */

public class FetchGameListAsync extends AsyncTask<String,Void,ArrayList<Game>> {

    final String URL="http://thegamesdb.net/api/GetGamesList.php";


    IData activity;

    public FetchGameListAsync(IData activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<Game> doInBackground(String... params) {

        RequestParams requestParams=new RequestParams("GET",URL);
        requestParams.addParam("name",params[0]);

        ArrayList<Game> gameArrayList=new ArrayList<>();
        try {
            HttpURLConnection httpURLConnection=requestParams.setUpConnection();
            httpURLConnection.connect();

            int status_code = httpURLConnection.getResponseCode();
            Log.d("^^^^11111status",""+status_code);
            if(status_code==520 ||status_code==522)
            {
                httpURLConnection.connect();

                status_code = httpURLConnection.getResponseCode();
                Log.d("^^^^11111status",""+status_code);
            }
            if(status_code==HttpURLConnection.HTTP_OK)
            {
                InputStream inputStream=httpURLConnection.getInputStream();
                gameArrayList=XMLUtil.PullParser.parseGame(inputStream);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return  gameArrayList;

    }

    @Override
    protected void onPostExecute(ArrayList<Game> gameArrayList) {
        super.onPostExecute(gameArrayList);
        activity.setUpData(gameArrayList);
    }

    static public interface IData
    {
        public void setUpData(ArrayList<Game> gameArrayList);
        public Context getContext();
    }
}
