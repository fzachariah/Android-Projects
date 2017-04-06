package com.example.febin.midterm;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by febin on 20/03/2017.
 */

public class CartDAO {


    private SQLiteDatabase sqLiteDatabase;

    public CartDAO(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public long save(Cart news) {

        UUID idOne = UUID.randomUUID();
        String val=""+idOne;
        news.setId(val);

        for(int i=0;i<news.getCartList().size();i++)
        {
            Product product=news.getCartList().get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(CartsTable.COLUMN_CART_ID,val );
            contentValues.put(CartsTable.COLUMN_ORDER_TITLE, product.getTitle());
            contentValues.put(CartsTable.COLUMN_ORDER_PRICE, product.getPrice());
            contentValues.put(CartsTable.COLUMN_NEWS_LINK, product.getImageURLTwo());
            Log.d("Content Value:",contentValues.toString());
             sqLiteDatabase.insert(CartsTable.TABLE_NAME, null, contentValues);


        }
        return  1;


    }


    /*public List<Cart> getAll() {

        ArrayList<Cart> newsArrayList = new ArrayList<>();
        Cursor c = sqLiteDatabase.query(CartsTable.TABLE_NAME, new String[]{CartsTable.COLUMN_CART_ID,CartsTable.COLUMN_ORDER_TITLE, CartsTable.COLUMN_ORDER_PRICE, CartsTable.COLUMN_NEWS_LINK}, null, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            do
            {
                Cart news=buildNoteFromCursor(c);
                if(news!=null) {
                    newsArrayList.add(news);
                }
            }while (c.moveToNext());
            if (!c.isClosed()) {
                c.close();
            }

        }

        return newsArrayList;
    }

    private Cart buildNoteFromCursor(Cursor c) {

        HashMap<String,ArrayList<Product>> stringArrayListHashMap= new HashMap<>();
        Product news = null;
        if (c != null) {
            news  = new Product();
            if(!stringArrayListHashMap.containsKey(c.getString(0)))
            {
                ArrayList<Product> products=new ArrayList<>();

            }
            else
            {
                ArrayList<Product> products=stringArrayListHashMap.get(c.getString(0));
            }
            news.setTitle(c.getString(0));
            news.setDescription(c.getString(1));
            news.setLink(c.getString(2));
            news.setPubDate(c.getString(3));
            news.setImage(c.getString(4));

        }
        return news;
    }*/

    /*public News get(String title) {
        News notes = null;

        Cursor c = sqLiteDatabase.query(true, NewsTable.TABLE_NAME, new String[]{NewsTable.COLUMN_NEWS_ID,NewsTable.COLUMN_NEWS_TITLE, NewsTable.COLUMN_NEWS_DESCRIPTION, NewsTable.COLUMN_NEWS_LINK,NewsTable.COLUMN_NEWS_PUBDATE,NewsTable.COLUMN_NEWS_IMAGE}, NewsTable.COLUMN_NEWS_TITLE + "=?", new String[]{title + ""}, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            notes = buildNoteFromCursor(c);
            if (!c.isClosed()) {
                c.close();
            }
        }
        return notes;
    }

    public boolean delete(News news) {
        return sqLiteDatabase.delete(NewsTable.TABLE_NAME, NewsTable.COLUMN_NEWS_TITLE + "=?", new String[]{news.getTitle() + ""}) > 0;
    }*/

}
