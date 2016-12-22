package com.oosegroup19.memoize.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.oosegroup19.memoize.R;
import com.oosegroup19.memoize.layout.NewNotificationFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import static com.oosegroup19.memoize.activity.HomePageActivity.PREFS_NAME;

/** The LoginActivity to log into the application.
 *
 */
public class LoginActivity extends AppCompatActivity {

    /** The onCreate method of the LoginActivity.
     * @param savedInstanceState The saved instance state of the loginactivity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Retrieves UI components
        Button loginButton = (Button) findViewById(R.id.loginButton);
        final TextView emailField = (TextView) findViewById(R.id.enterEmail);
        final TextView passwordField = (TextView) findViewById(R.id.enterPassword);

        //Executed when the user attempts to login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = emailField.getText().toString();
                String password = passwordField.getText().toString();

                //Error checks for if fields have been filled
                if (username.equals("") || password.equals("")) {
                    makeToast("One or more of your fields has not been filled.");
                } else {
                    //Makes a RESTful API call attempting to log in
                    AndroidNetworking.post(HomePageActivity.baseURL + "/api-token-auth-id/")
                            .addBodyParameter("username", username)
                            .addBodyParameter("password", password)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                     @Override
                                     public void onResponse(JSONObject response) {
                                         //Saves the login information into shared preferences if user successfully
                                         //logged in and moves to HomePageActivity.
                                         SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                         SharedPreferences.Editor editor = settings.edit();
                                         try {
                                             Log.i("tag", "Got token " + response.getString("token"));
                                             Log.i("tag", "Got user id: " + response.getString("id"));
                                             editor.putString("user_token", response.getString("token"));
                                             editor.putString("user_id", response.getString("id"));
                                             editor.apply(); //editor.commit() for synchronous
                                         } catch (JSONException e) {
                                             Log.e("tag", "Could not parse token from JSON object");
                                             Log.e("tag", e.toString());
                                         }
                                         movetoMain();
                                     }

                                     @Override
                                     public void onError(ANError anError) {
                                         Log.e("tag", "Could not fetch token.");
                                         Log.e("tag", anError.getErrorBody());
                                         makeToast("Invalid username or password.");
                                     }
                            });
                }
            }
        });


        //Button Listener
        Button newAccountButton = (Button) findViewById(R.id.newAccountButton);

        //Sets the text of the button
        newAccountButton.setText("New Account");

        //Executed when the user wants to create a new account
        newAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Makes a request to the RESTful API attempting to create a new account.
                AndroidNetworking.post(HomePageActivity.baseURL + "/users/")
                        .addBodyParameter("username", emailField.getText().toString())
                        .addBodyParameter("password", passwordField.getText().toString())
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                makeToast("You have successfully made an account!");
                                Log.i("tag", "Success: create account");
                                Log.i("tag", response.toString());
                                getUserToken(emailField.getText().toString(), passwordField.getText().toString());
                                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                SharedPreferences.Editor editor = settings.edit();
                                try {
                                    editor.putString("user_id", response.getString("id"));
                                    editor.apply(); //editor.commit() for synchronous
                                } catch (JSONException e) {
                                    Log.e("tag", "Could not parse user id");
                                }
                                movetoMain();
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.e("tag", "Could not create user.");
                                Log.e("tag", anError.toString());
                                makeToast("Error creating account.");
                            }
                        });

            }
        });

    }

    /** Retrieves the user token for authentication.
     * @param username The username
     * @param password the password
     */
    public void getUserToken(String username, String password) {
        // Makes a REST-ful api call retrieving a token for authentication.
        AndroidNetworking.post(HomePageActivity.baseURL + "/api-token-auth/")
                .addBodyParameter("username", username)
                .addBodyParameter("password", password)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        try {
                            Log.i("tag", "Got token " + response.getString("token"));
                            editor.putString("user_token", response.getString("token"));
                            editor.apply(); //editor.commit() for synchronous
                        } catch (JSONException e) {
                            Log.e("tag", "Could not parse token from JSON object");
                            Log.e("tag", e.toString());
                        }
                        movetoMain();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tag", "Could not fetch token.");
                        Log.e("tag", anError.getErrorBody());
                        makeToast("Coudl not fetch user token.");
                    }
                });
    }

    /** Creates a toast.
     * @param message The contents of the toast.
     */
    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /** Moves to the main method upon a successful login.
     */
    public void movetoMain() {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }
}
