package com.example.febin.group21_hw06;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;


public class RequestParams {
    String method;

    public RequestParams(String method, String baseURL) {
        this.method = method;
        this.baseURL = baseURL;
    }

    String baseURL;
    HashMap<String,String> params=new HashMap<>();
    public void addParam(String key,String value)
    {
        params.put(key,value);
    }
    public String getEncodedParams()
    {
        StringBuilder stringBuilder=new StringBuilder();
        for(String key:params.keySet())
        {
            try {
                String value= URLEncoder.encode(params.get(key),"UTF-8");
                if(stringBuilder.length()>0)
                {
                    stringBuilder.append("&");

                }
                stringBuilder.append(key+"="+value);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
    public String getEncodedURL()
    {
        return  baseURL+"?"+getEncodedParams();
    }
    public HttpURLConnection setUpConnection() throws IOException {
        HttpURLConnection httpURLConnection=null;
        if(method.equals("GET"))
        {
            Log.d("check here","test");
            URL url=new URL(getEncodedURL());
            httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            return httpURLConnection;
        }
        else
        {
            URL url=new URL(this.baseURL);
            httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(httpURLConnection.getOutputStream());
            outputStreamWriter.write(getEncodedParams());
            outputStreamWriter.flush();
        }
        return httpURLConnection;
    }

}
