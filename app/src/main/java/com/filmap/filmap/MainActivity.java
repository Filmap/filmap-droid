package com.filmap.filmap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.filmap.filmap.utils.MD5Util;
import com.squareup.picasso.Picasso;

import layout.FilmsFragment;
import layout.SearchFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FilmsFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener {

    // For showing user info.
    private TextView navHeaderName;
    private TextView navHeaderEmail;
    private ImageView ivGravatar;

    // For logging...
    private final String TAG = "MainActivity";

    // Fragments
    FilmsFragment filmsFragment;
    SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        // Set up components for interaction.
        navHeaderName = (TextView) header.findViewById(R.id.navHeaderName);
        navHeaderEmail = (TextView) header.findViewById(R.id.navHeaderEmail);
        ivGravatar = (ImageView) header.findViewById(R.id.ivGravatar);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if there is an authenticated user.
        // If not, show login screen.
        checkAuth();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
            getSupportActionBar().setTitle("Near Me");
        if (id == R.id.nav_near_me) {
            // Show map with nearby movies.
            Intent intent = new Intent(this, NearActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_search) {
            // Search for a movie

            // We have to save the fragment instead of creating a new one every time you go in and out
            // Use replace instead of add, or it will display all fragments at the same time
            // And they will overlap each other
            if (searchFragment == null) {
                searchFragment = new SearchFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, searchFragment).commit();
            getSupportActionBar().setTitle("Search");

        } else if (id == R.id.nav_my_list) {
            // Display the list of movies of the user.
            if (filmsFragment == null) {
                filmsFragment = new FilmsFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, filmsFragment).commit();
            getSupportActionBar().setTitle("My List");
        } else if (id == R.id.nav_manage) {
            // Settings
            getSupportActionBar().setTitle("Settings");
        } else if (id == R.id.nav_sign_out) {
            doLogout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Check if there is an authenticated user.
    // If not, show login screen.
    private void checkAuth() {
        // Get a shared preferences instance.
        SharedPreferences sharedPref = getSharedPreferences(SignInActivity.SETTINGS_NAME,
                Context.MODE_PRIVATE);

        // Check if there is an api token in the shared preferences...
        if (sharedPref.contains("token")) {
            // There is a token.
            Log.i(TAG, "Token found. Authenticating.");

            // Display user info in menu bar.
            String name = sharedPref.getString("name", "");
            String email = sharedPref.getString("email", "").toLowerCase();

            navHeaderEmail.setText(email);
            navHeaderName.setText(name);

            // Get an md5 of the user email to get their Gravatar profile pic.
            String hash = MD5Util.md5(email.trim());
            Log.i(TAG, "Gravatar hash: " + hash);
            Picasso.with(this).load("http://www.gravatar.com/avatar/" + hash + "?s=400").into(ivGravatar);
        } else {
            // No token.
            Log.i(TAG, "Token not found. Open authentication screen.");

            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        }
    }

    // Log the user out of the application.
    private void doLogout() {
        // remove token and show login screen
        Log.i(TAG, "Signing out...");

        // Remove token and user info from shared preferences.
        SharedPreferences sharedPref = getSharedPreferences(SignInActivity.SETTINGS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("token");
        editor.remove("name");
        editor.remove("email");
        editor.commit();

        // Empty fragments to erase current user's data.
        filmsFragment = null;
        searchFragment = null;

        // If the token was erased.
        if (! sharedPref.contains("token")) {
            // Open sign in intent.
            Log.i(TAG, "Logged out.");
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        } else {
            Log.e(TAG, "User still has a token. Error logging out.");
        }
    }

    @Override
    public void onFilmsFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSearchFragmentInteraction(Uri uri) {

    }

}
