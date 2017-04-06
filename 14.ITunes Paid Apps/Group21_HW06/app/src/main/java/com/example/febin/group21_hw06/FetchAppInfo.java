package com.example.febin.group21_hw06;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by febin on 20/02/2017.
 */

public class FetchAppInfo extends AsyncTask<String,Void,ArrayList<AppInfo>> {

    IData activity;

    public FetchAppInfo(IData activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<AppInfo> doInBackground(String... params) {
        RequestParams requestParams=new RequestParams("GET",params[0]);
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
        ArrayList<AppInfo> list=AppUtil.parser(stringBuilder.toString());
        Log.d("^^33333 Size:",""+list.size());

        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<AppInfo> appInfoArrayList) {
        super.onPostExecute(appInfoArrayList);
        activity.setUpData(appInfoArrayList);
    }

    static public interface IData
    {
        public void setUpData(ArrayList<AppInfo> appInfoArrayList);
        public Context getContext();
    }
}
