package com.filmap.filmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.filmap.filmap.models.OMDBFilm;
import com.filmap.filmap.rest.OMDBRestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent thisIntent = getIntent();

        /*Log.d(TAG, )
        */

        if (thisIntent.hasExtra("omdbid")) {
            getMovieById(thisIntent.getStringExtra("omdbid"));
        }

        //getMovieById("tt0892769");
    }

    public void getMovieClick(View v){


    }

    public void getMovieById(String id) {

        OMDBRestClient.get("i=" + id + "&y=&plot=short&r=json", null, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                // called when response HTTP status is "200 OK"

                System.out.println(res);

                Gson gson = new GsonBuilder().create();
                // Define Response class to correspond to the JSON response returned
                OMDBFilm omdbfilm = gson.fromJson(res, OMDBFilm.class);

                System.out.println(omdbfilm.toString());

                tvMovieTitle.setText(omdbfilm.getTitle());
                tvMovieYear.setText(omdbfilm.getYear());
                tvMovieGenre.setText(omdbfilm.getGenre());
                tvMovieRating.setText(omdbfilm.getImdbRating());
                tvMovieDirector.setText(omdbfilm.getDirector());
                tvMoviePlot.setText(omdbfilm.getPlot());

                setMovieImage(omdbfilm.getPoster());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }
        });

    }

    private void setMovieImage(String url) {
        Picasso.with(this).load(url).into(ivMoviePoster);
    }
}
