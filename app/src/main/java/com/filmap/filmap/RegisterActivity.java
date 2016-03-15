package com.filmap.filmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.filmap.filmap.rest.FilmapRestClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = "Register";

    // Editable fields.
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordConfirmation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Bitch please.
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordConfirmation = (EditText) findViewById(R.id.etPasswordConfirmation);
    }

    // Vespas Mandarinas is a very neat band.
    public void registerClick(View view) {
        // Do stuff to register user
        Log.i(TAG, "registerClick");
        register(etName.getText().toString(), etEmail.getText().toString(),
                etPassword.getText().toString(), etPasswordConfirmation.getText().toString());

    }

    // You should check out their music.
    public void signInClick(View v) {
        // Go back to the sign in screen
        finish();
    }

    // Display a toast message.
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Register the user
    public void register(String name, String email,
                         String password, String password_confirmation) {

        // Check if the password confirmation match the password.
        if (! password.equals(password_confirmation)) {
            showMessage("Passwords don't match");
        }

        // Set post params
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("email", email);
        params.put("password", password);
        params.put("password_confirmation", password_confirmation);

        // Make a post request to authenticate
        FilmapRestClient.post("user", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                // Called when response HTTP status is "200 OK"

                try {
                    JSONObject mainObject = new JSONObject(res);

                    // Check if the request has a response and if response is true
                    if (mainObject.has("response")) {
                        Boolean response = mainObject.getBoolean("response");
                        Log.i(TAG, response.toString());

                        if (response) {
                            // All done. Show login page.
                            showMessage("You can now Sign In.");
                            finish();
                        } else {
                            showMessage(res);
                        }

                    } else {
                        showMessage(res);
                    }
                } catch (Exception e) {
                    // Connection error.
                    System.out.println(e.getMessage());

                    showMessage("Connection error. Please make sure you have an active internet " +
                            "connection and try again.");
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                showMessage("Bad register. Try again using different credentials.");
            }
        });

    }
}
