package com.filmap.filmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.filmap.filmap.models.OMDBFilm;
import com.filmap.filmap.models.OMDBSearchResult;
import com.filmap.filmap.rest.OMDBRestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ListMovies extends AppCompatActivity {

    ListView listView;
    OMDBFilmsAdapter adapter;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movies);

        listView = (ListView) findViewById(R.id.listView);
        etSearch = (EditText) findViewById(R.id.etSearch);

        ArrayList<OMDBFilm> filmsArray = new ArrayList<OMDBFilm>();
        adapter = new OMDBFilmsAdapter(this, filmsArray);
        listView.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = etSearch.getText().toString();
                searchMovies(query);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                OMDBFilm omdbfilm = (OMDBFilm) listView.getItemAtPosition(position);

                System.out.println(omdbfilm.toString());

                showMovie(omdbfilm.getImdbID());
            }
        });
    }

    public void showMovie(String omdbid) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra("omdbid", omdbid);
        startActivity(intent);
    }

    public void btnSearchClick(View v) {
        String query = etSearch.getText().toString();

        searchMovies(query);
    }


    private void searchMovies(String query) {

        ArrayList<OMDBFilm> results;

        // encode query to send over as a query string
        try {
            query = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // went wrong...
        };

        OMDBRestClient.post("?s=" + query + "&y=&plot=short&r=json", null, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                // called when response HTTP status is "200 OK"

                System.out.println(res);

                Gson gson = new GsonBuilder().create();
                // Define Response class to correspond to the JSON response returned
                OMDBSearchResult omdbSearchResult = gson.fromJson(res, OMDBSearchResult.class);

                System.out.println(omdbSearchResult.getResponse());


                if (omdbSearchResult.getResponse().equals("True")) {

                    // clear list
                    adapter.clear();
                    // add new results to list
                    adapter.addAll(omdbSearchResult.getSearch());


                    System.out.println(omdbSearchResult.getSearch().get(0).toString());

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }
        });


    }


}
