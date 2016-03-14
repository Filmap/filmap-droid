package com.filmap.filmap.models;

import java.util.ArrayList;

/**
 * Created by ivanilson on 2016-03-14.
 */
public class OMDBSearchResult
{
    private ArrayList<OMDBFilm> Search;
    private String totalResults;
    private String Response;

    public ArrayList<OMDBFilm> getSearch() { return this.Search; }
    public void setSearch(ArrayList<OMDBFilm> Search) { this.Search = Search; }

    public String getTotalResults() { return this.totalResults; }
    public void setTotalResults(String totalResults) { this.totalResults = totalResults; }

    public String getResponse() { return this.Response; }
    public void setResponse(String Response) { this.Response = Response; }
}