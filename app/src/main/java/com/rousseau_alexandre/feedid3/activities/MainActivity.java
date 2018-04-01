package com.rousseau_alexandre.feedid3.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rousseau_alexandre.feedid3.R;
import com.rousseau_alexandre.feedid3.models.ImportedMp3File;
import com.rousseau_alexandre.feedid3.models.ImportedMp3FileAdapter;

public class MainActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;

    protected ListViewImportedMp3Files list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (ListViewImportedMp3Files) findViewById(R.id.listFilesImported);
        list.loadData(MainActivity.this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file browser.
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();

                ImportedMp3File file = new ImportedMp3File(uri.getPath());
                file.insert(MainActivity.this);


                Log.i("az", "Uri: " + uri.getPath());
            }
        }
    }

    /**
     * Reload list view on back pressed
     */
    @Override
    protected void onResume(){
        super.onResume();
        ImportedMp3FileAdapter adapter = (ImportedMp3FileAdapter) list.getAdapter();
        adapter.reload();
        System.out.println("onResume called");
    }


}
