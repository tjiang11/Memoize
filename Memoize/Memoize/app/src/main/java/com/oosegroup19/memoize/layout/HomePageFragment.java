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
public class HomePageFragment extends BaseFragment {
    public final static String FRAGMENTNAME = "HomePageFragment";
    private final String fragmentName = FRAGMENTNAME;


    private static final String DEBUG_TAG = "HttpExample";

    private TextView textView;

//    //Sarah's variables for moving between fragments
    private CharSequence mTitle;

    public String getFragmentName(){
        return this.fragmentName;
    }

    public static HomePageFragment newInstance(User user) {
        HomePageFragment fragment = new HomePageFragment();
        // owner = user;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        textView = (TextView) view.findViewById(R.id.textView2);

        AndroidNetworking.setParserFactory(new JacksonParserFactory());

        AndroidNetworking.get(HomePageActivity.baseURL + "/hello/")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        textView.setText(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        textView.setText("noo");
                        Log.e("tag", anError.getMessage());
                    }
                });

        //button listener
        Button button1 = (Button) view.findViewById(R.id.test_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectItem(1);
            }
        });

        return view;
    }

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
