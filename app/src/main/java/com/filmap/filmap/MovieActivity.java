package com.filmap.filmap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
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
    private ScrollView svMovieDetails;

    // For keeping track of the current movie state.
    private Boolean watchLater = false;
    private Boolean isWatched = false;
    private String filmOmdbId;

    // Misc...
    private final String TAG = "MovieActivity"; // For logging.
    private String apiToken; // For api calls.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // Set up for view manipulation.
        ivMoviePoster = (ImageView) findViewById(R.id.ivMoviePoster);
        tvMovieTitle = (TextView) findViewById(R.id.tvMovieTitle);
        tvMovieYear = (TextView) findViewById(R.id.tvMovieYear);
        tvMovieGenre = (TextView) findViewById(R.id.tvMovieGenre);
        tvMovieRating = (TextView) findViewById(R.id.tvMovieRating);
        tvMovieDirector = (TextView) findViewById(R.id.tvMovieDirector);
        tvMoviePlot = (TextView) findViewById(R.id.tvMoviePlot);
        btnWatchFilm = (Button) findViewById(R.id.btnWatchFilm);
        btnWatchLater = (Button) findViewById(R.id.btnWatchLater);
        svMovieDetails = (ScrollView) findViewById(R.id.svMovieDetails);

        // Get api token from shared preferences
        SharedPreferences sharedPref = getSharedPreferences(SignInActivity.SETTINGS_NAME, Context.MODE_PRIVATE);

        if (sharedPref.contains("token")) {
            apiToken = sharedPref.getString("token", "");
            Log.i(TAG, "API Token: " + apiToken);
        } else {
            Log.e(TAG, "Missing token. Filmap API requests won't work!!!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent thisIntent = getIntent();

        // Hide scroll view while loading content.
        svMovieDetails.setVisibility(View.INVISIBLE);

        // Get movie ID from previous activity.
        if (thisIntent.hasExtra("omdbid")) {
            filmOmdbId = thisIntent.getStringExtra("omdbid");
            displayMovieInfo(filmOmdbId);
        }

    }

    // Watching button clicked.
    public void onWatchFilmClicked(View v){
        Log.i(TAG, "watchFilm Button!");
        // Save film to list.
        saveFilm(true);
    }

    // Watch later button clicked.
    public void onWatchLaterClicked(View v){
        // Save film to watch later.
        saveFilm(false);
    }

    // Saves a film or updates an existing one.
    public void saveFilm(final Boolean watchAction){

        // Http params for post.
        RequestParams params = new RequestParams();

        Log.i(TAG, "Saving film...");

        // Api endpoint.
        String endpoint;

        // Check if the film was marked as watch later.
        if (! watchLater) {
            // Film not saved yet, save movie.
            endpoint = "films?token=" + apiToken;

            params.put("omdb", filmOmdbId);
            params.put("watched", (watchAction ? "1" : "0"));
        } else {
            // Film already saved, just mark as watched
            endpoint = "films/" + filmOmdbId + "/watch?token=" + apiToken;
        }

        // Call api to save or update a movie.
        FilmapRestClient.post(endpoint, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // Oops...
                Log.e(TAG, "Error saving film: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // 200 OK!
                Log.i(TAG, "Film saved: " + responseString);

                // Always disable watch later button.
                disableWatchLaterButton();

                // If the movie was marked as watched
                if (watchAction) {
                    // Disable watched button
                    disableWatchedButton();
                }
            }
        });

    }

    // Get movie from OMDB api and populate fields on the screen.
    public void displayMovieInfo(String id) {

        OMDBRestClient.getFilm(id, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                // 200 OK!

                Log.i(TAG, res);

                // Parse JSON to an OMDBFilm object.
                Gson gson = new GsonBuilder().create();
                OMDBFilm omdbfilm = gson.fromJson(res, OMDBFilm.class);

                Log.i(TAG, omdbfilm.toString());

                // Save the id for later use. (Mark as saved or watched).
                filmOmdbId = omdbfilm.getImdbID();

                // Populate fields on the screen.
                tvMovieTitle.setText(omdbfilm.getTitle());
                tvMovieYear.setText(omdbfilm.getYear());
                tvMovieGenre.setText(omdbfilm.getGenre());
                tvMovieRating.setText(String.format("%s/10", omdbfilm.getImdbRating()));
                tvMovieDirector.setText(omdbfilm.getDirector());
                tvMoviePlot.setText(omdbfilm.getPlot());
                // Display movie poster on the screen.
                setMovieImage(omdbfilm.getPoster());

                // Content is loaded, we can show it.
                svMovieDetails.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e(TAG, "API error: " + res);
            }
        });

        // Now, we will get the movie details especific for the user.
        // Ex. Movie is watched or saved to watch later.

        // Set post params
        RequestParams params = new RequestParams();
        params.put("token", apiToken);

        // Get movie details from Filmap
        FilmapRestClient.get("films/" + id, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                // We found a movie.
                Log.i(TAG, "Film found: " + res);

                // Parse json to a FilmapFilm class
                Gson gson = new GsonBuilder().create();
                FilmapFilm film = gson.fromJson(res, FilmapFilm.class);

                Log.i(TAG, "Film is watched: " + film.getWatched());

                // If the movie is saved on the api, it is at least marked to watch later.
                // So we can disable the watch later button and mark watchLater as true.
                disableWatchLaterButton();

                // If the movie was already marked as watched.
                isWatched = film.getWatched().equals("1");

                if (isWatched) {
                    disableWatchedButton();
                    Log.i(TAG, "Film watched");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // If the request fails, the movie is not saved yet.
                Log.i(TAG, "User hasn't saved this movie yet");
            }
        });

    }

    // Disable watch later button.
    private void disableWatchLaterButton() {
        btnWatchLater.setEnabled(false);
        btnWatchLater.setText("In your list");
        watchLater = true;
    }

    // Disable Watching button.
    private void disableWatchedButton() {
        btnWatchFilm.setEnabled(false);
        btnWatchFilm.setText("Watched");
        isWatched = true;
    }

    // Render poster on screen.
    private void setMovieImage(String url) {
        Picasso.with(this).load(url).into(ivMoviePoster);
    }
}
