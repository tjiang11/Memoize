package com.oosegroup19.memoize.layout;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oosegroup19.memoize.R;
import com.oosegroup19.memoize.activity.HomePageActivity;


/**
 * Implementation for the DropPin page on the app.
 */
public class DropPinFragment extends BaseFragment {
    //The name of the fragment
    public final static String FRAGMENTNAME = "DropPinFragment";
    private final String fragmentName = FRAGMENTNAME;

    //Instance fields
    private static String returnToFrag = "";

    private static String timeBasedReminderName = "";
    private static String timeBasedLocationDescription = "";
    private static String timeBasedEventDescription = "";

    private static String locationBasedEventName = "";
    private static String locationBasedEventLocationName = "";
    private static String locationBasedEventDescription = "";
    private static int locationBasedRadius = 100;

    //Instance fiels for Google maps
    MapView mMapView;
    private GoogleMap googleMap;
    private double finalLatitude = 0;
    private double finalLongitude = 0;

    /**
     * Required public constructor.
     */
    public DropPinFragment() {}

    /**
     * A constructor returning a new instance of a DropPinFragment.
     * @param returnTo The fragment type, to know which fragment type to segue to.
     * @return
     */
    public static DropPinFragment newInstance(String returnTo) {
        DropPinFragment fragment = new DropPinFragment();
        returnToFrag = returnTo;
        return fragment;
    }

    /**
     * A constructor returning a new instance of a DropPinFragment which persists
     * relevant information about locations.
     *
     * @param returnTo Which fragment type to segue back to.
     * @param name The name of the reminder
     * @param locDescription The location description of the reminder
     * @param eventDescription The event description of the reminder
     * @return A DropPinFragment
     */
    public static DropPinFragment newInstance(String returnTo, String name,
                                              String locDescription, String eventDescription) {
        DropPinFragment fragment = new DropPinFragment();
        returnToFrag = returnTo;

        timeBasedReminderName = name;
        timeBasedLocationDescription = locDescription;
        timeBasedEventDescription = eventDescription;

        return fragment;
    }

    /**
     * A constructor returning a new instance of a DropPinFragment which persists relevant
     * inforamtion about time reminders.
     *
     * @param returnTo Which fragment type to segue back to.
     * @param eventName The name of the event
     * @param eventLocationName The location of the event
     * @param eventLocationDescriptionName The location description of the event
     * @param radius The radius associated with the event
     * @return
     */
    public static DropPinFragment newInstance(String returnTo, String eventName,
                                              String eventLocationName, String eventLocationDescriptionName, int radius) {
        DropPinFragment fragment = new DropPinFragment();
        returnToFrag = returnTo;

        locationBasedEventName = eventName;
        locationBasedEventLocationName = eventLocationName;
        locationBasedEventDescription = eventLocationDescriptionName;
        locationBasedRadius = radius;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_drop_pin, container, false);

        //button listener
        Button saveLocationButton = (Button) rootView.findViewById(R.id.save_location_button);
        saveLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                Log.i("DropPinFrag", "Final latitude: " + finalLatitude + "Final longitude: " + finalLongitude);
                if (returnToFrag.equals("location")) {
                    fragment = LocationBasedNotificationFragment.newInstance(locationBasedEventLocationName, finalLatitude, finalLongitude,
                            locationBasedEventName, locationBasedEventDescription, locationBasedRadius);
                } else if (returnToFrag.equals("time")) {
                    fragment = TimeBasedNotificationFragment.newInstance(finalLatitude, finalLongitude,
                            timeBasedReminderName, timeBasedLocationDescription, timeBasedEventDescription);
                } else {
                    fragment = LocationBasedNotificationFragment.newInstance("", finalLatitude, finalLongitude);
                    Log.e("tag", "Error retrieving return fragment.");
                }
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Ref: http://stackoverflow.com/questions/19353255/how-to-put-google-maps-v2-on-a-fragment-using-viewpager
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    
                    Log.e("DropPinFragment", "Permissions for location not yet given...");

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    return;
                } else {
                    googleMap.setMyLocationEnabled(true);

                    // For dropping a marker at a point on the Map
                    final double currLat = HomePageActivity.currentLocation.getLatitude();
                    final double currLong = HomePageActivity.currentLocation.getLongitude();

                    finalLatitude = currLat;
                    finalLongitude = currLong;

                    Log.i("DropPinFrag", "Latitude: " + currLat + "Longitude: " + currLong);
                    LatLng initialPosition = new LatLng(currLat, currLong);

                    googleMap.addMarker(new MarkerOptions()
                            .position(initialPosition)
                            .title("My Current Location")
                            .snippet("Drag the pin to set the location!")
                            .draggable(true));

                    googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

                        @Override
                        public void onMarkerDragStart(Marker marker) {
                            Log.i("DropPinFrag", "Dragging start");
                        }

                        @Override
                        public void onMarkerDragEnd(Marker marker) {
                            Log.i("DropPinFrag", "Dragged lat: " + marker.getPosition().latitude + " Dragged long" +
                                marker.getPosition().longitude);
                            finalLatitude = marker.getPosition().latitude;
                            finalLongitude = marker.getPosition().longitude;
                        }

                        @Override
                        public void onMarkerDrag(Marker marker) {
                            //System.out.println("Dragging");
                        }
                    });

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(initialPosition).zoom(12).build();

                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }

            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /**
     * Requests permissions for google maps.
     * @param requestCode The request code.
     * @param permissions An array of permissions requested.
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e("DropPinFrag", "onrequestpermissionsresult called");
        if (grantResults[0] == 0 && grantResults[1] == 0) { //permission granted
            Log.i("DropPinFrag", "onrequestpermissionsresult 2 called");
            googleMap.setMyLocationEnabled(true);

            // For dropping a marker at a point on the Map
            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

            // For zooming automatically to the location of the marker
            CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }
    public String getFragmentName() {
        return this.fragmentName;
    }
}
