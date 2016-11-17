package com.oosegroup19.memoize;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by smsukardi on 11/14/16.
 */

/*---------- Listener class to get coordinates ------------- */
public class MyLocationListener implements LocationListener {

    Context context;

    public MyLocationListener(Context c) {
        this.context = c;
    }

    @Override
    public void onLocationChanged(Location loc) {
//        editLocation.setText("");
//        pb.setVisibility(View.INVISIBLE);
        Toast.makeText(context, "Location changed: Lat: " + loc.getLatitude() + " Lng: " + loc.getLongitude(), Toast.LENGTH_SHORT).show();
        String longitude = "Longitude: " + loc.getLongitude();
        Log.v("MyLocationListener", longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        Log.v("MyLocationListener", latitude);

        /*------- To get city name from coordinates -------- */
        String cityName = null;
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;

        try {
            Log.i("MyLocationListener", "In try block 1");
            addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            Log.i("MyLocationListener", "In try block 2");
            if (addresses.size() > 0) {
                Log.i("MyLocationListener", "In try block 3");
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
        }
        catch (IOException e) {
            Log.i("MyLocationListener", "In catch block");
            e.printStackTrace();
        }

        //Why isn't this line executing?
        String s = longitude + "\n" + latitude + "\n\nMy Current City is: " + cityName;
        Log.v("MyLocationListener", s);
//        editLocation.setText(s);
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}