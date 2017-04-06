package com.example.febin.inclass06_zachariah;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by febin on 16/02/2017.
 */

public class FetchGameDetailsAsync extends AsyncTask<String,Void,SingleGame> {

    final String URL="http://thegamesdb.net/api/GetGame.php";

    IDataDetails activity;

    public FetchGameDetailsAsync(IDataDetails activity) {
        this.activity = activity;
    }

    @Override
    protected SingleGame doInBackground(String... params) {

        SingleGame singleGame=null;
        RequestParams requestParams=new RequestParams("GET",URL);
        requestParams.addParam("id",params[0]);

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
                singleGame=XMLUtil.PullParserDetails.parseGameDetails(inputStream);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return singleGame;
    }

    @Override
    protected void onPostExecute(SingleGame singleGame) {

        activity.setUpDataDetails(singleGame);
    }

    static public interface IDataDetails
    {
        public void setUpDataDetails(SingleGame singleGame);

    }
}
