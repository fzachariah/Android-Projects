package com.example.febin.group21_hw08;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by febin on 04/04/2017.
 */

public class JSONParser {

    public static String  setLocation(String data)
    {

        String key="";
        try {

            JSONArray jsonArray=new JSONArray(data);
            if(jsonArray.length()>0)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(0);
                 key=""+jsonObject.getInt("Key");

            }
            else
            {
                key="error";
            }
        }catch (Exception e)
        {
            Log.d("Error: ",e.toString());
        }
        return key;

    }

    public static ArrayList<String> currentLocationData(String data)
    {
        ArrayList<String> stringArrayList=new ArrayList<>();

        try {

            JSONArray jsonArray=new JSONArray(data);
            if(jsonArray.length()>0)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(0);
                stringArrayList.add(""+jsonObject.getLong("EpochTime"));
                stringArrayList.add(""+jsonObject.getString("LocalObservationDateTime"));
                stringArrayList.add(""+jsonObject.getString("WeatherText"));
                stringArrayList.add(""+jsonObject.getInt("WeatherIcon"));
                JSONObject jsonObject1=jsonObject.getJSONObject("Temperature");
                JSONObject jsonObject2=jsonObject1.getJSONObject("Metric");
                stringArrayList.add(""+jsonObject2.getDouble("Value"));





            }

        }catch (Exception e)
        {
            Log.d("Error: ",e.toString());
        }



        return stringArrayList;
    }

    public static String getHeading(String data) {

        String heading="";
        try
        {
            JSONObject jsonObject=new JSONObject(data);
            JSONObject jsonObjectHeadLine=jsonObject.getJSONObject("Headline");
            heading=jsonObjectHeadLine.getString("Text");
        }catch (JSONException e)
        {

        }


        return  heading;
    }

    public static String getDate(String data) {


        String date="";
        try
        {
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonObjectDaily=jsonObject.getJSONArray("DailyForecasts");
            JSONObject jsonObject1=jsonObjectDaily.getJSONObject(0);
            String temp=jsonObject1.getString("Date");
            temp=temp.substring(0,temp.indexOf("T"));
            Log.d("Log:date",temp);
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            Date d=simpleDateFormat.parse(temp);
            Log.d("Log:date",d.toString());
            SimpleDateFormat dateFormat=new SimpleDateFormat("MMMM dd, yyyy");
            date=dateFormat.format(d);
            Log.d("Log:date",date);
        }catch (JSONException e)
        {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return  date;
    }

    public static String getTemp(String data,String type) {

        String temp="";

        try
        {
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonObjectDaily=jsonObject.getJSONArray("DailyForecasts");
            JSONObject jsonObject1=jsonObjectDaily.getJSONObject(0);
            JSONObject jsonObjectTemperature=jsonObject1.getJSONObject("Temperature");
            JSONObject minimum=jsonObjectTemperature.getJSONObject("Minimum");
            JSONObject maximum=jsonObjectTemperature.getJSONObject("Maximum");

            String minimumVal=minimum.getString("Value");
            String maximumVal=maximum.getString("Value");

            if(type.equals("C"))
            {
                minimumVal=Util.fToC(minimumVal);
                maximumVal=Util.fToC(maximumVal);
            }

            temp="Temperature: "+maximumVal+(char) 0x00B0 +"/"+minimumVal+(char) 0x00B0;
        }catch (JSONException e)
        {

        }



        return temp;
    }

    public static ArrayList<String> getNightDay(String data) {
        ArrayList<String> stringArrayList=new ArrayList<>();
        Log.d("Log;check",data);
        try
        {
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonObjectDaily=jsonObject.getJSONArray("DailyForecasts");
            JSONObject jsonObject1=jsonObjectDaily.getJSONObject(0);
            JSONObject jsonObjectDay=jsonObject1.getJSONObject("Day");
            JSONObject jsonObjectNight=jsonObject1.getJSONObject("Night");
            stringArrayList.add(jsonObjectDay.getString("Icon"));
            stringArrayList.add(jsonObjectDay.getString("IconPhrase"));
            stringArrayList.add(jsonObjectNight.getString("Icon"));
            stringArrayList.add(jsonObjectNight.getString("IconPhrase"));

        }catch (JSONException e)
        {

        }



        return stringArrayList;

    }

    public static String getURLMore(String data) {

        String url="";
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonObjectDaily = jsonObject.getJSONArray("DailyForecasts");
            JSONObject jsonObject1 = jsonObjectDaily.getJSONObject(0);
            url=jsonObject1.getString("MobileLink");
        }catch (Exception e)
        {

        }

        return url;
    }

    public static String getURLExtended(String data) {

        String url="";
        try {
            JSONObject jsonObject=new JSONObject(data);
            JSONObject jsonObjectHeadLine=jsonObject.getJSONObject("Headline");
            url=jsonObjectHeadLine.getString("MobileLink");

        }catch (Exception e)
        {

        }
        return url;
    }

    public static ArrayList<DayDetails> getFiveDayDetails(String data) {

        ArrayList<DayDetails> dayDetailsArrayList=new ArrayList<>();

        try
        {
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonObjectDaily=jsonObject.getJSONArray("DailyForecasts");
            Log.d("Log:Imp",""+jsonObjectDaily.length());
            for(int i=0;i<jsonObjectDaily.length();i++)
            {
                DayDetails dayDetails=new DayDetails();
                JSONObject jsonObject1=jsonObjectDaily.getJSONObject(i);
                String temp=jsonObject1.getString("Date");
                temp=temp.substring(0,temp.indexOf("T"));
                Log.d("Log:date",temp);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                Date d=simpleDateFormat.parse(temp);
                Log.d("Log:date",d.toString());
                SimpleDateFormat dateFormat=new SimpleDateFormat("MMM dd, yyyy");
                String date=dateFormat.format(d);
                dayDetails.setId(i);
                dayDetails.setDate(date);
                JSONObject jsonObjectTemperature=jsonObject1.getJSONObject("Temperature");
                JSONObject minimum=jsonObjectTemperature.getJSONObject("Minimum");
                JSONObject maximum=jsonObjectTemperature.getJSONObject("Maximum");
                String minimumVal=minimum.getString("Value");
                String maximumVal=maximum.getString("Value");
                dayDetails.setMinimumTemp(minimumVal);
                dayDetails.setMaximumTemp(maximumVal);

                JSONObject jsonObjectDay=jsonObject1.getJSONObject("Day");
                JSONObject jsonObjectNight=jsonObject1.getJSONObject("Night");
                dayDetails.setDayIcon(jsonObjectDay.getString("Icon"));
                dayDetails.setDayPhrase(jsonObjectDay.getString("IconPhrase"));
                dayDetails.setNightIcon(jsonObjectNight.getString("Icon"));
                dayDetails.setNightPhrase(jsonObjectNight.getString("IconPhrase"));

                String url=jsonObject1.getString("MobileLink");
                dayDetails.setMobileLink(url);
                dayDetailsArrayList.add(dayDetails);
                Log.d("Log:Imp",dayDetails.toString());

            }



        }catch (Exception e)
        {
            Log.d("Log:Imp",e.toString());
        }


        return  dayDetailsArrayList;

    }

    public static String getCity(String data) {

        String city="";

        try {

            JSONArray jsonArray = new JSONArray(data);
            if (jsonArray.length() > 0) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                city = "" + jsonObject.getString("EnglishName");

            }
        }catch (Exception e)
        {

        }
        return city;
    }

    public static String getCountry(String data) {

        String country="";
        try {

            JSONArray jsonArray = new JSONArray(data);
            if (jsonArray.length() > 0) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                JSONObject jsonObject1=jsonObject.getJSONObject("Country");
                country = "" + jsonObject1.getString("ID");

            }
        }catch (Exception e)
        {

        }
        return country;
    }
}
