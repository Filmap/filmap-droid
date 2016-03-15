package com.filmap.filmap;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;


import com.filmap.filmap.models.FilmapNearFilm;
import com.filmap.filmap.models.OMDBFilm;
import com.filmap.filmap.rest.FilmapRestClient;
import com.filmap.filmap.rest.OMDBRestClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

public class NearActivity extends FragmentActivity implements OnMapReadyCallback {



    private GoogleMap mMap;
    private Set<FilmMarker> filmTargets = new HashSet<FilmMarker>();

    // Misc
    private final String TAG = "Near";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Get current location
        // Natal: -5.810184, -35.192427
        // Cali: 37.385241,-122.096532
         LatLng currentLocation = new LatLng( -5.810184, -35.192427);
        // Add marker
         mMap.addMarker(new MarkerOptions().position(currentLocation).title("You"));
         mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        // Get near films
         nearFilms(10.0, currentLocation.latitude, currentLocation.longitude);



        // Add a marker in Sydney and move the camera

//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void addFilmMarker(String omdbID, Double lat, Double lng, String posterUrl) {
        LatLng film_location = new LatLng(lat, lng);

        Marker m = mMap.addMarker(new MarkerOptions().position(film_location));
        FilmMarker fm = new FilmMarker(m);
        filmTargets.add(fm);
        Picasso.with(getApplicationContext())
                .load(posterUrl)
                .resize(60,100)
                .onlyScaleDown()
                .centerCrop()
                .into(fm);


        Log.i(TAG, "Film marker for " + omdbID + ", poster = " + posterUrl + " at " + film_location.toString());
    }

    private void nearFilms(Double radius, Double lat, Double lng) {
        String query = "near/" + radius + "," + lat + "," + lng;
        Log.i("Search", "searchMovies " + query);


        // encode query to send over as a query string
        try {
            query = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // went wrong...
        };

        FilmapRestClient.get(query, null, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                // called when response HTTP status is "200 OK"

                Log.i(TAG, "Response: " + res);


                Gson gson = new GsonBuilder().create();
                // Define Response class to correspond to the JSON response returned
                FilmapNearFilm[] filmapNearResult = gson.fromJson(res, FilmapNearFilm[].class);

                Log.i(TAG, "Response #2: " + filmapNearResult.length);

                int result_length = filmapNearResult.length;
                if (result_length > 0) {

                    // add pins

                    for (FilmapNearFilm aFilmapNearResult : filmapNearResult) {
                        Log.i(TAG, "Film found: " + aFilmapNearResult.getOmdb());
                        getFilmFromOMDB(
                                aFilmapNearResult.getOmdb(),
                                aFilmapNearResult.getLat(),
                                aFilmapNearResult.getLng()
                        );
                    }

                } else {
                    Log.i(TAG, "False response");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e(TAG, "Failure response: " + res);
            }
        });
    }

    private void getFilmFromOMDB(String omdbID, final String lat, final String lng) {
        OMDBRestClient.getFilm(omdbID, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "no film found");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.i(TAG, "poster found: " + responseString);
                // Parse JSON to an OMDBFilm object.
                Gson gson = new GsonBuilder().create();
                OMDBFilm omdbfilm = gson.fromJson(responseString, OMDBFilm.class);

                Log.i(TAG, omdbfilm.toString());

                // Save the id for later use. (Mark as saved or watched).
                String posterUrl = omdbfilm.getPoster();
                addFilmMarker(
                        omdbfilm.getImdbID(),
                        Double.valueOf(lat), Double.valueOf(lng),
                        posterUrl
                );

            }
        });
    }

    class FilmMarker implements Target {
        private Marker m;

        public FilmMarker(Marker m) { this.m = m; }

        @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            m.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
            filmTargets.remove(this);
            Log.i(TAG, " @+ Set bitmap for "+m.getTitle()+" PT size: #"+filmTargets.size());
        }

        @Override public void onBitmapFailed(Drawable errorDrawable) {
            Log.i(TAG, " @+ [ERROR] Don't set bitmap for " + m.getTitle());
            filmTargets.remove(this);
        }

        @Override public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }
}
