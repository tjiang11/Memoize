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

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //button listeners
        Button loginButton = (Button) findViewById(R.id.loginButton);
        final TextView emailField = (TextView) findViewById(R.id.enterEmail);
        final TextView passwordField = (TextView) findViewById(R.id.enterPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = emailField.getText().toString();
                String password = passwordField.getText().toString();
                if (username.equals("") || password.equals("")) {
                    makeToast("One or more of your fields has not been filled.");
                } else {
                    AndroidNetworking.post(HomePageActivity.baseURL + "/api-token-auth-id/")
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


        //button listeners
        Button newAccountButton = (Button) findViewById(R.id.newAccountButton);

        newAccountButton.setText("New Account");
        newAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    public void getUserToken(String username, String password) {
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

    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void movetoMain() {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }

    /*
    public void moveToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    } */
}
