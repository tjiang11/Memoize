package com.oosegroup19.memoize.layout;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.oosegroup19.memoize.R;
import com.oosegroup19.memoize.activity.HomePageActivity;
import com.oosegroup19.memoize.structures.User;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by smsukardi on 11/12/16.
 */

/**
 * HomePage fragment which contains methods that ensure that
 * API calls can be made with the server.
 */
public class HomePageFragment extends BaseFragment {

    //Variables for moving between fragments
    public final static String FRAGMENTNAME = "HomePageFragment";
    private final String fragmentName = FRAGMENTNAME;
    private TextView textView;
    private CharSequence mTitle;

    //Method to retrieve the fragment name
    public String getFragmentName(){
        return this.fragmentName;
    }

    //Constructor to generate a new instance of the HomePageFragment
    public static HomePageFragment newInstance(User user) {
        HomePageFragment fragment = new HomePageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflates the view
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        //Finds the TextView
        textView = (TextView) view.findViewById(R.id.textView2);

        AndroidNetworking.setParserFactory(new JacksonParserFactory());

        //API Call to test if connected with the Django REST server
        AndroidNetworking.get(HomePageActivity.baseURL + "/hello/")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        textView.setText(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        textView.setText("Please connect to the internet.");
                        Log.e("tag", anError.getMessage());
                    }
                });

        //Button listener to go to the 2nd tag (index 1)
        Button button1 = (Button) view.findViewById(R.id.test_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectItem(1);
            }
        });

        return view;
    }

    /**
     * Method to select a particular fragment to go to
     * @param position The position selected
     */
    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 1:
                mTitle = "New Lesson";
                fragment = new ReminderLogFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_main, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

}
