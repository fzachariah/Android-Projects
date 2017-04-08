package com.example.febin.group21_hw08;

import android.util.Log;

/**
 * Created by febin on 07/04/2017.
 */

public class Util {

    public static String fToC(String temp)
    {
        String temperature="";
        float fahrenheit=Float.parseFloat(temp);
        float celsius = (fahrenheit - 32) * 5 / 9;
        double val= Math.round((celsius * 10.0))/10.0;
        temperature=""+val;
        return temperature;

    }


    public static String cToF(String temp)
    {

        float celsius =Float.parseFloat(temp);
        float fahrenheit = (float) (32.0 + ((celsius * 9) / 5));
        Log.d("checking here",""+celsius+" "+fahrenheit);
        double val= Math.round((fahrenheit * 100.0))/100.0;
        return ""+val;
    }
}
