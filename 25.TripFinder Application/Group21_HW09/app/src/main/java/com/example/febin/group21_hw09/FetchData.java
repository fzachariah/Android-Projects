package com.example.febin.group21_hw09;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by febin on 04/04/2017.
 */

public class FetchData extends AsyncTask<String,Void,PolylineOptions> {

    IData activity;

    public FetchData(IData activity) {
        this.activity = activity;
    }

    @Override
    protected PolylineOptions doInBackground(String... params) {
        String val="";
        OkHttpClient okHttpClient=new OkHttpClient();

        Request request = new Request.Builder()
                .url(params[0])
                .build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            val=response.body().string();
            Log.d("testing here",""+val);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jObject=null;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DirectionJsonParser parser = new DirectionJsonParser();
        routes = parser.parse(jObject);

        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;


        for(int i=0;i<routes.size();i++){
            Log.d("PolyLine","Test");
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            List<HashMap<String, String>> path = routes.get(i);


            for(int j=0;j<path.size();j++){
                HashMap<String,String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }


            lineOptions.addAll(points);
            lineOptions.width(10);
            lineOptions.color(Color.BLUE);
        }

        return lineOptions;

    }

    @Override
    protected void onPostExecute(PolylineOptions data) {
        super.onPostExecute(data);
        activity.setUpData(data);

    }
    static public interface IData
    {
        public void setUpData(PolylineOptions data);
        public Context getContext();
    }
}
