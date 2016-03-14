package com.filmap.filmap;

import android.app.Fragment;
import android.app.FragmentManager;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import layout.FilmsFragment;
import layout.SearchFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FilmsFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener {

    private TextView navHeaderName;
    private TextView navHeaderEmail;
    private ImageView ivGravatar;

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

        navHeaderName = (TextView) header.findViewById(R.id.navHeaderName);
        navHeaderEmail = (TextView) header.findViewById(R.id.navHeaderEmail);
        ivGravatar = (ImageView) header.findViewById(R.id.ivGravatar);

        //navHeaderEmail.setText("email@goeshere.com");

        //setContentView(R.layout.activity_main);


        //navHeaderName.setText("Name");

       // Log.d("PROPRETIES", "PROPRETIES");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPref = getSharedPreferences(SignInActivity.SETTINGS_NAME,
                Context.MODE_PRIVATE);

        // verify if the user is logged in, if not, call sign in activity
        if (! sharedPref.contains("token")) {
            System.out.println("CAN I HAS TOKEN PLZ!");
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        } else {
            // handle displaying profile and gravatar stuff
            String name = sharedPref.getString("name", "");
            String email = sharedPref.getString("email", "").toLowerCase();

            navHeaderEmail.setText(email);
            navHeaderName.setText(name);

            String hash = MD5Util.md5(email.trim());

            System.out.println(hash);

            Picasso.with(this).load("http://www.gravatar.com/avatar/" + hash + "?s=400").
                    into(ivGravatar);
            System.out.println("WE HAS A TOKENS!");
        }
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

        if (id == R.id.nav_near_me) {
            // Handle the camera action
            //Intent intent = new Intent(this, ListMovies.class);
            //startActivity(intent);
        } else if (id == R.id.nav_search) {
            // Search for a movie


            // We have to save the fragment instead of creating a new one every time you go in and out
            if (searchFragment == null) {
                searchFragment = new SearchFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, searchFragment).commit();

        } else if (id == R.id.nav_my_list) {
            // Use replace instead of add, or it will display all fragments at the same time
            // And they will overlap each other

            if (filmsFragment == null) {
                filmsFragment = new FilmsFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, filmsFragment).commit();

        } else if (id == R.id.nav_manage) {
            // Settings
        } else if (id == R.id.nav_sign_out) {
            // remove token and show login screen
            System.out.println("SIGNING OUT");

            // Remove token from shared preferences.
            SharedPreferences sharedPref = getSharedPreferences(SignInActivity.SETTINGS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.remove("token");
            editor.remove("name");
            editor.remove("email");
            editor.commit();

            if (! sharedPref.contains("token")) {
                System.out.println("CAN I HAS TOKEN PLZ!");
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
            } else {
                System.out.println("WE HAS A TOKENS! IUUUPIIII!");

            }


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFilmsFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSearchFragmentInteraction(Uri uri) {

    }

}
