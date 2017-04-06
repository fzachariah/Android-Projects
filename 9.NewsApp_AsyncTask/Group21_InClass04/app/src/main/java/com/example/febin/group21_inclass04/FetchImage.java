package com.example.febin.group21_inclass04;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by febin on 06/02/2017.
 */

public class FetchImage extends AsyncTask<String,Void,Bitmap> {

    IDataImage activity;

    public FetchImage(IDataImage activity) {
        this.activity = activity;
    }
    protected Bitmap doInBackground(String... params) {

        RequestParams requestParams=new RequestParams("GET",params[0]);
        HttpURLConnection httpURLConnection=null;
        Bitmap bitmap=null;
        try {
            httpURLConnection=requestParams.setUpConnection();
            bitmap= BitmapFactory.decodeStream(httpURLConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bitmap;



    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        activity.setUpImage(bitmap);
    }

    static public interface IDataImage
    {
        public void setUpImage(Bitmap bitmap);

    }
}
