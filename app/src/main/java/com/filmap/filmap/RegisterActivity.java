package com.filmap.filmap;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordConfirmation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordConfirmation = (EditText) findViewById(R.id.etPasswordConfirmation);
    }

    public void registerClick(View view) {
        // Do stuff to register user

        // Set post params
        RequestParams params = new RequestParams();
        params.put("name", etName.getText());
        params.put("email", etEmail.getText());
        params.put("password", etPassword.getText());
        params.put("password_confirmation", etPasswordConfirmation.getText());

        // Make a post request to authenticate
        FilmapRestClient.post("user", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                // Called when response HTTP status is "200 OK"

                try {
                    JSONObject mainObject = new JSONObject(res);

                    if (mainObject.has("response")) {
                        Boolean response = mainObject.getBoolean("response");

                        if(response == true) {
                            showMessage("Success! You can now Sign In.");
                            finish();
                        } else {
                            showMessage(res);
                        }

                    } else {

                        showMessage(res);
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

    public void signInClick(View v) {
        // Go back to the sign in screen
        finish();
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
