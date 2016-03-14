package com.filmap.filmap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageView;

import com.filmap.filmap.rest.FilmapRestClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SignInActivity extends AppCompatActivity {

    public static final String SETTINGS_NAME = "Settings";
    private static final String TAG = "SignIn";

    private EditText etEmail;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
    }


    public void signInClick(View v) {
        Log.i(TAG, "signInClick");
        signIn(etEmail.getText().toString(), etPassword.getText().toString());

    }

    private void signIn(String email, String password) {
        // Set post params
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);

        // Make a post request to authenticate
        FilmapRestClient.post("authenticate", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                // Called when response HTTP status is "200 OK"

                try {
                    JSONObject mainObject = new JSONObject(res);

                    if (mainObject.has("token")) {
                        String token = mainObject.getString("token");
                        String email = mainObject.getString("email");
                        String name = mainObject.getString("name");

                        Log.i(TAG, token);

                        // save token in shared preferences
                        SharedPreferences sharedPref = getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("token", token);
                        editor.putString("name", name);
                        editor.putString("email", email);
                        editor.apply();

                        if (sharedPref.contains("token")) {
                            Log.i(TAG, "IT HAS TOKEN!");

                            // Finish this activity and let the app do it's job
                            finish();
                        }

                        showMessage("Welcome!");
                        //showMessage(token);
                    } 
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    Log.i(TAG, "Connection error");
                    showMessage("Connection error. Please make sure you have an active internet connection and try again.");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.i(TAG, "Failure, invalid creds");

                etPassword.setText(""); // Empty the password field.
                showMessage("Invalid credentials. Please try again.");
            }
        });
    }

    public void registerClick(View v) {
        Log.i(TAG, "go to register");

        // Start RegisterActivity
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
