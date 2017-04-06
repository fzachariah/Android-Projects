package com.example.febin.inclass07_group21;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by febin on 27/02/2017.
 */

public class NotesDAO {

    private SQLiteDatabase sqLiteDatabase;

    public NotesDAO(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public long save(Notes notes) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesTable.COLUMN_NOTES_NOTE, notes.getNote());
        contentValues.put(NotesTable.COLUMN_NOTES_PRIORITY, notes.getPriority());
        contentValues.put(NotesTable.COLUMN_NOTES_TIME, notes.getUpdateTime());
        contentValues.put(NotesTable.COLUMN_NOTES_STATUS, notes.getStatus());
        return sqLiteDatabase.insert(NotesTable.TABLE_NAME, null, contentValues);

    }


    public boolean delete(Notes notes) {
        return sqLiteDatabase.delete(NotesTable.TABLE_NAME, NotesTable.COLUMN_NOTES_ID + "=?", new String[]{notes.getId() + ""}) > 0;
    }

    public boolean update(Notes note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesTable.COLUMN_NOTES_STATUS, note.getStatus());
        contentValues.put(NotesTable.COLUMN_NOTES_TIME, note.getUpdateTime());
        return sqLiteDatabase.update(NotesTable.TABLE_NAME, contentValues, NotesTable.COLUMN_NOTES_ID + "=?", new String[]{note.getId() + ""}) > 0;

    }

    public Notes get(long id) {
        Notes notes = null;

        Cursor c = sqLiteDatabase.query(true, NotesTable.TABLE_NAME, new String[]{NotesTable.COLUMN_NOTES_ID,NotesTable.COLUMN_NOTES_NOTE, NotesTable.COLUMN_NOTES_PRIORITY, NotesTable.COLUMN_NOTES_TIME,NotesTable.COLUMN_NOTES_STATUS}, NotesTable.COLUMN_NOTES_ID + "=?", new String[]{id + ""}, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            notes = buildNoteFromCursor(c);
            if (!c.isClosed()) {
                c.close();
            }
        }
        return notes;
    }

    public List<Notes> getAll() {

        ArrayList<Notes> notesArrayList = new ArrayList<>();
        Cursor c = sqLiteDatabase.query(NotesTable.TABLE_NAME, new String[]{NotesTable.COLUMN_NOTES_ID,NotesTable.COLUMN_NOTES_NOTE, NotesTable.COLUMN_NOTES_PRIORITY, NotesTable.COLUMN_NOTES_TIME,NotesTable.COLUMN_NOTES_STATUS}, null, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            do
            {
                Notes notes=buildNoteFromCursor(c);
                if(notes!=null) {
                    notesArrayList.add(notes);
                }
            }while (c.moveToNext());
            if (!c.isClosed()) {
                c.close();
            }

        }

        return notesArrayList;
    }

    private Notes buildNoteFromCursor(Cursor c) {
        Notes notes = null;
        if (c != null) {
            notes  = new Notes();
            notes.setId(c.getLong(0));
            notes.setNote(c.getString(1));
            notes.setPriority(c.getInt(2));
            notes.setUpdateTime(c.getString(3));
            notes.setStatus(c.getInt(4));

        }
        return notes;
    }
}
