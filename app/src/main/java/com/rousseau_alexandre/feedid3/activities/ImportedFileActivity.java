package com.rousseau_alexandre.feedid3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.rousseau_alexandre.feedid3.R;
import com.rousseau_alexandre.feedid3.models.ImportedFile;

import java.io.IOException;

public class ImportedFileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imported_mp3_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent intent = getIntent();
        ImportedFile file = (ImportedFile) intent.getSerializableExtra(MainActivity.EXTRA_MESSAGE);
        toolbar.setTitle(file.getPath());

        try {



            file.getMyMp3File();
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImportedFileActivity.this, ChangeActivity.class);
                intent.putExtra(MainActivity.EXTRA_MESSAGE, "");
                startActivity(intent);
            }
        });
    }

}
