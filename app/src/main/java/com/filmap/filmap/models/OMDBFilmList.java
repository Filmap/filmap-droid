package com.filmap.filmap.models;


import java.util.ArrayList;

/**
 * Created by duartemac on 2016-03-13.
 */
public class OMDBFilmList extends ArrayList<OMDBFilm>{

    private static final long serialVersionUID = 1L;
    private static final String TAG = "FilmList";

    private static OMDBFilmList list = new OMDBFilmList();

    public void inserir(OMDBFilm film) {

        list.add(film);
    }

    public static OMDBFilmList getInstance() {
        return list;
    }


    @Override
    public String toString() {
        String result ="";

        for ( OMDBFilm f : list ) {
            result += f.toString() + "\n";
        }

        return result;
    }


}
