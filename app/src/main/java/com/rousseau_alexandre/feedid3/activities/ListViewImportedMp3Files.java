package com.rousseau_alexandre.feedid3.activities;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.rousseau_alexandre.feedid3.models.ImportedMp3File;
import com.rousseau_alexandre.feedid3.models.ImportedMp3FileAdapter;


public class ListViewImportedMp3Files extends ListView {
    public ListViewImportedMp3Files(Context context) {
        super(context);
    }

    public ListViewImportedMp3Files(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewImportedMp3Files(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * Load all files
     *
     * @param context
     * @todo fetch this from https://raspberry-cook.fr
     */
    public void loadData(Context context) {
        final ImportedMp3FileAdapter adapter = new ImportedMp3FileAdapter(context);
        setAdapter(adapter);
    }


}
