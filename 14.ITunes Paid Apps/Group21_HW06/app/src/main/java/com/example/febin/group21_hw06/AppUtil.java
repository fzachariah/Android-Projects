package com.example.febin.group21_hw06;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by febin on 20/02/2017.
 */

public class AppUtil {



    public static ArrayList<AppInfo> parser(String data)
    {
        ArrayList<AppInfo> appList=new ArrayList<>();
        try {
            JSONObject jsonObject=new JSONObject(data);
            JSONObject jsonObjectOne=jsonObject.getJSONObject("feed");
            JSONArray jsonArray=jsonObjectOne.getJSONArray("entry");
            for(int i=0;i<jsonArray.length();i++)
            {
                AppInfo app=new AppInfo();

                JSONObject jsonQuestion=jsonArray.getJSONObject(i);

                JSONObject jsonObjectImName=jsonQuestion.getJSONObject("im:name");
                String appName=jsonObjectImName.getString("label");
                app.setAppName(appName);

                JSONArray jsonOArrayImImage=jsonQuestion.getJSONArray("im:image");
                JSONObject jsonObjectImage=jsonOArrayImImage.getJSONObject(0);
                String imageName=jsonObjectImage.getString("label");
                app.setImageURL(imageName);

                JSONObject jsonObjectPrice=jsonQuestion.getJSONObject("im:price");
                JSONObject jsonObjectAttr1=jsonObjectPrice.getJSONObject("attributes");
                String price=jsonObjectAttr1.getString("amount");
                String currency=jsonObjectAttr1.getString("currency");
                app.setPrice(currency+" "+price);

                JSONObject jsonObjectDev=jsonQuestion.getJSONObject("im:artist");
                String artist=jsonObjectDev.getString("label");
                app.setDevName(artist);

                JSONObject jsonObjectRelease=jsonQuestion.getJSONObject("im:releaseDate");
                String date=jsonObjectRelease.getString("label");
                app.setReleaseDate(date);

                JSONObject jsonObjectCategory=jsonQuestion.getJSONObject("category");
                JSONObject jsonObjectAttr=jsonObjectCategory.getJSONObject("attributes");
                String category=jsonObjectAttr.getString("label");
                app.setCategory(category);

                appList.add(app);

            }
        }catch (Exception e)
        {
            Log.d("Error: ",e.toString());
        }

        return  appList;
    }

}
