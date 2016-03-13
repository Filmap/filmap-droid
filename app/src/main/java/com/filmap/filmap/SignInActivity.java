package com.filmap.filmap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SignInActivity extends AppCompatActivity {

    public static final String SETTINGS_NAME = "Settings";

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
        // Set post params
        RequestParams params = new RequestParams();
        params.put("email", etEmail.getText());
        params.put("password", etPassword.getText());

        // Make a post request to authenticate
        FilmapRestClient.post("authenticate", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                // Called when response HTTP status is "200 OK"

                try {
                    JSONObject mainObject = new JSONObject(res);

                    if (mainObject.has("token")) {
                        String token = mainObject.getString("token");

                        System.out.println(token);

                        // save token in shared preferences
                        SharedPreferences sharedPref = getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("token", token);
                        editor.commit();

                        if (sharedPref.contains("token")) {
                            System.out.println("IT HAS TOKEN!");

                            // Finish this activity and let the app do it's job
                            finish();
                        }

                        showMessage(token);

                    } else {

                        showMessage("Invalid credentials. Please try again.");

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());

                    showMessage("Connection error. Please make sure you have an active internet connection and try again.");
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }
        });


    }

    public void registerClick(View v) {
        //System.out.println("register");

        // Start RegisterActivity
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setPositiveButton("OK", null);
        builder.setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
