package com.example.febin.midterm;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by febin on 20/03/2017.
 */

public class JSONUtil {

    public static ArrayList<Product> parser(String data)
    {
        ArrayList<Product> productslist=new ArrayList<>();
        try {
           /* JSONObject jsonObject=new JSONObject(data);
            JSONObject jsonObjectOne=jsonObject.getJSONObject("feed");*/
            //JSONArray jsonArray=jsonObjectOne.getJSONArray("entry");

            JSONArray jsonArray=new JSONArray(data);

            for(int i=0;i<jsonArray.length();i++)
            {
                Product product=new Product();

                JSONObject jsonProduct=jsonArray.getJSONObject(i);

                //JSONObject jsonObjectImName=jsonProduct.getJSONObject("name");
                String appName=jsonProduct.getString("name");
                product.setTitle(appName);

                String id=jsonProduct.getString("id");
                product.setId((id));



                JSONObject jsonOArrayImImage=jsonProduct.getJSONObject("image_urls");
                JSONArray appImage1=jsonOArrayImImage.getJSONArray("91x121");
                JSONObject jsonObject1=appImage1.getJSONObject(0);
                String imageFirstURL= jsonObject1.getString("url");
                product.setImageURLOne(imageFirstURL);



                JSONArray appImage2=jsonOArrayImImage.getJSONArray("300x400");
                JSONObject jsonObject2=appImage2.getJSONObject(0);
                String imageFirstURL11= jsonObject2.getString("url");
                product.setImageURLTwo(imageFirstURL11);


                JSONArray priceJsonArray1=jsonProduct.getJSONArray("skus");
                JSONObject jsonObjectprice=priceJsonArray1.getJSONObject(0);
                String msrprice=jsonObjectprice.getString("msrp_price");
                product.setMsrpPrcie(msrprice);

                String actualPrice=jsonObjectprice.getString("sale_price");
                product.setPrice(actualPrice);

                float mmsrpTemp=Float.parseFloat(msrprice);
                float salesPrice=Float.parseFloat(actualPrice);
                float temp= (mmsrpTemp-salesPrice)/(mmsrpTemp);
                float val=temp*100;

                product.setDiscount(""+val);

                productslist.add(product);

            }
        }catch (Exception e)
        {
            Log.d("Error: ",e.toString());
        }

        return  productslist;
    }

}
