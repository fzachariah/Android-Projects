package com.example.febin.midterm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by febin on 10/02/2017.
 */

public class FetchJSONAsync extends AsyncTask<String,Void,ArrayList<Product>>{

    IData activity;

    public FetchJSONAsync(IData activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<Product> doInBackground(String... params) {
        RequestParams requestParams=new RequestParams("GET",params[0]);
        Log.d("Printing nhere","check");
        StringBuilder stringBuilder=new StringBuilder("");
        try {
            HttpURLConnection httpURLConnection=requestParams.setUpConnection();
            httpURLConnection.connect();

            int status_code = httpURLConnection.getResponseCode();
            Log.d("^^^^11111status",""+status_code);
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
        Log.d("^^22222 Json:",stringBuilder.toString());
        ArrayList<Product> list=JSONUtil.parser(stringBuilder.toString());
        Log.d("^^33333 Size:",""+list.size());

        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<Product> products) {
        super.onPostExecute(products);
        activity.setUpData(products);
    }

    static public interface IData
    {
        public void setUpData(ArrayList<Product> questions);
        public Context getContext();
    }
}
