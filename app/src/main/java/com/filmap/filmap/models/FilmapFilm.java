package com.filmap.filmap.models;

/**
 * Created by duartemac on 2016-03-12.
 */

public class FilmapFilm {
    private String id;
    private String omdb;
    private String user_id;
    private boolean watched;

    public FilmapFilm(String id, String omdb, String user_id, boolean watched) {
        this.id = id;
        this.omdb = omdb;
        this.user_id = user_id;
        this.watched = watched;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOmdb() {
        return omdb;
    }

    public void setOmdb(String omdb) {
        this.omdb = omdb;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }
}
