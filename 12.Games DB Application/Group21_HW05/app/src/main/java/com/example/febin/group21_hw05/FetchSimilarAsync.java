package com.example.febin.group21_hw05;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by febin on 18/02/2017.
 */

public class FetchSimilarAsync extends AsyncTask<ArrayList<String>,Void,ArrayList<Game>> {


    final String URL="http://thegamesdb.net/api/GetGame.php";

    IDataSimilar activity;

    public FetchSimilarAsync(IDataSimilar activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<Game> doInBackground(ArrayList<String>... params) {

        ArrayList<Game> similarList=new ArrayList<>();
        for(int i=0;i<params[0].size();i++)
        {
            RequestParams requestParams=new RequestParams("GET",URL);
            requestParams.addParam("id",params[0].get(i));

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
                    Game result=XMLUtil.PullParserSimilar.parseGameSimilar(inputStream);
                    similarList.add(result);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return similarList;
    }

    @Override
    protected void onPostExecute(ArrayList<Game> gameArrayList) {
        super.onPostExecute(gameArrayList);
        activity.setUpDataDetails(gameArrayList);
    }

    static public interface IDataSimilar
    {
        public void setUpDataDetails(ArrayList<Game> similarList);

    }
}
