package com.example.febin.midterm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by febin on 26/02/2017.
 */

public class DatabaseDataManager {

    private Context mContext;
    private DatabaseOpenHelper databaseOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private CartDAO cartDAO;

    public DatabaseDataManager(Context mContext) {
        this.mContext = mContext;
        databaseOpenHelper =new DatabaseOpenHelper(this.mContext);
        sqLiteDatabase= databaseOpenHelper.getWritableDatabase();
        cartDAO=new CartDAO(sqLiteDatabase);
    }

    public void close()
    {
        if(sqLiteDatabase!=null)
        {
            sqLiteDatabase.close();
        }
    }

    public CartDAO getNewsDAO()
    {
        return this.cartDAO;
    }
    /*public List<Cart> getAllNews()
    {
        //return this.cartDAO.getAll();
    }*/

    public long saveNote(Cart news)
    {
        return this.cartDAO.save(news);
    }

   /* public News getNews(String title)
    {
        return this.newsDAO.get(title);
    }
    public boolean deleteNews(News news)
    {
        return this.newsDAO.delete(news);
    }*/

    /*

    public boolean updateNote(Notes note)
    {
        return this.notesDAO.update(note);
    }


    */
}
