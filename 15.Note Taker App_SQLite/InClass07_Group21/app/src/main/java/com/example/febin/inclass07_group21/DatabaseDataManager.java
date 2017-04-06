package com.example.febin.inclass07_group21;

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
    private NotesDAO notesDAO;

    public DatabaseDataManager(Context mContext) {
        this.mContext = mContext;
        databaseOpenHelper =new DatabaseOpenHelper(this.mContext);
        sqLiteDatabase= databaseOpenHelper.getWritableDatabase();
        notesDAO=new NotesDAO(sqLiteDatabase);
    }

    public void close()
    {
        if(sqLiteDatabase!=null)
        {
            sqLiteDatabase.close();
        }
    }

    public NotesDAO getNewsDAO()
    {
        return this.notesDAO;
    }

    public long saveNote(Notes notes)
    {
        return this.notesDAO.save(notes);
    }
    public boolean deleteNews(Notes notes)
    {
        return this.notesDAO.delete(notes);
    }

    public boolean updateNote(Notes note)
    {
        return this.notesDAO.update(note);
    }

    public Notes getNews(Long id)
    {
        return this.notesDAO.get(id);
    }
    public List<Notes> getAllNews()
    {
        return this.notesDAO.getAll();
    }
}
