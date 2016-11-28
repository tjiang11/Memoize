package com.oosegroup19.memoize.layout;

import android.Manifest;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DropPinFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DropPinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DropPinFragment extends BaseFragment {
    public final static String FRAGMENTNAME = "DropPinFragment";
    private final String fragmentName = FRAGMENTNAME;

    MapView mMapView;
    private GoogleMap googleMap;
    private double finalLatitude = 0;
    private double finalLongitude = 0;

    public DropPinFragment() {
        // Required empty public constructor
    }

    public static DropPinFragment newInstance(String param1, String param2) {
        DropPinFragment fragment = new DropPinFragment();
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
                Log.i("DropPinFrag", "Final latitude: " + finalLatitude + "Final longitude: " + finalLongitude);
                LocationBasedNotificationFragment fragment = LocationBasedNotificationFragment.newInstance("", finalLatitude, finalLongitude);
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
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
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