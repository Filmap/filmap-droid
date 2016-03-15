package com.filmap.filmap.models;

/**
 * Created by duartemac on 2016-03-12.
 */

public class FilmapFilm {
    private String id;

    public String getId() { return this.id; }

    public void setId(String id) { this.id = id; }

    private String omdb;

    public String getOmdb() { return this.omdb; }

    public void setOmdb(String omdb) { this.omdb = omdb; }

    private String user_id;

    public String getUserId() { return this.user_id; }

    public void setUserId(String user_id) { this.user_id = user_id; }

    private String watched;

    public String getWatched() { return this.watched; }

    public void setWatched(String watched) { this.watched = watched; }

    private String created_at;

    public String getCreatedAt() { return this.created_at; }

    public void setCreatedAt(String created_at) { this.created_at = created_at; }

    private String updated_at;

    public String getUpdatedAt() { return this.updated_at; }

    public void setUpdatedAt(String updated_at) { this.updated_at = updated_at; }
}
