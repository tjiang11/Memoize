package com.oosegroup19.memoize.scheduler;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.oosegroup19.memoize.R;
import com.oosegroup19.memoize.activity.HomePageActivity;
import com.oosegroup19.memoize.structures.LastResortReminderItem;
import com.oosegroup19.memoize.structures.LocationReminderItem;
import com.oosegroup19.memoize.structures.ReminderItemAdapter;
import com.oosegroup19.memoize.structures.TimeReminderItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import okhttp3.Response;

import static com.oosegroup19.memoize.activity.HomePageActivity.PREFS_NAME;

/**
 * This {@code IntentService} does the app's actual work.
 * {@code SampleAlarmReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class SampleSchedulingService extends IntentService {
    Context context;
    public SampleSchedulingService() {
        super("SchedulingService");
    }

    public static int PORT = 8001;

    //public static String baseURL = "http://10.0.2.2:" + PORT; //uncomment if you are using emulator, use 10.0.3.2 for genymotion, 10.0.2.2 if not
    public static String baseURL = "http://9194e27a.ngrok.io"; //uncomment and put your ngrok url here if using ngrok tunneling
    public static String PREFS_NAME = "myPrefs";

    public static final String TAG = "Scheduling Demo";
    // An ID used to post the notification.
    public static final int NOTIFICATION_ID = 1;
    // The string the app searches for in the Google home page content. If the app finds 
    // the string, it indicates the presence of a doodle.  
    public static final String SEARCH_STRING = "doodle";
    // The Google home page URL from which the app fetches content.
    // You can find a list of other Google domains with possible doodles here:
    // http://en.wikipedia.org/wiki/List_of_Google_domains
    public static final String URL = "http://www.google.com";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    @Override
    protected void onHandleIntent(Intent intent) {
//        // BEGIN_INCLUDE(service_onhandle)
//        // The URL from which to fetch content.
//        String urlString = URL;
//
//        String result ="";
//
//        // Try to connect to the Google homepage and download content.
//        try {
//            result = loadFromNetwork(urlString);
//        } catch (IOException e) {
//            Log.i(TAG, getString(R.string.connection_error));
//        }
//
//        // If the app finds the string "doodle" in the Google home page content, it
//        // indicates the presence of a doodle. Post a "Doodle Alert" notification.
//        if (result.indexOf(SEARCH_STRING) != -1) {
//            sendNotification("found!");
//            Log.i(TAG, "Found doodle!!");
//        } else {
//            sendNotification("Not found!");
//            Log.i(TAG, "No doodle found. :-(");
//        }
//        // Release the wake lock provided by the BroadcastReceiver.
//        SampleAlarmReceiver.completeWakefulIntent(intent);
//        // END_INCLUDE(service_onhandle)
//
        context = getApplicationContext();
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        AndroidNetworking.get(baseURL + "/users/" + settings.getString("user_id", "0") + "/locationreminders/")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        LocationReminderItem[] myReminderItems = gson.fromJson(response, LocationReminderItem[].class);

                        for (LocationReminderItem reminderItem : myReminderItems) {
                            Log.i("ReminderLogFrag", reminderItem.toString());
                            sendNotification(reminderItem.getName());
                            deleteReminder("location", reminderItem.getId());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tag", "" + anError.getErrorBody());
                    }
                });

        AndroidNetworking.get(baseURL + "/users/" + settings.getString("user_id", "0") + "/timereminders/")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        TimeReminderItem[] myReminderItems = gson.fromJson(response, TimeReminderItem[].class);

                        for (TimeReminderItem reminderItem : myReminderItems) {
                            reminderItem.convertTime();
                            Log.i("ReminderLogFrag", reminderItem.toString());
                            sendNotification(reminderItem.getName());
                            deleteReminder("time", reminderItem.getId());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tag", "" + anError.getErrorBody());
                    }
                });

        AndroidNetworking.get(baseURL + "/users/" + settings.getString("user_id", "0") + "/lastresortreminders/")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        LastResortReminderItem[] myReminderItems = gson.fromJson(response, LastResortReminderItem[].class);

                        for (LastResortReminderItem reminderItem : myReminderItems) {
                            reminderItem.convertTime();
                            Log.i("ReminderLogFrag", reminderItem.toString());
                            sendNotification(reminderItem.getName());
                            deleteReminder("lastresort", reminderItem.getId());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tag", "" + anError.getErrorBody());
                    }
                });

        SampleAlarmReceiver.completeWakefulIntent(intent);
    }
    
    // Post a notification indicating whether a doodle was found.
    private void sendNotification(String msg) {
        Random r = new Random();

        mNotificationManager = (NotificationManager)
               this.getSystemService(Context.NOTIFICATION_SERVICE);
    
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
            new Intent(this, HomePageActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle(msg)
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(r.nextInt(), mBuilder.build());
    }

    private void deleteReminder(String type, int reminderId) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        AndroidNetworking.delete(baseURL + "/users/" + settings.getString("user_id", "0") + "/" + type + "remindersdetail/" + reminderId)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        makeToast("Successfully deleted.");
                        Log.i("tag", "Successfully deleted");
                    }

                    @Override
                    public void onError(ANError anError) {
                        makeToast("Error attempting to delete.");
                        Log.e("tag", "Failed to delete.");
                    }
                });
    }
 
//
// The methods below this line fetch content from the specified URL and return the
// content as a string.
//
    /** Given a URL string, initiate a fetch operation. */
    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str ="";
      
        try {
            stream = downloadUrl(urlString);
            str = readIt(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }      
        }
        return str;
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     * @param urlString A string representation of a URL.
     * @return An InputStream retrieved from a successful HttpURLConnection.
     * @throws IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {
    
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }

    /** 
     * Reads an InputStream and converts it to a String.
     * @param stream InputStream containing HTML from www.google.com.
     * @return String version of InputStream.
     * @throws IOException
     */
    private String readIt(InputStream stream) throws IOException {
      
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        for(String line = reader.readLine(); line != null; line = reader.readLine()) 
            builder.append(line);
        reader.close();
        return builder.toString();
    }

    public void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
