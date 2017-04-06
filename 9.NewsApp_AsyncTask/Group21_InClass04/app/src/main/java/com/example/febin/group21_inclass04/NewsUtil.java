package com.example.febin.group21_inclass04;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by febin on 06/02/2017.
 */

public class NewsUtil {

    public static ArrayList<News> parser(String data)
    {
        Log.d("22222222222cehcking",data);
        ArrayList<News> list=new ArrayList<>();
        try {
            JSONObject root=new JSONObject(data);
            String status=root.getString("status");
            if(status.equals("ok"))
            {
                Log.d("2222222222Sucessful","sucess");
                JSONArray jsonArray=root.getJSONArray("articles");
                Log.d("Status",""+jsonArray.length());
                for(int i=0;i<jsonArray.length();i++)
                {
                    News news=new News();
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    String author=jsonObject1.getString("author");
                    Log.d("Check here",author);
                    String title=jsonObject1.getString("title");
                    Log.d("Check here",title);
                    String description=jsonObject1.getString("description");
                    String urlToImage=jsonObject1.getString("urlToImage");
                    String publishedAt=jsonObject1.getString("publishedAt");
                    news.setAuthor(author);
                    news.setTitle(title);
                    news.setDescription(description);
                    news.setUrlToImage(urlToImage);
                    news.setPublishedAt(publishedAt);
                    list.add(news);

                }



            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return  list;
    }



}
