package com.rousseau_alexandre.feedid3.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rousseau_alexandre.feedid3.R;


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
            viewHolder.path = (TextView) convertView.findViewById(R.id.path);
            convertView.setTag(viewHolder);
        }

        // `getItem(position)` will get item's position of `List<Tweet>`
        ImportedFile file = getItem(position);

        // then we fill the view
        viewHolder.path.setText(file.getPath());

        return convertView;
    }

    public void reload() {
        clear();
        addAll(ImportedFile.all(getContext()));
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public TextView path;
    }
}
