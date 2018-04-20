package com.rousseau_alexandre.feedid3.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.github.madeindjs.feedID3.MyMp3File;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.NotSupportedException;
import com.rousseau_alexandre.feedid3.R;
import com.rousseau_alexandre.feedid3.models.ImportedFile;

import java.io.IOException;

public class ImportedFileActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private TextInputEditText titleInput;
    private TextInputEditText artistInput;
    private TextInputEditText albumInput;
    private TextInputEditText genreInput;
    private TextInputEditText yearInput;
    private TextInputEditText trackNumberInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imported_mp3_file);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // get widgets
        this.titleInput = findViewById(R.id.title);
        this.artistInput = findViewById(R.id.artist);
        this.albumInput = findViewById(R.id.album);
        this.genreInput = findViewById(R.id.genre);
        this.yearInput = findViewById(R.id.year);
        this.trackNumberInput = findViewById(R.id.track_number);

        // get file
        Intent intent = getIntent();
        ImportedFile file = (ImportedFile) intent.getSerializableExtra(MainActivity.EXTRA_MESSAGE);
        final MyMp3File mp3File = file.getMyMp3File();

        // set title toolbar
        toolbar.setTitle(file.getPath());
        setSupportActionBar(toolbar);

        // fill form
        if(mp3File != null){
            ID3v1 id3 = mp3File.getCurrentID3();
            this.titleInput.setText(id3.getTitle());
            this.artistInput.setText(id3.getArtist());
            this.albumInput.setText(id3.getAlbum());
            this.genreInput.setText(id3.getGenreDescription());
            this.yearInput.setText(id3.getYear());
            this.trackNumberInput.setText(id3.getTrack());
        } else {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "Cannot open MP3 file", Snackbar.LENGTH_LONG)
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                    .show();
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check MP3 file access
                if(mp3File == null) {
                    Snackbar.make(view, "Cannot open MP3 file", Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();
                    return;
                }

                verifyStoragePermissions();

                ID3v24Tag id3 = new ID3v24Tag();
                id3.setTitle(ImportedFileActivity.this.titleInput.getText().toString());
                id3.setAlbum(ImportedFileActivity.this.albumInput.getText().toString());
                id3.setArtist(ImportedFileActivity.this.artistInput.getText().toString());
                //id3.setGenre(ImportedFileActivity.this.genreInput.getText().toString());
                id3.setYear(ImportedFileActivity.this.yearInput.getText().toString());
                id3.setTrack(ImportedFileActivity.this.trackNumberInput.getText().toString());

                mp3File.setNewID3(id3);

                try{
                    mp3File.update();
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .show();
                }catch(IOException | NotSupportedException e){
                    Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();
                    Log.d("myapp", Log.getStackTraceString(e));
                }

            }
        });
    }

    private void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
