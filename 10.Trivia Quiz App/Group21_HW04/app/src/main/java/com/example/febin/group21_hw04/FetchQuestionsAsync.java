package com.example.febin.group21_hw04;

import android.content.Context;
import android.os.AsyncTask;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by febin on 10/02/2017.
 */

public class FetchQuestionsAsync extends AsyncTask<String,Void,ArrayList<Question>>{

    IData activity;

    public FetchQuestionsAsync(IData activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<Question> doInBackground(String... params) {
        RequestParams requestParams=new RequestParams("GET",params[0]);
        StringBuilder stringBuilder=new StringBuilder("");
        try {
            HttpURLConnection httpURLConnection=requestParams.setUpConnection();
            httpURLConnection.connect();

            int status_code = httpURLConnection.getResponseCode();
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
        ArrayList<Question> list=QuizUtil.parser(stringBuilder.toString());

        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<Question> questions) {
        super.onPostExecute(questions);
        activity.setUpData(questions);
    }

    static public interface IData
    {
        public void setUpData(ArrayList<Question> questions);
        public Context getContext();
    }
}
