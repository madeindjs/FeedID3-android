package com.rousseau_alexandre.feedid3.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.provider.MediaStore.Audio.Media;

import com.rousseau_alexandre.feedid3.R;
import com.rousseau_alexandre.feedid3.models.ImportedFile;
import com.rousseau_alexandre.feedid3.models.ImportedFileAdapter;

import java.io.File;
import java.net.URI;

public class MainActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;
    public static final String EXTRA_MESSAGE = "com.github.madeindjs.feedide3-android.FILE_ID";


    protected ListViewImportedFiles list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (ListViewImportedFiles) findViewById(R.id.listFilesImported);
        list.loadData(MainActivity.this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImportedFile file = (ImportedFile) list.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, ImportedFileActivity.class);
                intent.putExtra(EXTRA_MESSAGE, file);
                startActivity(intent);
            }
        });

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file browser.
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                // Filter to show only images, using the image MIME data type. If one wanted to
                // search for ogg vorbis files, the type would be "audio/ogg". To search for all
                // documents available via installed storage providers, it would be "*/*".
                intent.setType("audio/mpeg");
                // Filter to only show results that can be "opened", such as a file (as opposed to a
                // list of contacts or timezones)
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(intent, READ_REQUEST_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    Snackbar.make(view, "Please install a File Manager.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.action_settings:
                break;
            case R.id.action_credits:
                Intent intent = new Intent(MainActivity.this, CreditsActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            if (resultData != null) {
                Uri uri = resultData.getData();

                String path = uri.toString();
                if (path.toLowerCase().startsWith("file://")) {
                    path = (new File(URI.create(path))).getAbsolutePath();
                } else {
                    path = this.getAudioPath(uri);
                    // not a valid file selected
                    // @see: https://stackoverflow.com/questions/20067508/get-real-path-from-uri-android-kitkat-new-storage-access-framework
                    // @see: https://stackoverflow.com/questions/19834842/android-gallery-on-android-4-4-kitkat-returns-different-uri-for-intent-action
                }
                ImportedFile file = new ImportedFile(path);
                file.insert(MainActivity.this);

            }
        } else {
            // Back from pick with cancel status
        }
    }

    /**
     * @see https://stackoverflow.com/questions/20067508/get-real-path-from-uri-android-kitkat-new-storage-access-framework
     * @param uri
     * @return
     */
    public String getAudioPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String documentId = cursor.getString(0);
        documentId = documentId.substring(documentId.lastIndexOf(":") + 1);
        cursor.close();


        final String[] projection = {MediaStore.Audio.Media.DATA};
        final String selection = MediaStore.Audio.Media._ID + " = ? ";
        final String[] params = {documentId};

        {
            cursor = getContentResolver().query(
                    Media.EXTERNAL_CONTENT_URI,
                    projection, selection, params, null
            );
            cursor.moveToFirst();

            try {
                int index = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                String path = cursor.getString(index);

                return path;
            } catch (CursorIndexOutOfBoundsException e) {
            } finally {
                cursor.close();
            }
        }


        return documentId;
    }

    /**
     * Reload list view on back pressed
     */
    @Override
    protected void onResume(){
        super.onResume();
        ImportedFileAdapter adapter = (ImportedFileAdapter) list.getAdapter();
        adapter.reload();
        System.out.println("onResume called");
    }

    /**
     * @see https://stackoverflow.com/questions/32431723/read-external-storage-permission-for-android#32617585
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted and now can proceed
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Permission denied to read your External storage", Snackbar.LENGTH_LONG).show();
                }
                return;
            }
            // add other cases for more permissions
        }
    }


}
