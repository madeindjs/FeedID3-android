package com.rousseau_alexandre.feedid3.activities;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.rousseau_alexandre.feedid3.models.ImportedFileAdapter;


public class ListViewImportedFiles extends ListView {
    public ListViewImportedFiles(Context context) {
        super(context);
    }

    public ListViewImportedFiles(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewImportedFiles(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * Load all files
     *
     * @param context
     * @todo fetch this from https://raspberry-cook.fr
     */
    public void loadData(Context context) {
        final ImportedFileAdapter adapter = new ImportedFileAdapter(context);
        setAdapter(adapter);
    }


}
