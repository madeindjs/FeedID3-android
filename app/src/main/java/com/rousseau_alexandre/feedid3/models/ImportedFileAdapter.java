package com.rousseau_alexandre.feedid3.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.rousseau_alexandre.feedid3.R;

import java.io.IOException;


/**
 * Adapter for list or pages
 *
 * https://github.com/florent37/TutosAndroidFrance/tree/master/ListViewSample
 * http://tutos-android-france.com/listview-afficher-une-liste-delements/
 */
public class ImportedFileAdapter extends ArrayAdapter<ImportedFile> {


    public ImportedFileAdapter(Context context) {
        super(context, 0, ImportedFile.all(context));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_file, parent, false);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if(viewHolder == null){
            viewHolder = new ViewHolder();
            viewHolder.loadElements(convertView);

            convertView.setTag(viewHolder);
        }

        // `getItem(position)` will get item's position of `List<Tweet>`
        viewHolder.file = getItem(position);

        // then we fill the view

        viewHolder.setUpBadges();

        return convertView;
    }

    public void reload() {
        clear();
        addAll(ImportedFile.all(getContext()));
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public TextView path;
        public LinearLayout badgesLayout;
        public TextView badgeNoAlbum;
        public TextView badgeNoArtist;
        public TextView badgeNoGenre;
        public TextView badgeNoYear;

        public ImportedFile file;


        public void loadElements(View convertView) {
            path = (TextView) convertView.findViewById(R.id.path);
            badgesLayout = (LinearLayout) convertView.findViewById(R.id.badges);

            badgeNoAlbum = (TextView) badgesLayout.findViewById(R.id.badgeNoAlbum);
            badgeNoArtist = (TextView) badgesLayout.findViewById(R.id.badgeNoArtist);
            badgeNoGenre = (TextView) badgesLayout.findViewById(R.id.badgeNoGenre);
            badgeNoYear = (TextView) badgesLayout.findViewById(R.id.badgeNoYear);
        }

        public void setUpBadges() {
            ID3v1 id3 =file.getMyMp3File().getCurrentID3();

            if(id3.getArtist() != null && !id3.getArtist().isEmpty()) {
                badgeNoArtist.setVisibility(View.INVISIBLE);
            }

            if(id3.getAlbum() != null && !id3.getAlbum().isEmpty()) {
                badgeNoAlbum.setVisibility(View.INVISIBLE);
            }

            if(id3.getYear() != null && !id3.getYear().isEmpty()) {
                badgeNoYear.setVisibility(View.INVISIBLE);
            }

            if(id3.getGenreDescription() != null && !id3.getGenreDescription().isEmpty()) {
                badgeNoGenre.setVisibility(View.INVISIBLE);
            }


            path.setText(file.getPath());
        }
    }
}
