package com.oosegroup19.memoize;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by smsukardi on 11/14/16.
 */

/** Listener class to retrieve location coordinates
 */
public class MyLocationListener implements LocationListener {

    //Instance fields
    Context context;
    public static double currLatitude;
    public static double currLongitude;
    public static boolean hasLatitude = false;

    //The current best location
    private Location currentBestLocation = null;


    /**The LocationListener constructor.
     * @param c The context.
     */
    public MyLocationListener(Context c) {
        this.context = c;
    }


    /**Called whenever the location is changed.
     * @param loc The new location changed to.
     */
    @Override
    public void onLocationChanged(Location loc) {
        // Logs teh latitude and longitude
        String longitude = "Longitude: " + loc.getLongitude();
        Log.i("MyLocationListener", longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        Log.i("MyLocationListener", latitude);

        this.hasLatitude = true;
        this.currLatitude = loc.getLatitude();
        this.currLongitude = loc.getLongitude();

        //Retrieves city name from coordinates
        String cityName = null;
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        String s = longitude + "\n" + latitude + "\n\nMy Current City is: " + cityName;
        Log.v("MyLocationListener", s);
    }


    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}