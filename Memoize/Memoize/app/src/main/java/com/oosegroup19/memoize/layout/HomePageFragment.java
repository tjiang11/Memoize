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
    private int PORT = 8000;
    String test_url = "http://10.0.2.2:" + PORT + "/hello/";

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
        getActivity().setTitle("Testing...");

        textView = (TextView) view.findViewById(R.id.textView2);

        AndroidNetworking.setParserFactory(new JacksonParserFactory());

        AndroidNetworking.get(test_url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        textView.setText(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        textView.setText("noo");
                    }
                });

        //testConnection(test_url, "GET");

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

    public void testConnection(String test_url, String request_method) {
        new APITask(request_method).execute(test_url);
    }

    class APITask extends AsyncTask<String, Void, String> {
        String request_method;

        public APITask(String request_method) {
            this.request_method = request_method;
        }

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            URL url = null;
            JSONObject object = null;
            InputStream inStream = null;
            String returnVal = "Nothing";
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();
                inStream = urlConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                String temp, response = "";
                while ((temp = bReader.readLine()) != null) {
                    response += temp;
                }
//                object = new JSONObject(response);
//                Log.i(DEBUG_TAG, object.toString());
                returnVal = response;
            } catch (Exception e) {
                Log.e(DEBUG_TAG, e.toString());
                returnVal = e.toString();
            } finally {
                if (inStream != null) {
                    try {
                        // this will close the bReader as well
                        inStream.close();
                    } catch (IOException ignored) {
                    }
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                return returnVal;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            textView.setText(result);
        }
    }



//    public void testConnection() {
//        String stringUrl = test_url;
//        ConnectivityManager connMgr = (ConnectivityManager)
//                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        if (networkInfo != null && networkInfo.isConnected()) {
//            new DownloadWebpageTask().execute(stringUrl);
//        } else {
//            textView.setText("No network connection available.");
//        }
//    }
//
//    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//            // params comes from the execute() call: params[0] is the url.
//            try {
//                return downloadUrl(urls[0]);
//            } catch (IOException e) {
//                Log.d(DEBUG_TAG, e.getMessage());
//                return "Unable to retrieve web page. URL may be invalid.";
//            }
//        }
//        // onPostExecute displays the results of the AsyncTask.
//        @Override
//        protected void onPostExecute(String result) {
//            textView.setText(result);
//        }
//    }
//
//    private String downloadUrl(String myurl) throws IOException {
//        InputStream is = null;
//        // Only display the first 500 characters of the retrieved
//        // web page content.
//        int len = 500;
//
//        try {
//            URL url = new URL(myurl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            Log.d(DEBUG_TAG, "test log 2");
//            conn.setReadTimeout(10000 /* milliseconds */);
//            conn.setConnectTimeout(15000 /* milliseconds */);
//            conn.setRequestMethod("GET");
//            conn.setDoInput(true);
//            // Starts the query
//            Log.d(DEBUG_TAG, "test log 3");
//            conn.connect();
//            Log.d(DEBUG_TAG, "test log 4");
//            int response = conn.getResponseCode();
//            Log.d(DEBUG_TAG, "The response is: " + response);
//            is = conn.getInputStream();
//
//            // Convert the InputStream into a string
//            return readIt(is, len);
//            // Makes sure that the InputStream is closed after the app is
//            // finished using it.
//        } finally {
//            if (is != null) {
//                is.close();
//            }
//        }
//    }
//
//    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
//        Reader reader = null;
//        reader = new InputStreamReader(stream, "UTF-8");
//        char[] buffer = new char[len];
//        reader.read(buffer);
//        return new String(buffer);
//    }

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
