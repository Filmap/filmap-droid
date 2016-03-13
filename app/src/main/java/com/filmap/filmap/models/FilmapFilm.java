package com.filmap.filmap.models;

/**
 * Created by duartemac on 2016-03-12.
 */

public class FilmapFilm {
    private long id;
    private long omdb;
    private long user_id;
    private boolean watched;

    public FilmapFilm(long id, long omdb, long user_id, boolean watched) {
        this.id = id;
        this.omdb = omdb;
        this.user_id = user_id;
        this.watched = watched;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOmdb() {
        return omdb;
    }

    public void setOmdb(long omdb) {
        this.omdb = omdb;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }
}
