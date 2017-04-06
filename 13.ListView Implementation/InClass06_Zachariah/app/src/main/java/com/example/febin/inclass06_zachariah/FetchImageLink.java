package com.example.febin.inclass06_zachariah;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by febin on 20/02/2017.
 */

public class FetchImageLink extends AsyncTask<ArrayList<Game>,Void,ArrayList<String>> {

    final String URL="http://thegamesdb.net/api/GetGame.php";

    ILinkDetails activity;

    public FetchImageLink(ILinkDetails activity) {
        this.activity = activity;
    }


    @Override
    protected ArrayList<String> doInBackground(ArrayList<Game>... params) {
        ArrayList<String> listLink=new ArrayList<>();

        for(int i=0;i<params[0].size();i++)
        {
            RequestParams requestParams=new RequestParams("GET",URL);
            requestParams.addParam("id",params[0].get(i).getId());

            try {
                HttpURLConnection httpURLConnection=requestParams.setUpConnection();
                httpURLConnection.connect();

                int status_code = httpURLConnection.getResponseCode();
                Log.d("^^^^11111status",""+status_code);
                if(status_code==520 ||status_code==522)
                {
                    Log.d("^^^^11111status",""+status_code);
                    listLink.add("");
                }
                if(status_code==HttpURLConnection.HTTP_OK)
                {
                    InputStream inputStream=httpURLConnection.getInputStream();
                    String link =XMLUtil.PullParserLink.parseGameLink(inputStream);
                    listLink.add(link);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }




        return  listLink;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        activity.setUpDataDetails(strings);
    }

    static public interface ILinkDetails
    {
        public void setUpDataDetails(ArrayList<String> links);

    }
}
