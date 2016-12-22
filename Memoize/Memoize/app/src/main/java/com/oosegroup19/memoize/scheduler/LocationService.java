package com.oosegroup19.memoize.scheduler;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


//Code from modified http://stackoverflow.com/questions/14478179/background-service-with-location-listener-in-android

/**The LocationService class is a background service that updates the location.
 * Created by smsukardi on 12/19/16.
 */
public class LocationService extends Service  {

    //Instance fields
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int MINUTE = 1000;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;

    public static String PREFS_NAME = "myPrefs";
    Context context;

    Intent intent;
    int counter = 0;

    @Override
    public void onCreate()
    {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        Log.i("LocationService", "Starting...");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    /**Returns if there is a better location.
     *
     * @param location The location input
     * @param currentBestLocation The current best location
     * @return Whether one location is better
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > MINUTE;
        boolean isSignificantlyOlder = timeDelta < -MINUTE;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }

    /**LocationListener for location updates.
     */
    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc) {
            context = getApplicationContext();

            //Prints the latitude and longitude of the new current location
            Log.i("*************", "Location changed");
            Log.i("*************", "Latitude: " + loc.getLatitude());
            Log.i("*************", "Longitude: " + loc.getLongitude());

            //Places the current latitude and longitude to shared preferences
            //to access in the scheduling service
            SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putLong("current_latitude", Double.doubleToRawLongBits(loc.getLatitude()));
            editor.putLong("current_longitude", Double.doubleToRawLongBits(loc.getLongitude()));
            editor.apply();
        }

        /**Creates a toast when the provider has been disabled.
         * @param provider The provider.
         */
        public void onProviderDisabled(String provider) {
            Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
        }

        /**Creates a toast when the provider has been enabled.
         * @param provider The provider.
         */
        public void onProviderEnabled(String provider) {
            Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        /**Called when the status is changed.
         * @param provider The provider
         * @param status The changed status
         * @param extras A bundle containing extra information.
         */
        public void onStatusChanged(String provider, int status, Bundle extras) {}

    }
}