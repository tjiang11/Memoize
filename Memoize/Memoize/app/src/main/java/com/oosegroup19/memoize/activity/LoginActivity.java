package com.oosegroup19.memoize.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
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

import org.json.JSONObject;

import java.text.DecimalFormat;

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
                if (emailField.getText().toString().equals("") || passwordField.getText().toString().equals("")) {
                    makeToast("One or more of your fields has not been filled.");
                } else {
                    movetoMain();

                    /*
                    //make api call to create a new event!
                    AndroidNetworking.post(HomePageActivity.baseURL + "/users/1/locationreminders/")
                            .addBodyParameter("email", emailField.getText().toString())
                            .addBodyParameter("password", passwordField.getText().toString())
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    makeToast("You have successfully logged in!");
                                    Log.i("tag", "Success");
                                    Log.i("tag", response.toString());

                                    movetoMain();
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Log.e("tag", "noooooo");
                                    Log.e("tag", anError.toString());
                                    makeToast("Your Notification could not be saved to the database. Please try again with " +
                                            "a more secure connection.");
                                }
                            }); */
                }
            }
        });


        //button listeners
        Button newAccountButton = (Button) findViewById(R.id.newAccountButton);

        newAccountButton.setText("New Account");
        newAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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
