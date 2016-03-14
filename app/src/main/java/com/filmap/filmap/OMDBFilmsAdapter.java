package com.filmap.filmap;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.filmap.filmap.models.OMDBFilm;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ivanilson on 2016-03-14.
 */
public class OMDBFilmsAdapter extends ArrayAdapter<OMDBFilm> {

    public OMDBFilmsAdapter(Context context, ArrayList<OMDBFilm> omdbfilms){
        super(context, 0, omdbfilms);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OMDBFilm omdbFilm = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_omdb_list, parent, false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvYear = (TextView) convertView.findViewById(R.id.tvYear);
        TextView tvType = (TextView) convertView.findViewById(R.id.tvType);
        ImageView ivPoster = (ImageView) convertView.findViewById(R.id.ivPoster);

        tvTitle.setText(omdbFilm.getTitle());
        tvYear.setText(omdbFilm.getYear());
        tvType.setText(omdbFilm.getType());

        if (omdbFilm.getPoster() != null) {
            Picasso.with(this.getContext()).load(omdbFilm.getPoster()).into(ivPoster);
        }

        return convertView;
    }

}
