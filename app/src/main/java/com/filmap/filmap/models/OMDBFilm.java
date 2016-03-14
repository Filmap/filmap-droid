package com.filmap.filmap.models;

/**
 * Created by duartemac on 2016-03-13.
 */
public class OMDBFilm {
    private String imdbID;
    private String Title;
    private String Genre;
    private String Year;
    private String Director;
    private String Actors;
    private String Plot;
    private String Poster;
    private String imdbRating;

    public OMDBFilm(String imdbID, String title, String genre, String year, String director,
                    String actors, String plot, String poster, String imdbRating) {
        this.imdbID = imdbID;
        Title = title;
        Genre = genre;
        Year = year;
        Director = director;
        Actors = actors;
        Plot = plot;
        Poster = poster;
        this.imdbRating = imdbRating;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public String getActors() {
        return Actors;
    }

    public void setActors(String actors) {
        Actors = actors;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    @Override
    public String toString() {
        return "OMDBFilm{" +
                "imdbID='" + imdbID + '\'' +
                ", Title='" + Title + '\'' +
                ", Genre='" + Genre + '\'' +
                ", Year='" + Year + '\'' +
                ", Director='" + Director + '\'' +
                ", Actors='" + Actors + '\'' +
                ", Plot='" + Plot + '\'' +
                ", Poster='" + Poster + '\'' +
                ", imdbRating='" + imdbRating + '\'' +
                '}';
    }
}
