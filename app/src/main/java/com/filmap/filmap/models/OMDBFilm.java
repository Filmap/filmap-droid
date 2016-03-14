package com.filmap.filmap.models;

/**
 * Created by duartemac on 2016-03-13.
 * Perfected by ivanilson on 2016-03-14. < lolz
 */
public class OMDBFilm
{
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

    public OMDBFilm(){};

    private String Title;
    private String Year;
    private String Rated;
    private String Released;
    private String Runtime;
    private String Genre;
    private String Director;
    private String Writer;
    private String Actors;
    private String Plot;
    private String Language;
    private String Country;
    private String Awards;
    private String Poster;
    private String Metascore;
    private String imdbRating;
    private String imdbVotes;
    private String imdbID;
    private String Type;
    private String Response;


    public String getTitle() { return this.Title; }
    public void setTitle(String Title) { this.Title = Title; }

    public String getYear() { return this.Year; }
    public void setYear(String Year) { this.Year = Year; }

    public String getRated() { return this.Rated; }
    public void setRated(String Rated) { this.Rated = Rated; }

    public String getReleased() { return this.Released; }
    public void setReleased(String Released) { this.Released = Released; }

    public String getRuntime() { return this.Runtime; }
    public void setRuntime(String Runtime) { this.Runtime = Runtime; }

    public String getGenre() { return this.Genre; }
    public void setGenre(String Genre) { this.Genre = Genre; }

    public String getDirector() { return this.Director; }
    public void setDirector(String Director) { this.Director = Director; }

    public String getWriter() { return this.Writer; }
    public void setWriter(String Writer) { this.Writer = Writer; }

    public String getActors() { return this.Actors; }
    public void setActors(String Actors) { this.Actors = Actors; }

    public String getPlot() { return this.Plot; }
    public void setPlot(String Plot) { this.Plot = Plot; }

    public String getLanguage() { return this.Language; }
    public void setLanguage(String Language) { this.Language = Language; }

    public String getCountry() { return this.Country; }
    public void setCountry(String Country) { this.Country = Country; }

    public String getAwards() { return this.Awards; }
    public void setAwards(String Awards) { this.Awards = Awards; }

    public String getPoster() { return this.Poster; }
    public void setPoster(String Poster) { this.Poster = Poster; }

    public String getMetascore() { return this.Metascore; }
    public void setMetascore(String Metascore) { this.Metascore = Metascore; }

    public String getImdbRating() { return this.imdbRating; }
    public void setImdbRating(String imdbRating) { this.imdbRating = imdbRating; }

    public String getImdbVotes() { return this.imdbVotes; }
    public void setImdbVotes(String imdbVotes) { this.imdbVotes = imdbVotes; }

    public String getImdbID() { return this.imdbID; }
    public void setImdbID(String imdbID) { this.imdbID = imdbID; }

    public String getType() { return this.Type; }
    public void setType(String Type) { this.Type = Type; }

    public String getResponse() { return this.Response; }
    public void setResponse(String Response) { this.Response = Response; }

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
