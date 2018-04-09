package com.rousseau_alexandre.feedid3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.madeindjs.feedID3.MyMp3File;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
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

        // get file
        Intent intent = getIntent();
        ImportedFile file = (ImportedFile) intent.getSerializableExtra(MainActivity.EXTRA_MESSAGE);


        try {
            MyMp3File mp3File = file.getMyMp3File();
            ID3v1 id3 = mp3File.getCurrentID3();

            ((TextInputEditText) findViewById(R.id.title)).setText(id3.getTitle());
            ((TextInputEditText) findViewById(R.id.artist)).setText(id3.getArtist());
            ((TextInputEditText) findViewById(R.id.album)).setText(id3.getAlbum());
            ((TextInputEditText) findViewById(R.id.genre)).setText(id3.getGenreDescription());
            ((TextInputEditText) findViewById(R.id.genre)).setText(id3.getYear());
            ((TextInputEditText) findViewById(R.id.track_number)).setText(id3.getTrack());

            id3.getArtist();
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "This is main activity", Snackbar.LENGTH_LONG)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                    .show();

        }


        // set title toolbar
        toolbar.setTitle(file.getPath());
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
