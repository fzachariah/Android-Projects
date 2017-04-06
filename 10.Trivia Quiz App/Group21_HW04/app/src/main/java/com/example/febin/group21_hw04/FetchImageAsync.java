package com.example.febin.group21_hw04;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;

/**
 * Created by febin on 10/02/2017.
 */

public class FetchImageAsync extends AsyncTask<String, Void,Bitmap> {


    IDataImage activity;
    int count;

    public FetchImageAsync(IDataImage activity) {
        this.activity = activity;
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        RequestParams requestParams=new RequestParams("GET",params[0]);
        count=Integer.parseInt(params[1]);
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
        HashMap<Integer,Bitmap> hmap=new HashMap<>();
        hmap.put(count,bitmap);
        super.onPostExecute(bitmap);
        activity.setUpImage(hmap);
    }

    static public interface IDataImage
    {
        public void setUpImage(HashMap<Integer,Bitmap> upImage);

    }
}
