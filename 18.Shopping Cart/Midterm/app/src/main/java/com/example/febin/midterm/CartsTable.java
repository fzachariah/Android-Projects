package com.example.febin.midterm;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by febin on 20/03/2017.
 */

public class CartsTable {

    final static  String TABLE_NAME="cart";
    final static  String COLUMN_CART_ID="_id";
    final static  String COLUMN_ORDER_TITLE="title";
    final static  String COLUMN_ORDER_PRICE="price";
    final static  String COLUMN_NEWS_LINK="link";


    static public void onCreate(SQLiteDatabase db)
    {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("CREATE TABLE "+ TABLE_NAME+ " (");
        stringBuilder.append(COLUMN_CART_ID+" text not null , ");
        stringBuilder.append(COLUMN_ORDER_TITLE+" text not null, ");
        stringBuilder.append(COLUMN_ORDER_PRICE+" text, ");

        stringBuilder.append(COLUMN_NEWS_LINK+" text); ");
        try {
            db.execSQL(stringBuilder.toString());
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    static public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        CartsTable.onCreate(db);
    }

}
