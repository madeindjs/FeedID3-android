package com.rousseau_alexandre.feedid3.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.List;

public class ImportedMp3File extends Record {

    public static final String TABLE_NAME = "files";
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

    public ImportedMp3File(String path) {
        this.path = path;
    }

    /**
     * Cursor given from this kind of query `SELECT id, path FROM files`
     *
     * @param cursor
     */
    public ImportedMp3File(Cursor cursor) {
        id = cursor.getLong(0);
        path = cursor.getString(1);
    }

    public String getPath() {
        return path;
    }

    public static List<ImportedMp3File> all(Context context) {
        SQLiteDatabase db = getDatabase(context);
        Cursor cursor = db.rawQuery("SELECT id, path FROM " + TABLE_NAME, null);

        List<ImportedMp3File> files = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            files.add(new ImportedMp3File(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return files;
    }

    public static ImportedMp3File get(Context context, long id) {
        Cursor cursor = getDatabase(context).rawQuery(
                String.format("SELECT id, path FROM %s WHERE id = ?", TABLE_NAME),
                new String[]{Long.toString(id)}
        );
        cursor.moveToFirst();

        return new ImportedMp3File(cursor);
    }
    protected ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("path", path);
        return values;
    }



}
