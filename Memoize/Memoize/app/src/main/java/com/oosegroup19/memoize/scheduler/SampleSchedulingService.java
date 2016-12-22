package com.oosegroup19.memoize.scheduler;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
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

/** The SampleSchedulingService for notifications.
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
    //public static String baseURL = "https://e1ad007b.ngrok.io"; //uncomment and put your ngrok url here if using ngrok tunneling
    public static String baseURL = "https://memoize.herokuapp.com";

    public static String PREFS_NAME = "myPrefs";

    public static final String TAG = "Scheduling Demo";
    // An ID used to post the notification.
    public static final int NOTIFICATION_ID = 1;

    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    @Override
    protected void onHandleIntent(Intent intent) {
        //Gets the application context
        context = getApplicationContext();
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);

        //Retrieves the latitude and longitude from SharedPreferences
        Double lat = Double.longBitsToDouble(settings.getLong("current_latitude", 10));
        Double lon = Double.longBitsToDouble(settings.getLong("current_longitude", 15));

        //Makes a REST-ful API call to retrieve LocationReminders nearby
        AndroidNetworking.get(baseURL + "/users/" + settings.getString("user_id", "0")
                + "/locationreminders/" + "?latitude=" + lat + "&longitude=" + lon)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        //Places using GSON into array
                        Gson gson = new Gson();
                        LocationReminderItem[] myReminderItems = gson.fromJson(response, LocationReminderItem[].class);

                        //Sends a notification for every LocationReminderItem and removes it from
                        //the database
                        for (LocationReminderItem reminderItem : myReminderItems) {
                            sendNotification(reminderItem.getName(), reminderItem.getId());
                            deleteReminder("location", reminderItem.getId());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tag", "" + anError.getErrorBody());
                    }
                });

        //Makes a REST-ful API call to retrieve TimeReminders
        AndroidNetworking.get(baseURL + "/users/" + settings.getString("user_id", "0") + "/timereminders/" +
                "?get_current")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        //Sends a notification for every TimeReminder item and removes it
                        //from the database
                        Gson gson = new Gson();
                        TimeReminderItem[] myReminderItems = gson.fromJson(response, TimeReminderItem[].class);

                        for (TimeReminderItem reminderItem : myReminderItems) {
                            reminderItem.convertTime();
                            Log.i("ReminderLogFrag", reminderItem.toString());
                            sendNotification(reminderItem.getName(), reminderItem.getId());
                            deleteReminder("time", reminderItem.getId());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tag", "" + anError.getErrorBody());
                    }
                });

        //Makes a REST-ful API call to retrieve LastResortReminders
        AndroidNetworking.get(baseURL + "/users/" + settings.getString("user_id", "0")
                + "/lastresortreminders/" + "?latitude=" + lat + "&longitude=" + lon)

                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        LastResortReminderItem[] myReminderItems = gson.fromJson(response, LastResortReminderItem[].class);

                        //Sends a notification for every LastResortReminder item and removes it
                        //from the database
                        for (LastResortReminderItem reminderItem : myReminderItems) {
                            reminderItem.convertTime();
                            Log.i("ReminderLogFrag", reminderItem.toString());
                            sendNotification(reminderItem.getName(), reminderItem.getId());
                            deleteReminder("lastresort", reminderItem.getId());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tag", "" + anError.getErrorBody());
                    }
                });

        //Creates an AlarmManager object and sets the next alarm (so that alarms
        //can be set every 15 seconds)
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, SampleAlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 15000, alarmIntent);
        SampleAlarmReceiver.completeWakefulIntent(intent);
    }
    
    /**
     * Posts a notification.
     */
    private void sendNotification(String msg, int id) {
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
        mNotificationManager.notify(id, mBuilder.build());
    }


    /**Method to delete a reminder.
     *
     * @param type The type of reminder it is.
     * @param reminderId The id of the reminder.
     */
    private void deleteReminder(String type, int reminderId) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);

        //Makes the REST-ful API call to delete the reminder.
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
 

    /**
     * A method that, given a URL string, initiates a fetch operation.
     */
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

    /**A method to make a toast.
     * @param message The message in the toast.
     */
    public void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
