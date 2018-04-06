package com.rousseau_alexandre.feedid3.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.github.madeindjs.feedID3.MyMp3File;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImportedFile extends Record {

    public static final String TABLE_NAME = "imported_files";
    public static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, path TEXT NOT NULL);";

    private long id;
    private String path;

    /**
     * Dirty truck to get table name in child class
     * @return
     */
    protected String getTableName() {
        return TABLE_NAME;
    }

    public ImportedFile(String path) {
        this.path = path;
    }

    /**
     * Cursor given from this kind of query `SELECT id, path FROM files`
     *
     * @param cursor
     */
    public ImportedFile(Cursor cursor) {
        id = cursor.getLong(0);
        path = cursor.getString(1);
    }

    public static List<ImportedFile> all(Context context) {
        SQLiteDatabase db = getDatabase(context);
        Cursor cursor = db.rawQuery("SELECT id, path FROM " + TABLE_NAME, null);

        List<ImportedFile> files = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            files.add(new ImportedFile(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return files;
    }

    public static ImportedFile get(Context context, long id) {
        Cursor cursor = getDatabase(context).rawQuery(
                String.format("SELECT id, path FROM %s WHERE id = ?", TABLE_NAME),
                new String[]{Long.toString(id)}
        );
        cursor.moveToFirst();

        return new ImportedFile(cursor);
    }

    public String getPath() {
        return path;
    }

    public MyMp3File getMyMp3File() throws IOException, UnsupportedTagException, InvalidDataException {
        return new MyMp3File(path);
    }


    protected ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("path", path);
        return values;
    }



}
