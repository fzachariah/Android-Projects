package com.example.febin.inclass07_group21;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by febin on 27/02/2017.
 */

public class NotesTable {


    final static  String TABLE_NAME="notes";
    final static  String COLUMN_NOTES_ID="_id";
    final static  String COLUMN_NOTES_NOTE="note";
    final static  String COLUMN_NOTES_PRIORITY="Priority";
    final static  String COLUMN_NOTES_TIME="time";
    final static  String COLUMN_NOTES_STATUS="status";

    static public void onCreate(SQLiteDatabase db)
    {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("CREATE TABLE "+ TABLE_NAME+ " (");
        stringBuilder.append(COLUMN_NOTES_ID+" integer primary key autoincrement, ");
        stringBuilder.append(COLUMN_NOTES_NOTE+" text not null, ");
        stringBuilder.append(COLUMN_NOTES_PRIORITY+" integer not null, ");
        stringBuilder.append(COLUMN_NOTES_TIME+" text not null, ");
        stringBuilder.append(COLUMN_NOTES_STATUS+" integer not null); ");
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
        NotesTable.onCreate(db);
    }
}
