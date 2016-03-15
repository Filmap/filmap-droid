package com.filmap.filmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.filmap.filmap.models.FilmapFilm;
import com.filmap.filmap.models.OMDBFilm;
import com.filmap.filmap.rest.FilmapRestClient;
import com.filmap.filmap.rest.OMDBRestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;



import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {

    private ImageView ivMoviePoster;
    private TextView tvMovieTitle;
    private TextView tvMovieYear;
    private TextView tvMovieGenre;
    private TextView tvMovieRating;
    private TextView tvMovieDirector;
    private TextView tvMoviePlot;
    private Button btnWatchFilm;
    private Button btnWatchLater;

    private Boolean hasWatched = false;
    private Boolean isWatched = false;

    private String film_omdb_id;
    private final String TAG = "MovieActivity";
    private String token2 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxOCIsImlzcyI6Imh0dHA6XC9cL2FwaS5maWxtYXAubGFicy5nYVwvYXV0aGVudGljYXRlIiwiaWF0IjoxNDU3OTEyNDA3LCJleHAiOjE0NTg1MTcyMDcsIm5iZiI6MTQ1NzkxMjQwNywianRpIjoiOWIwOWYzOGE3NTNiNjY2MzM1NzM4N2EwZTkyNzhjZTUifQ.zSlFD2WCnzfowdz6pq_YiFpYL4XCF5MvH7RIML181S8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        ivMoviePoster = (ImageView) findViewById(R.id.ivMoviePoster);
        tvMovieTitle = (TextView) findViewById(R.id.tvMovieTitle);
        tvMovieYear = (TextView) findViewById(R.id.tvMovieYear);
        tvMovieGenre = (TextView) findViewById(R.id.tvMovieGenre);
        tvMovieRating = (TextView) findViewById(R.id.tvMovieRating);
        tvMovieDirector = (TextView) findViewById(R.id.tvMovieDirector);
        tvMoviePlot = (TextView) findViewById(R.id.tvMoviePlot);
        btnWatchFilm = (Button) findViewById(R.id.btnWatchFilm);
        btnWatchLater = (Button) findViewById(R.id.btnWatchLater);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent thisIntent = getIntent();

        /*Log.d(TAG, )
        */

        if (thisIntent.hasExtra("omdbid")) {
            film_omdb_id = thisIntent.getStringExtra("omdbid");
            getMovieById(film_omdb_id);
        }

        //getMovieById("tt0892769");
    }

    public void onWatchFilmClicked(View v){
        Log.i(TAG, "watchFilm Button!");
        watchFilm(true);
    }

    public void onWatchLaterClicked(View v){
        watchFilm(false);
    }

    public void watchFilm(Boolean watchAction){
        RequestParams params = new RequestParams();
        params.put("token", token2);

        if (watchAction) {

            if (hasWatched )
            Log.i(TAG, "watchFilm!");

            params.put("omdb", film_omdb_id);
            params.put("watched", (watchAction ? "1" : "0"));

            btnWatchFilm.setEnabled(false);
            btnWatchLater.setEnabled(false);

            FilmapRestClient.post("films", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e(TAG, "Failure saving film " + responseString);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.i(TAG, "Film saved: " + responseString);
                }
            });

        } else { // watch later
            Log.i(TAG, "watch Later!");
            btnWatchLater.setEnabled(false);

            FilmapRestClient.get("films/" + film_omdb_id + "/watch", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e(TAG, "Failure watching film");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.i(TAG, "Film watched");
                }
            });

        }
        // Send request
    }

    public void getMovieById(String id) {

        OMDBRestClient.getFilm(id, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                // called when response HTTP status is "200 OK"

                System.out.println(res);

                Gson gson = new GsonBuilder().create();
                // Define Response class to correspond to the JSON response returned
                OMDBFilm omdbfilm = gson.fromJson(res, OMDBFilm.class);

                System.out.println(omdbfilm.toString());

                film_omdb_id = omdbfilm.getImdbID();

                tvMovieTitle.setText(omdbfilm.getTitle());
                tvMovieYear.setText(omdbfilm.getYear());
                tvMovieGenre.setText(omdbfilm.getGenre());
                tvMovieRating.setText(String.format("%s/10", omdbfilm.getImdbRating()));
                tvMovieDirector.setText(omdbfilm.getDirector());
                tvMoviePlot.setText(omdbfilm.getPlot());

                setMovieImage(omdbfilm.getPoster());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }
        });


        // Set post params
        RequestParams params = new RequestParams();
        params.put("token", token2);
        Log.i(TAG, token2);

        // Make a post request to authenticate
        FilmapRestClient.get("films/" + id, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                // Called when response HTTP status is "200 OK"
                Log.i(TAG, "Film found: " + res);
                Gson gson = new GsonBuilder().create();
                // Define Response class to correspond to the JSON response returned
                FilmapFilm film = gson.fromJson(res, FilmapFilm.class);
                Log.i(TAG, "FILM: " + film.getWatched());
                btnWatchLater.setEnabled(false);
                hasWatched = true;
                isWatched = film.getWatched().equals("1");
                if (isWatched) {
                    btnWatchFilm.setEnabled(false);
                    Log.i(TAG, "Film watched");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.i(TAG, "User hasn't watched yet");

//                showMessage("Invalid credentials. Please try again.");
            }
        });

    }

    private void setMovieImage(String url) {
        Picasso.with(this).load(url).into(ivMoviePoster);
    }
}
